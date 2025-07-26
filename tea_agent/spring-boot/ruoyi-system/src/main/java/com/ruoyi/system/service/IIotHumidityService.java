package com.ruoyi.system.service;

import com.ruoyi.system.domain.IotHumidity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IIotHumidityService {
    int insertIotHumidity(IotHumidity iotHumidity);

    List<IotHumidity> selectIotHumidityListByTime(Date startTime, Date endTime);

    IotHumidity selectLatestIotHumidity();

    List<Map<String, Object>> selectIotHumidityListMapByTime(Date startTime, Date endTime);

    Map<String, Object> selectLatestIotHumidityMap();
}