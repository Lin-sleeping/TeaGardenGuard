package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.IotSoilMoisture;
import com.ruoyi.system.mapper.IotSoilMoistureMapper;
import com.ruoyi.system.service.IIotSoilMoistureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IotSoilMoistureServiceImpl implements IIotSoilMoistureService {
    @Autowired
    private IotSoilMoistureMapper iotSoilMoistureMapper;

    @Override
    public int insertIotSoilMoisture(IotSoilMoisture iotSoilMoisture) {
        return iotSoilMoistureMapper.insertIotSoilMoisture(iotSoilMoisture);
    }

    @Override
    public List<IotSoilMoisture> selectIotSoilMoistureListByTime(Date startTime, Date endTime) {
        return iotSoilMoistureMapper.selectIotSoilMoistureListByTime(startTime, endTime);
    }

    @Override
    public IotSoilMoisture selectLatestIotSoilMoisture() {
        return iotSoilMoistureMapper.selectLatestIotSoilMoisture();
    }

    @Override
    public List<Map<String, Object>> selectIotSoilMoistureListMapByTime(Date startTime, Date endTime) {
        return iotSoilMoistureMapper.selectIotSoilMoistureListMapByTime(startTime, endTime);
    }

    @Override
    public Map<String, Object> selectLatestIotSoilMoistureMap() {
        return iotSoilMoistureMapper.selectLatestIotSoilMoistureMap();
    }
}