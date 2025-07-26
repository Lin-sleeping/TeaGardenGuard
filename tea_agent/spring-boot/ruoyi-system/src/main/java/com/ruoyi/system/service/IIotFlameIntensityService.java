package com.ruoyi.system.service;

import com.ruoyi.system.domain.IotFlameIntensity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IIotFlameIntensityService {
    int insertIotFlameIntensity(IotFlameIntensity iotFlameIntensity);

    List<IotFlameIntensity> selectIotFlameIntensityListByTime(Date startTime, Date endTime);

    IotFlameIntensity selectLatestIotFlameIntensity();

    List<Map<String, Object>> selectIotFlameIntensityListMapByTime(Date startTime, Date endTime);

    Map<String, Object> selectLatestIotFlameIntensityMap();
}