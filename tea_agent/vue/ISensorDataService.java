package com.wumei.smart.service;

import com.wumei.smart.domain.SensorData;
import com.wumei.smart.domain.dto.SensorDataDTO;

import java.util.List;

/**
 * 传感器数据Service接口
 * 
 * @author wumei
 * @date 2024-01-15
 */
public interface ISensorDataService {
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
     * 批量删除传感器数据
     * 
     * @param ids 需要删除的传感器数据主键集合
     * @return 结果
     */
    public int deleteSensorDataByIds(Long[] ids);

    /**
     * 删除传感器数据信息
     * 
     * @param id 传感器数据主键
     * @return 结果
     */
    public int deleteSensorDataById(Long id);

    /**
     * 获取实时传感器数据
     * 
     * @return 传感器数据DTO
     */
    public SensorDataDTO getCurrentSensorData();

    /**
     * 获取传感器历史数据
     * 
     * @param hours 小时数
     * @return 传感器数据DTO
     */
    public SensorDataDTO getSensorHistoryData(Integer hours);

    /**
     * 获取传感器统计数据
     * 
     * @return 传感器数据DTO
     */
    public SensorDataDTO getSensorStatsData();
}