package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.IotFlameIntensity;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

@Repository
public interface IotFlameIntensityMapper {
    int insertIotFlameIntensity(IotFlameIntensity iotFlameIntensity);

    List<IotFlameIntensity> selectIotFlameIntensityListByTime(@Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    IotFlameIntensity selectLatestIotFlameIntensity();

    List<Map<String, Object>> selectIotFlameIntensityListMapByTime(@Param("startTime") Date startTime,
            @Param("endTime") Date endTime);

    Map<String, Object> selectLatestIotFlameIntensityMap();
}