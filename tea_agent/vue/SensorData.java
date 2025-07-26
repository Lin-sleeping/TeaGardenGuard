package com.wumei.smart.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 传感器数据对象 sensor_data
 * 
 * @author wumei
 * @date 2024-01-15
 */
public class SensorData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 数据ID */
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String deviceId;

    /** 温度值 */
    @Excel(name = "温度值")
    private BigDecimal temperature;

    /** 湿度值 */
    @Excel(name = "湿度值")
    private BigDecimal humidity;

    /** CO2浓度 */
    @Excel(name = "CO2浓度")
    private Integer co2;

    /** 数据创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "数据创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setCo2(Integer co2) {
        this.co2 = co2;
    }

    public Integer getCo2() {
        return co2;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("deviceId", getDeviceId())
                .append("temperature", getTemperature())
                .append("humidity", getHumidity())
                .append("co2", getCo2())
                .append("createTime", getCreateTime())
                .toString();
    }
}