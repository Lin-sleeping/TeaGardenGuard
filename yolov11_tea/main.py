import cv2
import numpy as np
import requests
import uuid
import os
import uvicorn
import base64
from datetime import datetime
from typing import Optional
from fastapi import FastAPI, File, UploadFile, HTTPException, Form
from fastapi.responses import JSONResponse, Response
from fastapi.staticfiles import StaticFiles
from pydantic import BaseModel
from ultralytics import YOLO

# 创建FastAPI应用
app = FastAPI(
    title="茶叶病虫害检测API",
    description="基于YOLO的茶叶病虫害检测服务，支持表单文件上传或URL参数",
    version="1.0.0",
)

# 配置完整基础URL（部署时修改为实际公网地址）
BASE_FULL_URL = "http://<ip>:8001"  # 本地测试用

# 挂载静态文件服务
app.mount("/static", StaticFiles(directory="static"), name="static")

# 配置
OUTPUT_DIR = "static/detections"
os.makedirs(OUTPUT_DIR, exist_ok=True)
STATIC_PATH = "/static/detections"

# 加载YOLO模型
model = YOLO("./1/best.pt")

# 健康检查端点
@app.get("/health")
async def health_check():
    return {"status": "healthy", "model_loaded": True}

# 添加请求模型
class DetectionRequest(BaseModel):
    img_url: str

@app.post("/detect/")
async def detect_objects(file: UploadFile = File(...)):
    """
    文件上传检测端点 - 返回JSON格式结果
    
    输入:
    - file: UploadFile - 图片文件 (支持 jpg, png, jpeg 等图片格式)
      - Content-Type: multipart/form-data
      - 字段名: file
    
    输出:
    - JSON格式响应:
      {
        "res": "检测到的类别名称，多个用逗号分隔",
        "image_base64": "带标注的图片base64编码字符串",
        "detections": [
          {
            "class_id": 类别ID,
            "class_name": "类别名称",
            "confidence": 置信度分数,
            "bbox": [x1, y1, x2, y2]  // 边界框坐标
          }
        ],
        "message": "检测成功",
        "timestamp": "ISO格式时间戳"
      }
    
    使用示例:
    curl -X POST "http://ip:8001/detect/" -F "file=@image.jpg"
    """
    # 检查文件类型
    if not file.content_type.startswith('image/'):
        raise HTTPException(
            status_code=422,
            detail="上传的文件不是有效的图片格式"
        )
    
    try:
        # 读取文件
        image_bytes = await file.read()
        image = cv2.imdecode(np.frombuffer(image_bytes, np.uint8), cv2.IMREAD_COLOR)
        
        # 验证图像
        if image is None or image.size == 0:
            raise ValueError("无法加载图像（可能是无效的图片格式）")
        
        # 转换为RGB格式
        if len(image.shape) == 3 and image.shape[2] == 3:
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        
        # 模型检测
        results = model.predict(image)
        annotated_image = results[0].plot()  # 生成带标注的图像
        
        # 将标注图像转换为base64编码
        annotated_bgr = cv2.cvtColor(annotated_image, cv2.COLOR_RGB2BGR)
        _, buffer = cv2.imencode('.jpg', annotated_bgr)
        img_base64 = base64.b64encode(buffer).decode('utf-8')
        
        # 提取检测结果详情
        detections = []
        detected_classes = set()
        for box in results[0].boxes:
            class_name = results[0].names[int(box.cls)]
            detected_classes.add(class_name)
            detections.append({
                "class_id": int(box.cls),
                "class_name": class_name,
                "confidence": float(box.conf),
                "bbox": [int(coord) for coord in box.xyxy.tolist()[0]]  # [x1, y1, x2, y2]
            })
        
        return {
            "res": ", ".join(detected_classes) if detected_classes else "无检测结果",
            "image_base64": img_base64,
            "detections": detections,
            "message": "检测成功",
            "timestamp": datetime.now().isoformat()
        }
    
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        import traceback
        traceback.print_exc()
        raise HTTPException(
            status_code=500,
            detail=f"处理失败: {str(e)}"
        )

