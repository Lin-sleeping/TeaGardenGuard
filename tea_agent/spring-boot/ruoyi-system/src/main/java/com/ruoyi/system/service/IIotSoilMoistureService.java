package com.ruoyi.system.service;

import com.ruoyi.system.domain.IotSoilMoisture;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IIotSoilMoistureService {
    int insertIotSoilMoisture(IotSoilMoisture iotSoilMoisture);

    List<IotSoilMoisture> selectIotSoilMoistureListByTime(Date startTime, Date endTime);

    IotSoilMoisture selectLatestIotSoilMoisture();

    List<Map<String, Object>> selectIotSoilMoistureListMapByTime(Date startTime, Date endTime);

    Map<String, Object> selectLatestIotSoilMoistureMap();
}