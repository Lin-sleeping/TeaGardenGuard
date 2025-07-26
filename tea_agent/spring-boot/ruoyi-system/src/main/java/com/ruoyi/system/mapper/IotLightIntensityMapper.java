package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.IotLightIntensity;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

@Repository
public interface IotLightIntensityMapper {
    int insertIotLightIntensity(IotLightIntensity iotLightIntensity);

    List<IotLightIntensity> selectIotLightIntensityListByTime(@Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    IotLightIntensity selectLatestIotLightIntensity();

    List<Map<String, Object>> selectIotLightIntensityListMapByTime(@Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    Map<String, Object> selectLatestIotLightIntensityMap();
}