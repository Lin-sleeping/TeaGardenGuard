package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.IotTemperature;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import java.util.Map;

@Repository
public interface IotTemperatureMapper {
    int insertIotTemperature(IotTemperature iotTemperature);

    List<IotTemperature> selectIotTemperatureListByTime(@Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    IotTemperature selectLatestIotTemperature();

    List<Map<String, Object>> selectIotTemperatureListMapByTime(@Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    Map<String, Object> selectLatestIotTemperatureMap();
}