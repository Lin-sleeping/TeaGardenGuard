package com.ruoyi.system.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 温度传感器数据表实体
 */
public class IotTemperature implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id; // 主键ID
    private BigDecimal value; // 温度(℃)
    private Date dataTime; // 数据时间戳

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }
}