@app.post("/detect/image")
async def detect_objects_image(file: UploadFile = File(...)):
    """
    文件上传检测端点 - 返回带标注的图片文件
    
    输入:
    - file: UploadFile - 图片文件 (支持 jpg, png, jpeg 等图片格式)
      - Content-Type: multipart/form-data
      - 字段名: file
    
    输出:
    - 二进制图片数据 (JPEG格式)
      - Content-Type: image/jpeg
      - Content-Disposition: attachment; filename=detection_result.jpg
      - 图片内容: 原图 + YOLO检测框和标签标注
    
    使用示例:
    curl -X POST "http://ip:8001/detect/image" -F "file=@image.jpg" -o result.jpg
    """
    # 检查文件类型
    if not file.content_type.startswith('image/'):
        raise HTTPException(
            status_code=422,
            detail="上传的文件不是有效的图片格式"
        )
    
    try:
        # 读取文件
        image_bytes = await file.read()
        image = cv2.imdecode(np.frombuffer(image_bytes, np.uint8), cv2.IMREAD_COLOR)
        
        # 验证图像
        if image is None or image.size == 0:
            raise ValueError("无法加载图像（可能是无效的图片格式）")
        
        # 转换为RGB格式
        if len(image.shape) == 3 and image.shape[2] == 3:
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        
        # 模型检测
        results = model.predict(image)
        annotated_image = results[0].plot()  # 生成带标注的图像
        
        # 将标注图像转换为字节
        annotated_bgr = cv2.cvtColor(annotated_image, cv2.COLOR_RGB2BGR)
        _, buffer = cv2.imencode('.jpg', annotated_bgr)
        
        return Response(
            content=buffer.tobytes(),
            media_type="image/jpeg",
            headers={"Content-Disposition": "attachment; filename=detection_result.jpg"}
        )
    
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        import traceback
        traceback.print_exc()
        raise HTTPException(
            status_code=500,
            detail=f"处理失败: {str(e)}"
        )

@app.post("/detect/json")
async def detect_objects_json(request: DetectionRequest):
    """
    URL检测端点 - 通过图片URL进行检测
    
    输入:
    - JSON格式请求体:
      {
        "img_url": "图片的HTTP/HTTPS URL地址"
      }
      - Content-Type: application/json
    
    输出:
    - JSON格式响应:
      {
        "res": "检测到的类别名称，多个用逗号分隔",
        "resimg_url": "带标注图片的访问URL",
        "detections": [
          {
            "class_id": 类别ID,
            "class_name": "类别名称", 
            "confidence": 置信度分数,
            "bbox": [x1, y1, x2, y2]  // 边界框坐标
          }
        ],
        "message": "检测成功",
        "timestamp": "ISO格式时间戳"
      }
    
    使用示例:
    curl -X POST "http://ip:8001/detect/json" \
         -H "Content-Type: application/json" \
         -d '{"img_url": "https://example.com/image.jpg"}'
    """
    try:
        # 处理URL
        response = requests.get(request.img_url, timeout=10)
        response.raise_for_status()
        
        if 'image/' not in response.headers.get('Content-Type', ''):
            raise ValueError("URL指向的内容不是有效的图片")
            
        image = cv2.imdecode(np.frombuffer(response.content, np.uint8), cv2.IMREAD_COLOR)
        
        # 验证图像
        if image is None or image.size == 0:
            raise ValueError("无法加载图像（可能是无效的图片格式或URL）")
        
        # 转换为RGB格式
        if len(image.shape) == 3 and image.shape[2] == 3:
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        
        # 模型检测
        results = model.predict(image)
        annotated_image = results[0].plot()  # 生成带标注的图像
        
        # 保存结果图像
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        unique_id = str(uuid.uuid4())[:8]
        filename = f"{timestamp}_{unique_id}.jpg"
        filepath = os.path.join(OUTPUT_DIR, filename)
        cv2.imwrite(filepath, cv2.cvtColor(annotated_image, cv2.COLOR_RGB2BGR))
        
        # 生成完整可访问的结果URL
        resimg_url = f"{BASE_FULL_URL}{STATIC_PATH}/{filename}"
        
        # 提取检测结果详情
        detections = []
        detected_classes = set()  # 用于收集检测到的类别
        for box in results[0].boxes:
            class_name = results[0].names[int(box.cls)]
            detected_classes.add(class_name)
            detections.append({
                "class_id": int(box.cls),
                "class_name": class_name,
                "confidence": float(box.conf),
                "bbox": [int(coord) for coord in box.xyxy.tolist()[0]]  # [x1, y1, x2, y2]
            })
        
        return {
            "res": ", ".join(detected_classes) if detected_classes else "无检测结果",  # 检测到的类别字符串
            "resimg_url": resimg_url,
            "detections": detections,
            "message": "检测成功",
            "timestamp": datetime.now().isoformat()
        }
    
    except requests.exceptions.RequestException as e:
        raise HTTPException(
            status_code=400,
            detail=f"图片下载失败: {str(e)}"
        )
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        import traceback
        traceback.print_exc()
        raise HTTPException(
            status_code=500,
            detail=f"处理失败: {str(e)}"
        )

