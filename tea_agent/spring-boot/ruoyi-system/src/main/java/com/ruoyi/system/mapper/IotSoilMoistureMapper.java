package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.IotSoilMoisture;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import java.util.Map;

@Repository
public interface IotSoilMoistureMapper {
    int insertIotSoilMoisture(IotSoilMoisture iotSoilMoisture);

    List<IotSoilMoisture> selectIotSoilMoistureListByTime(@Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    IotSoilMoisture selectLatestIotSoilMoisture();

    List<Map<String, Object>> selectIotSoilMoistureListMapByTime(@Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    Map<String, Object> selectLatestIotSoilMoistureMap();
}