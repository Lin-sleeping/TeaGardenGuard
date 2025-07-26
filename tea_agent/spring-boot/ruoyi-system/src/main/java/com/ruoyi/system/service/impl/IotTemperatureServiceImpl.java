package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.IotTemperature;
import com.ruoyi.system.mapper.IotTemperatureMapper;
import com.ruoyi.system.service.IIotTemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IotTemperatureServiceImpl implements IIotTemperatureService {
    @Autowired
    private IotTemperatureMapper iotTemperatureMapper;

    @Override
    public int insertIotTemperature(IotTemperature iotTemperature) {
        return iotTemperatureMapper.insertIotTemperature(iotTemperature);
    }

    @Override
    public List<IotTemperature> selectIotTemperatureListByTime(Date startTime, Date endTime) {
        return iotTemperatureMapper.selectIotTemperatureListByTime(startTime, endTime);
    }

    @Override
    public IotTemperature selectLatestIotTemperature() {
        return iotTemperatureMapper.selectLatestIotTemperature();
    }

    @Override
    public List<Map<String, Object>> selectIotTemperatureListMapByTime(Date startTime, Date endTime) {
        return iotTemperatureMapper.selectIotTemperatureListMapByTime(startTime, endTime);
    }

    @Override
    public Map<String, Object> selectLatestIotTemperatureMap() {
        return iotTemperatureMapper.selectLatestIotTemperatureMap();
    }
}