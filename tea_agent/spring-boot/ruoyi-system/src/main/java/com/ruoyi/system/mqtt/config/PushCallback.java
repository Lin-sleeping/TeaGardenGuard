/******************************************************************************
 * 作者：kerwincui
 * 时间：2021-06-08
 * 邮箱：164770707@qq.com
 * 源码地址：https://gitee.com/kerwincui/wumei-smart
 * author: kerwincui
 * create: 2021-06-08
 * email：164770707@qq.com
 * source:https://github.com/kerwincui/wumei-smart
 ******************************************************************************/
package com.ruoyi.system.mqtt.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.service.IIotCategoryService;
import com.ruoyi.system.service.IIotDeviceService;
import com.ruoyi.system.service.IIotDeviceSetService;
import com.ruoyi.system.service.IIotDeviceStatusService;
import com.ruoyi.system.domain.IotTemperature;
import com.ruoyi.system.service.IIotTemperatureService;
import com.ruoyi.system.domain.IotHumidity;
import com.ruoyi.system.domain.IotLightIntensity;
import com.ruoyi.system.domain.IotSoilMoisture;
import com.ruoyi.system.domain.IotFlameIntensity;
import com.ruoyi.system.service.IIotHumidityService;
import com.ruoyi.system.service.IIotLightIntensityService;
import com.ruoyi.system.service.IIotSoilMoistureService;
import com.ruoyi.system.service.IIotFlameIntensityService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @Classname PushCallback
 * @Description 消费监听类
 */
@Component
public class PushCallback implements MqttCallback {
    private static final Logger logger = LoggerFactory.getLogger(MqttPushClient.class);

    @Autowired
    private MqttConfig mqttConfig;
    @Autowired
    private IIotDeviceService iotDeviceService;
    @Autowired
    private IIotCategoryService iotCategoryService;
    @Autowired
    private IIotDeviceStatusService iotDeviceStatusService;
    @Autowired
    private IIotDeviceSetService iotDeviceSetService;
    @Autowired
    private MqttPushClient mqttPushClient;
    @Autowired
    private IIotTemperatureService iotTemperatureService;
    @Autowired
    private IIotHumidityService iotHumidityService;
    @Autowired
    private IIotLightIntensityService iotLightIntensityService;
    @Autowired
    private IIotSoilMoistureService iotSoilMoistureService;
    @Autowired
    private IIotFlameIntensityService iotFlameIntensityService;

    private static MqttClient client;

    @Override
    public void connectionLost(Throwable throwable) {
        // 连接丢失后，一般在这里面进行重连
        logger.info("连接断开，可以做重连");
        if (client == null || !client.isConnected()) {
            mqttConfig.getMqttPushClient();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        // subscribe后得到的消息会执行到这里面
        logger.info("接收消息主题 : " + topic);
        logger.info("接收消息Qos : " + mqttMessage.getQos());
        logger.info("接收消息内容 : " + new String(mqttMessage.getPayload()));

        if (topic.equals("test/mqtt-dv1/temperature")) {
            String payload = new String(mqttMessage.getPayload(), java.nio.charset.StandardCharsets.UTF_8).trim();
            java.math.BigDecimal value = new java.math.BigDecimal(payload);
            IotTemperature temp = new IotTemperature();
            temp.setValue(value);
            temp.setDataTime(new java.util.Date());
            iotTemperatureService.insertIotTemperature(temp);
            return;
        } else if (topic.equals("test/mqtt-dv1/humidity")) {
            String payload = new String(mqttMessage.getPayload(), java.nio.charset.StandardCharsets.UTF_8).trim();
            java.math.BigDecimal value = new java.math.BigDecimal(payload);
            IotHumidity humidity = new IotHumidity();
            humidity.setValue(value);
            humidity.setDataTime(new java.util.Date());
            iotHumidityService.insertIotHumidity(humidity);
            return;
        } else if (topic.equals("test/mqtt-dv2/lux")) {
            String payload = new String(mqttMessage.getPayload(), java.nio.charset.StandardCharsets.UTF_8).trim();
            java.math.BigDecimal value = new java.math.BigDecimal(payload);
            IotLightIntensity light = new IotLightIntensity();
            light.setValue(value);
            light.setDataTime(new java.util.Date());
            iotLightIntensityService.insertIotLightIntensity(light);
            return;
        } else if (topic.equals("test/mqtt-dv3/soilHumidity")) {
            String payload = new String(mqttMessage.getPayload(), java.nio.charset.StandardCharsets.UTF_8).trim();
            java.math.BigDecimal value = new java.math.BigDecimal(payload);
            IotSoilMoisture soil = new IotSoilMoisture();
            soil.setValue(value);
            soil.setDataTime(new java.util.Date());
            iotSoilMoistureService.insertIotSoilMoisture(soil);

            return;
        } else if (topic.equals("test/mqtt-dv4/flame")) {
            String payload = new String(mqttMessage.getPayload(), java.nio.charset.StandardCharsets.UTF_8).trim();
            Integer value = Integer.parseInt(payload);
            IotFlameIntensity flame = new IotFlameIntensity();
            flame.setValue(value);
            flame.setDataTime(new java.util.Date());
            iotFlameIntensityService.insertIotFlameIntensity(flame);
            return;
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        logger.info("deliveryComplete---------" + iMqttDeliveryToken.isComplete());
    }
}
