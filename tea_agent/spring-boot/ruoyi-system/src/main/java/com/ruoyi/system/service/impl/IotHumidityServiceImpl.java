package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.IotHumidity;
import com.ruoyi.system.mapper.IotHumidityMapper;
import com.ruoyi.system.service.IIotHumidityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IotHumidityServiceImpl implements IIotHumidityService {
    @Autowired
    private IotHumidityMapper iotHumidityMapper;

    @Override
    public int insertIotHumidity(IotHumidity iotHumidity) {
        return iotHumidityMapper.insertIotHumidity(iotHumidity);
    }

    @Override
    public List<IotHumidity> selectIotHumidityListByTime(Date startTime, Date endTime) {
        return iotHumidityMapper.selectIotHumidityListByTime(startTime, endTime);
    }

    @Override
    public IotHumidity selectLatestIotHumidity() {
        return iotHumidityMapper.selectLatestIotHumidity();
    }

    @Override
    public List<Map<String, Object>> selectIotHumidityListMapByTime(Date startTime, Date endTime) {
        return iotHumidityMapper.selectIotHumidityListMapByTime(startTime, endTime);
    }

    @Override
    public Map<String, Object> selectLatestIotHumidityMap() {
        return iotHumidityMapper.selectLatestIotHumidityMap();
    }
}