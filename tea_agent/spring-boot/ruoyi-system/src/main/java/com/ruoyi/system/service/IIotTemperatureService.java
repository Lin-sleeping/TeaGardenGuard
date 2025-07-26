package com.ruoyi.system.service;

import com.ruoyi.system.domain.IotTemperature;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IIotTemperatureService {
    int insertIotTemperature(IotTemperature iotTemperature);

    List<IotTemperature> selectIotTemperatureListByTime(Date startTime, Date endTime);

    IotTemperature selectLatestIotTemperature();

    List<Map<String, Object>> selectIotTemperatureListMapByTime(Date startTime, Date endTime);

    Map<String, Object> selectLatestIotTemperatureMap();
}