@app.post("/detect/json2base64")
async def detect_objects_json2base64(request: DetectionRequest):
    """
    URL检测端点 - 通过图片URL进行检测，返回base64编码图片
    
    输入:
    - JSON格式请求体:
      {
        "img_url": "图片的HTTP/HTTPS URL地址"
      }
      - Content-Type: application/json
    
    输出:
    - JSON格式响应:
      {
        "res": "检测到的类别名称，多个用逗号分隔",
        "resimg_url": "data:image/jpeg;base64,{base64编码的带标注图片}",
        "detections": [
          {
            "class_id": 类别ID,
            "class_name": "类别名称", 
            "confidence": 置信度分数,
            "bbox": [x1, y1, x2, y2]  // 边界框坐标
          }
        ],
        "message": "检测成功",
        "timestamp": "ISO格式时间戳"
      }
    
    使用示例:
    curl -X POST "http://ip:8001/detect/json2base64" \
         -H "Content-Type: application/json" \
         -d '{"img_url": "https://example.com/image.jpg"}'
    
    返回的resimg_url可以直接用于markdown显示: ![]{{resimg_url}}
    """
    try:
        # 处理URL
        response = requests.get(request.img_url, timeout=10)
        response.raise_for_status()
        
        if 'image/' not in response.headers.get('Content-Type', ''):
            raise ValueError("URL指向的内容不是有效的图片")
            
        image = cv2.imdecode(np.frombuffer(response.content, np.uint8), cv2.IMREAD_COLOR)
        
        # 验证图像
        if image is None or image.size == 0:
            raise ValueError("无法加载图像（可能是无效的图片格式或URL）")
        
        # 转换为RGB格式
        if len(image.shape) == 3 and image.shape[2] == 3:
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        
        # 模型检测
        results = model.predict(image)
        annotated_image = results[0].plot()  # 生成带标注的图像
        
        # 将标注图像转换为base64编码
        annotated_bgr = cv2.cvtColor(annotated_image, cv2.COLOR_RGB2BGR)
        _, buffer = cv2.imencode('.jpg', annotated_bgr)
        img_base64 = base64.b64encode(buffer).decode('utf-8')
        
        # 生成data URL格式的base64图片URL
        resimg_url = f"data:image/jpeg;base64,{img_base64}"
        
        # 提取检测结果详情
        detections = []
        detected_classes = set()  # 用于收集检测到的类别
        for box in results[0].boxes:
            class_name = results[0].names[int(box.cls)]
            detected_classes.add(class_name)
            detections.append({
                "class_id": int(box.cls),
                "class_name": class_name,
                "confidence": float(box.conf),
                "bbox": [int(coord) for coord in box.xyxy.tolist()[0]]  # [x1, y1, x2, y2]
            })
        
        return {
            "res": ", ".join(detected_classes) if detected_classes else "无检测结果",  # 检测到的类别字符串
            "resimg_url": resimg_url,
            "detections": detections,
            "message": "检测成功",
            "timestamp": datetime.now().isoformat()
        }
    
    except requests.exceptions.RequestException as e:
        raise HTTPException(
            status_code=400,
            detail=f"图片下载失败: {str(e)}"
        )
    except ValueError as e:
        raise HTTPException(status_code=422, detail=str(e))
    except Exception as e:
        import traceback
        traceback.print_exc()
        raise HTTPException(
            status_code=500,
            detail=f"处理失败: {str(e)}"
        )

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8001)