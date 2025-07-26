package com.ruoyi.system.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.IotTemperature;
import com.ruoyi.system.domain.IotHumidity;
import com.ruoyi.system.domain.IotLightIntensity;
import com.ruoyi.system.domain.IotSoilMoisture;
import com.ruoyi.system.domain.IotFlameIntensity;
import com.ruoyi.system.service.IIotTemperatureService;
import com.ruoyi.system.service.IIotHumidityService;
import com.ruoyi.system.service.IIotLightIntensityService;
import com.ruoyi.system.service.IIotSoilMoistureService;
import com.ruoyi.system.service.IIotFlameIntensityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensor")
public class IotDataHistoryController {
    @Autowired
    private IIotTemperatureService temperatureService;
    @Autowired
    private IIotHumidityService humidityService;
    @Autowired
    private IIotLightIntensityService lightService;
    @Autowired
    private IIotSoilMoistureService soilService;
    @Autowired
    private IIotFlameIntensityService flameService;

    @GetMapping("/history")
    public AjaxResult getHistory(@RequestParam String type, @RequestParam(defaultValue = "24") Integer hours) {

        List<Map<String, Object>> result = new ArrayList<>();
        Date now = new Date();
        Date startTime = new Date(now.getTime() - hours * 60 * 60 * 1000L);
        switch (type) {
            case "temperature": {
                // 直接用自定义SQL返回的Map，包含time字段
                List<Map<String, Object>> list = temperatureService.selectIotTemperatureListMapByTime(startTime, now);
                result = list;
                break;
            }
            case "humidity": {
                List<Map<String, Object>> list = humidityService.selectIotHumidityListMapByTime(startTime, now);
                result = list;
                break;
            }
            case "light": {
                List<Map<String, Object>> list = lightService.selectIotLightIntensityListMapByTime(startTime, now);
                result = list;
                break;
            }
            case "soil": {
                List<Map<String, Object>> list = soilService.selectIotSoilMoistureListMapByTime(startTime, now);
                result = list;
                break;
            }
            case "flame": {
                List<Map<String, Object>> list = flameService.selectIotFlameIntensityListMapByTime(startTime, now);
                result = list;
                break;
            }
            default:
                return AjaxResult.error("不支持的type类型");
        }
        return AjaxResult.success(result);
    }

    @GetMapping("/current")
    public AjaxResult getCurrent() {
        Map<String, Object> result = new HashMap<>();
        // 温度
        Map<String, Object> temp = temperatureService.selectLatestIotTemperatureMap();
        result.put("temperature", temp != null ? temp.get("value") : null);
        result.put("temperatureTime", temp != null ? temp.get("time") : null);
        // 湿度
        Map<String, Object> hum = humidityService.selectLatestIotHumidityMap();
        result.put("humidity", hum != null ? hum.get("value") : null);
        result.put("humidityTime", hum != null ? hum.get("time") : null);
        // 光照
        Map<String, Object> light = lightService.selectLatestIotLightIntensityMap();
        result.put("light", light != null ? light.get("value") : null);
        result.put("lightTime", light != null ? light.get("time") : null);
        // 土壤湿度
        Map<String, Object> soil = soilService.selectLatestIotSoilMoistureMap();
        result.put("soil", soil != null ? soil.get("value") : null);
        result.put("soilTime", soil != null ? soil.get("time") : null);
        // 火焰
        Map<String, Object> flame = flameService.selectLatestIotFlameIntensityMap();
        result.put("flame", flame != null ? flame.get("value") : null);
        result.put("flameTime", flame != null ? flame.get("time") : null);
        return AjaxResult.success(result);
    }
}