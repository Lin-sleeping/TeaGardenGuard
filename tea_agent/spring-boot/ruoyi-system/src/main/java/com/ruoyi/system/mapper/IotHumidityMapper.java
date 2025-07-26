package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.IotHumidity;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import java.util.Map;

@Repository
public interface IotHumidityMapper {
    int insertIotHumidity(IotHumidity iotHumidity);

    List<IotHumidity> selectIotHumidityListByTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    IotHumidity selectLatestIotHumidity();

    List<Map<String, Object>> selectIotHumidityListMapByTime(@Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    Map<String, Object> selectLatestIotHumidityMap();
}