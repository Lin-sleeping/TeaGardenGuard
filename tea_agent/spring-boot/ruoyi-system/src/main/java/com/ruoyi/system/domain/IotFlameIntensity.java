package com.ruoyi.system.domain;

import java.io.Serializable;
import java.util.Date;

public class IotFlameIntensity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer value;
    private Date dataTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }
}