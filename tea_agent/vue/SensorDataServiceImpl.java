package com.wumei.smart.service.impl;

import com.wumei.smart.domain.SensorData;
import com.wumei.smart.domain.dto.SensorDataDTO;
import com.wumei.smart.mapper.SensorDataMapper;
import com.wumei.smart.service.ISensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 传感器数据Service业务层处理
 * 
 * @author wumei
 * @date 2024-01-15
 */
@Service
public class SensorDataServiceImpl implements ISensorDataService {
    @Autowired
    private SensorDataMapper sensorDataMapper;

    /**
     * 查询传感器数据
     * 
     * @param id 传感器数据主键
     * @return 传感器数据
     */
    @Override
    public SensorData selectSensorDataById(Long id) {
        return sensorDataMapper.selectSensorDataById(id);
    }

    /**
     * 查询传感器数据列表
     * 
     * @param sensorData 传感器数据
     * @return 传感器数据
     */
    @Override
    public List<SensorData> selectSensorDataList(SensorData sensorData) {
        return sensorDataMapper.selectSensorDataList(sensorData);
    }

    /**
     * 新增传感器数据
     * 
     * @param sensorData 传感器数据
     * @return 结果
     */
    @Override
    public int insertSensorData(SensorData sensorData) {
        return sensorDataMapper.insertSensorData(sensorData);
    }

    /**
     * 修改传感器数据
     * 
     * @param sensorData 传感器数据
     * @return 结果
     */
    @Override
    public int updateSensorData(SensorData sensorData) {
        return sensorDataMapper.updateSensorData(sensorData);
    }

    /**
     * 批量删除传感器数据
     * 
     * @param ids 需要删除的传感器数据主键
     * @return 结果
     */
    @Override
    public int deleteSensorDataByIds(Long[] ids) {
        return sensorDataMapper.deleteSensorDataByIds(ids);
    }

    /**
     * 删除传感器数据信息
     * 
     * @param id 传感器数据主键
     * @return 结果
     */
    @Override
    public int deleteSensorDataById(Long id) {
        return sensorDataMapper.deleteSensorDataById(id);
    }

    /**
     * 获取实时传感器数据
     * 
     * @return 传感器数据DTO
     */
    @Override
    public SensorDataDTO getCurrentSensorData() {
        SensorDataDTO dto = new SensorDataDTO();

        // 获取所有设备的最新数据
        List<SensorData> latestDataList = sensorDataMapper.selectAllLatestSensorData();

        if (latestDataList != null && !latestDataList.isEmpty()) {
            // 合并所有设备的数据
            BigDecimal totalTemp = BigDecimal.ZERO;
            BigDecimal totalHumidity = BigDecimal.ZERO;
            Integer totalCo2 = 0;
            int tempCount = 0;
            int humidityCount = 0;
            int co2Count = 0;
            Date latestTime = null;

            for (SensorData data : latestDataList) {
                if (data.getTemperature() != null) {
                    totalTemp = totalTemp.add(data.getTemperature());
                    tempCount++;
                }
                if (data.getHumidity() != null) {
                    totalHumidity = totalHumidity.add(data.getHumidity());
                    humidityCount++;
                }
                if (data.getCo2() != null) {
                    totalCo2 += data.getCo2();
                    co2Count++;
                }
                if (latestTime == null || data.getCreateTime().after(latestTime)) {
                    latestTime = data.getCreateTime();
                }
            }

            // 计算平均值
            if (tempCount > 0) {
                dto.setTemperature(totalTemp.divide(BigDecimal.valueOf(tempCount), 2, BigDecimal.ROUND_HALF_UP));
            }
            if (humidityCount > 0) {
                dto.setHumidity(totalHumidity.divide(BigDecimal.valueOf(humidityCount), 2, BigDecimal.ROUND_HALF_UP));
            }
            if (co2Count > 0) {
                dto.setCo2(totalCo2 / co2Count);
            }

            dto.setUpdateTime(latestTime);
            dto.setDeviceStatus("online"); // 假设有数据就是在线状态

            // 计算趋势（这里简化处理，实际可以根据历史数据计算）
            dto.setTemperatureTrend("stable");
            dto.setHumidityTrend("stable");
            dto.setCo2Trend("stable");
        }

        return dto;
    }

    /**
     * 获取传感器历史数据
     * 
     * @param hours 小时数
     * @return 传感器数据DTO
     */
    @Override
    public SensorDataDTO getSensorHistoryData(Integer hours) {
        if (hours == null) {
            hours = 24; // 默认24小时
        }

        SensorDataDTO dto = new SensorDataDTO();

        // 获取历史数据
        List<SensorData> historyData = sensorDataMapper.selectAllHistoryData(hours);

        if (historyData != null && !historyData.isEmpty()) {
            List<String> timeLabels = new ArrayList<>();
            List<BigDecimal> temperatureData = new ArrayList<>();
            List<BigDecimal> humidityData = new ArrayList<>();
            List<Integer> co2Data = new ArrayList<>();

            for (SensorData data : historyData) {
                // 格式化时间标签
                String timeLabel = String.format("%02d:%02d",
                        data.getCreateTime().getHours(),
                        data.getCreateTime().getMinutes());
                timeLabels.add(timeLabel);

                // 添加数据
                temperatureData.add(data.getTemperature());
                humidityData.add(data.getHumidity());
                co2Data.add(data.getCo2());
            }

            dto.setTimeLabels(timeLabels);
            dto.setTemperatureData(temperatureData);
            dto.setHumidityData(humidityData);
            dto.setCo2Data(co2Data);
        }

        return dto;
    }

    /**
     * 获取传感器统计数据
     * 
     * @return 传感器数据DTO
     */
    @Override
    public SensorDataDTO getSensorStatsData() {
        // 这里可以根据需要实现统计逻辑
        // 比如获取最近24小时的统计数据
        Date endTime = new Date();
        Date startTime = new Date(endTime.getTime() - 24 * 60 * 60 * 1000);

        // 这里需要根据实际需求实现统计逻辑
        // 暂时返回空对象
        return new SensorDataDTO();
    }
}