package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.IotLightIntensity;
import com.ruoyi.system.mapper.IotLightIntensityMapper;
import com.ruoyi.system.service.IIotLightIntensityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IotLightIntensityServiceImpl implements IIotLightIntensityService {
    @Autowired
    private IotLightIntensityMapper iotLightIntensityMapper;

    @Override
    public int insertIotLightIntensity(IotLightIntensity iotLightIntensity) {
        return iotLightIntensityMapper.insertIotLightIntensity(iotLightIntensity);
    }

    @Override
    public List<IotLightIntensity> selectIotLightIntensityListByTime(Date startTime, Date endTime) {
        return iotLightIntensityMapper.selectIotLightIntensityListByTime(startTime, endTime);
    }

    @Override
    public IotLightIntensity selectLatestIotLightIntensity() {
        return iotLightIntensityMapper.selectLatestIotLightIntensity();
    }

    @Override
    public List<Map<String, Object>> selectIotLightIntensityListMapByTime(Date startTime, Date endTime) {
        return iotLightIntensityMapper.selectIotLightIntensityListMapByTime(startTime, endTime);
    }

    @Override
    public Map<String, Object> selectLatestIotLightIntensityMap() {
        return iotLightIntensityMapper.selectLatestIotLightIntensityMap();
    }
}