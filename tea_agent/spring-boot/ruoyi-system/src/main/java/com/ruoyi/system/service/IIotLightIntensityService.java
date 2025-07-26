package com.ruoyi.system.service;

import com.ruoyi.system.domain.IotLightIntensity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IIotLightIntensityService {
    int insertIotLightIntensity(IotLightIntensity iotLightIntensity);

    List<IotLightIntensity> selectIotLightIntensityListByTime(Date startTime, Date endTime);

    IotLightIntensity selectLatestIotLightIntensity();

    List<Map<String, Object>> selectIotLightIntensityListMapByTime(Date startTime, Date endTime);

    Map<String, Object> selectLatestIotLightIntensityMap();
}