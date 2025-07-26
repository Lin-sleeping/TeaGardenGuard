# 传感器数据API接口文档

## 1. 获取实时传感器数据

### 接口地址
```
GET /sensor/current
```

### 请求参数
无

### 响应格式
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "temperature": 25.6,
    "humidity": 65.2,
    "co2": 450,
    "deviceStatus": "online",
    "temperatureTrend": "up",
    "humidityTrend": "down",
    "co2Trend": "stable",
    "updateTime": "2024-01-15 14:30:25"
  }
}
```

### 字段说明
- `temperature`: 温度值，单位：°C，类型：number
- `humidity`: 湿度值，单位：%，类型：number
- `co2`: CO₂浓度，单位：ppm，类型：number
- `deviceStatus`: 设备状态，可选值：online/offline/error，类型：string
- `temperatureTrend`: 温度趋势，可选值：up/down/stable，类型：string
- `humidityTrend`: 湿度趋势，可选值：up/down/stable，类型：string
- `co2Trend`: CO₂趋势，可选值：up/down/stable，类型：string
- `updateTime`: 数据更新时间，格式：YYYY-MM-DD HH:mm:ss，类型：string

## 2. 获取传感器历史数据

### 接口地址
```
GET /sensor/history
```

### 请求参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| hours | number | 否 | 获取最近多少小时的数据，默认24 |
| type | string | 否 | 传感器类型，可选值：temperature/humidity/co2/all，默认all |

### 响应格式（格式一：结构化数据）
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "timeLabels": ["00:00", "01:00", "02:00", "..."],
    "temperatureData": [25.1, 25.3, 25.0, "..."],
    "humidityData": [65, 66, 64, "..."],
    "co2Data": [450, 455, 448, "..."]
  }
}
```

### 响应格式（格式二：数组数据）
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "time": "2024-01-15 00:00:00",
      "temperature": 25.1,
      "humidity": 65,
      "co2": 450
    },
    {
      "time": "2024-01-15 01:00:00",
      "temperature": 25.3,
      "humidity": 66,
      "co2": 455
    }
  ]
}
```

## 3. 获取传感器统计数据

### 接口地址
```
GET /sensor/stats
```

### 请求参数
无

### 响应格式
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "temperature": {
      "current": 25.6,
      "max": 28.5,
      "min": 22.1,
      "avg": 24.8
    },
    "humidity": {
      "current": 65.2,
      "max": 78.5,
      "min": 45.2,
      "avg": 62.3
    },
    "co2": {
      "current": 450,
      "max": 680,
      "min": 380,
      "avg": 420
    }
  }
}
```

## 4. 获取设备状态

### 接口地址
```
GET /device/status
```

### 请求参数
无

### 响应格式
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "deviceId": "sensor_001",
    "status": "online",
    "lastHeartbeat": "2024-01-15 14:30:25",
    "ipAddress": "192.168.1.100",
    "firmwareVersion": "v1.2.3"
  }
}
```

## 错误响应格式

```json
{
  "code": 500,
  "msg": "服务器内部错误",
  "data": null
}
```

## 状态码说明

- `200`: 操作成功
- `400`: 请求参数错误
- `401`: 未授权
- `403`: 禁止访问
- `404`: 资源不存在
- `500`: 服务器内部错误

## 注意事项

1. 所有时间字段使用 `YYYY-MM-DD HH:mm:ss` 格式
2. 数值字段保留适当的小数位数
3. 趋势字段用于显示数据变化方向
4. 设备状态字段用于显示传感器设备是否正常工作
5. 历史数据建议按时间倒序排列（最新的在前）
6. 如果某个传感器数据暂时不可用，可以返回 `null` 或 `0` 