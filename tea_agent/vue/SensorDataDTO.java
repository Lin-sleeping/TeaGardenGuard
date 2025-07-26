package com.wumei.smart.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 传感器数据传输对象
 * 
 * @author wumei
 * @date 2024-01-15
 */
public class SensorDataDTO {
    
    /** 温度值 */
    private BigDecimal temperature;
    
    /** 湿度值 */
    private BigDecimal humidity;
    
    /** CO2浓度 */
    private Integer co2;
    
    /** 设备状态 */
    private String deviceStatus;
    
    /** 温度趋势 */
    private String temperatureTrend;
    
    /** 湿度趋势 */
    private String humidityTrend;
    
    /** CO2趋势 */
    private String co2Trend;
    
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    /** 时间标签 */
    private List<String> timeLabels;
    
    /** 温度数据 */
    private List<BigDecimal> temperatureData;
    
    /** 湿度数据 */
    private List<BigDecimal> humidityData;
    
    /** CO2数据 */
    private List<Integer> co2Data;
    
    /** 历史数据列表 */
    private List<SensorHistoryDTO> historyData;

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    public Integer getCo2() {
        return co2;
    }

    public void setCo2(Integer co2) {
        this.co2 = co2;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getTemperatureTrend() {
        return temperatureTrend;
    }

    public void setTemperatureTrend(String temperatureTrend) {
        this.temperatureTrend = temperatureTrend;
    }

    public String getHumidityTrend() {
        return humidityTrend;
    }

    public void setHumidityTrend(String humidityTrend) {
        this.humidityTrend = humidityTrend;
    }

    public String getCo2Trend() {
        return co2Trend;
    }

    public void setCo2Trend(String co2Trend) {
        this.co2Trend = co2Trend;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getTimeLabels() {
        return timeLabels;
    }

    public void setTimeLabels(List<String> timeLabels) {
        this.timeLabels = timeLabels;
    }

    public List<BigDecimal> getTemperatureData() {
        return temperatureData;
    }

    public void setTemperatureData(List<BigDecimal> temperatureData) {
        this.temperatureData = temperatureData;
    }

    public List<BigDecimal> getHumidityData() {
        return humidityData;
    }

    public void setHumidityData(List<BigDecimal> humidityData) {
        this.humidityData = humidityData;
    }

    public List<Integer> getCo2Data() {
        return co2Data;
    }

    public void setCo2Data(List<Integer> co2Data) {
        this.co2Data = co2Data;
    }

    public List<SensorHistoryDTO> getHistoryData() {
        return historyData;
    }

    public void setHistoryData(List<SensorHistoryDTO> historyData) {
        this.historyData = historyData;
    }
}

/**
 * 传感器历史数据DTO
 */
class SensorHistoryDTO {
    
    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    
    /** 温度值 */
    private BigDecimal temperature;
    
    /** 湿度值 */
    private BigDecimal humidity;
    
    /** CO2浓度 */
    private Integer co2;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    public Integer getCo2() {
        return co2;
    }

    public void setCo2(Integer co2) {
        this.co2 = co2;
    }
} 