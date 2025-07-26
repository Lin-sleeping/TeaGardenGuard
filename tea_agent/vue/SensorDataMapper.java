package com.wumei.smart.mapper;

import com.wumei.smart.domain.SensorData;
import com.wumei.smart.domain.dto.SensorDataDTO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 传感器数据Mapper接口
 * 
 * @author wumei
 * @date 2024-01-15
 */
public interface SensorDataMapper {
    /**
     * 查询传感器数据
     * 
     * @param id 传感器数据主键
     * @return 传感器数据
     */
    public SensorData selectSensorDataById(Long id);

    /**
     * 查询传感器数据列表
     * 
     * @param sensorData 传感器数据
     * @return 传感器数据集合
     */
    public List<SensorData> selectSensorDataList(SensorData sensorData);

    /**
     * 新增传感器数据
     * 
     * @param sensorData 传感器数据
     * @return 结果
     */
    public int insertSensorData(SensorData sensorData);

    /**
     * 修改传感器数据
     * 
     * @param sensorData 传感器数据
     * @return 结果
     */
    public int updateSensorData(SensorData sensorData);

    /**
     * 删除传感器数据
     * 
     * @param id 传感器数据主键
     * @return 结果
     */
    public int deleteSensorDataById(Long id);

    /**
     * 批量删除传感器数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSensorDataByIds(Long[] ids);

    /**
     * 获取最新的传感器数据
     * 
     * @param deviceId 设备ID
     * @return 最新数据
     */
    public SensorData selectLatestSensorData(@Param("deviceId") String deviceId);

    /**
     * 获取所有设备的最新数据
     * 
     * @return 最新数据列表
     */
    public List<SensorData> selectAllLatestSensorData();

    /**
     * 获取历史数据
     * 
     * @param deviceId 设备ID
     * @param hours    小时数
     * @return 历史数据列表
     */
    public List<SensorData> selectHistoryData(@Param("deviceId") String deviceId, @Param("hours") Integer hours);

    /**
     * 获取所有设备的历史数据
     * 
     * @param hours 小时数
     * @return 历史数据列表
     */
    public List<SensorData> selectAllHistoryData(@Param("hours") Integer hours);

    /**
     * 获取温度趋势
     * 
     * @param deviceId 设备ID
     * @param hours    小时数
     * @return 趋势数据
     */
    public List<BigDecimal> selectTemperatureTrend(@Param("deviceId") String deviceId, @Param("hours") Integer hours);

    /**
     * 获取湿度趋势
     * 
     * @param deviceId 设备ID
     * @param hours    小时数
     * @return 趋势数据
     */
    public List<BigDecimal> selectHumidityTrend(@Param("deviceId") String deviceId, @Param("hours") Integer hours);

    /**
     * 获取CO2趋势
     * 
     * @param deviceId 设备ID
     * @param hours    小时数
     * @return 趋势数据
     */
    public List<Integer> selectCo2Trend(@Param("deviceId") String deviceId, @Param("hours") Integer hours);

    /**
     * 获取时间标签
     * 
     * @param hours 小时数
     * @return 时间标签列表
     */
    public List<String> selectTimeLabels(@Param("hours") Integer hours);

    /**
     * 获取统计数据
     * 
     * @param deviceId  设备ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 统计数据
     */
    public SensorDataDTO selectStatsData(@Param("deviceId") String deviceId,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);
}