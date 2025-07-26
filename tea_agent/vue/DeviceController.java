package com.wumei.smart.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.wumei.smart.domain.SensorDevice;
import com.wumei.smart.service.ISensorDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备状态Controller
 * 
 * @author wumei
 * @date 2024-01-15
 */
@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController {
    @Autowired
    private ISensorDeviceService sensorDeviceService;

    /**
     * 获取设备状态
     */
    @GetMapping("/status")
    public AjaxResult getDeviceStatus() {
        try {
            // 获取所有设备状态
            List<SensorDevice> devices = sensorDeviceService.selectSensorDeviceList(new SensorDevice());

            Map<String, Object> result = new HashMap<>();

            // 统计设备状态
            int onlineCount = 0;
            int offlineCount = 0;
            int errorCount = 0;

            for (SensorDevice device : devices) {
                if ("online".equals(device.getStatus())) {
                    onlineCount++;
                } else if ("offline".equals(device.getStatus())) {
                    offlineCount++;
                } else if ("error".equals(device.getStatus())) {
                    errorCount++;
                }
            }

            result.put("devices", devices);
            result.put("onlineCount", onlineCount);
            result.put("offlineCount", offlineCount);
            result.put("errorCount", errorCount);
            result.put("totalCount", devices.size());

            return AjaxResult.success(result);
        } catch (Exception e) {
            logger.error("获取设备状态失败", e);
            return AjaxResult.error("获取设备状态失败：" + e.getMessage());
        }
    }

    /**
     * 更新设备心跳
     */
    @PostMapping("/heartbeat")
    public AjaxResult updateHeartbeat(@RequestParam String deviceId) {
        try {
            // 更新设备心跳时间
            SensorDevice device = new SensorDevice();
            device.setDeviceId(deviceId);
            device.setStatus("online");
            // 这里需要设置lastHeartbeat为当前时间

            int result = sensorDeviceService.updateSensorDevice(device);
            return AjaxResult.success("心跳更新成功");
        } catch (Exception e) {
            logger.error("更新设备心跳失败", e);
            return AjaxResult.error("更新心跳失败：" + e.getMessage());
        }
    }
}