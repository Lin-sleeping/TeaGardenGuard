package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.IotFlameIntensity;
import com.ruoyi.system.mapper.IotFlameIntensityMapper;
import com.ruoyi.system.service.IIotFlameIntensityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IotFlameIntensityServiceImpl implements IIotFlameIntensityService {
    @Autowired
    private IotFlameIntensityMapper iotFlameIntensityMapper;

    @Override
    public int insertIotFlameIntensity(IotFlameIntensity iotFlameIntensity) {
        return iotFlameIntensityMapper.insertIotFlameIntensity(iotFlameIntensity);
    }

    @Override
    public List<IotFlameIntensity> selectIotFlameIntensityListByTime(Date startTime, Date endTime) {
        return iotFlameIntensityMapper.selectIotFlameIntensityListByTime(startTime, endTime);
    }

    @Override
    public IotFlameIntensity selectLatestIotFlameIntensity() {
        return iotFlameIntensityMapper.selectLatestIotFlameIntensity();
    }

    @Override
    public List<Map<String, Object>> selectIotFlameIntensityListMapByTime(Date startTime, Date endTime) {
        return iotFlameIntensityMapper.selectIotFlameIntensityListMapByTime(startTime, endTime);
    }

    @Override
    public Map<String, Object> selectLatestIotFlameIntensityMap() {
        return iotFlameIntensityMapper.selectLatestIotFlameIntensityMap();
    }
}