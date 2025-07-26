-- 传感器数据库表结构
-- 创建数据库
CREATE DATABASE IF NOT EXISTS wumei_smart DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE wumei_smart;

-- 传感器设备表
CREATE TABLE sensor_device (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '设备ID',
    device_id VARCHAR(50) NOT NULL UNIQUE COMMENT '设备唯一标识',
    device_name VARCHAR(100) NOT NULL COMMENT '设备名称',
    device_type VARCHAR(50) NOT NULL COMMENT '设备类型：temperature/humidity/co2/combined',
    ip_address VARCHAR(50) COMMENT '设备IP地址',
    firmware_version VARCHAR(20) COMMENT '固件版本',
    status ENUM('online', 'offline', 'error') DEFAULT 'offline' COMMENT '设备状态',
    last_heartbeat DATETIME COMMENT '最后心跳时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_device_id (device_id),
    INDEX idx_status (status),
    INDEX idx_last_heartbeat (last_heartbeat)
) COMMENT '传感器设备表';

-- 传感器数据表
CREATE TABLE sensor_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '数据ID',
    device_id VARCHAR(50) NOT NULL COMMENT '设备ID',
    temperature DECIMAL(5,2) COMMENT '温度值(°C)',
    humidity DECIMAL(5,2) COMMENT '湿度值(%)',
    co2 INT COMMENT 'CO2浓度(ppm)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
    INDEX idx_device_id (device_id),
    INDEX idx_create_time (create_time),
    INDEX idx_temperature (temperature),
    INDEX idx_humidity (humidity),
    INDEX idx_co2 (co2),
    FOREIGN KEY (device_id) REFERENCES sensor_device(device_id) ON DELETE CASCADE
) COMMENT '传感器数据表';

-- 传感器统计数据表（用于缓存统计数据）
CREATE TABLE sensor_stats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '统计ID',
    device_id VARCHAR(50) NOT NULL COMMENT '设备ID',
    stat_date DATE NOT NULL COMMENT '统计日期',
    stat_type ENUM('hourly', 'daily', 'monthly') NOT NULL COMMENT '统计类型',
    temperature_max DECIMAL(5,2) COMMENT '温度最大值',
    temperature_min DECIMAL(5,2) COMMENT '温度最小值',
    temperature_avg DECIMAL(5,2) COMMENT '温度平均值',
    humidity_max DECIMAL(5,2) COMMENT '湿度最大值',
    humidity_min DECIMAL(5,2) COMMENT '湿度最小值',
    humidity_avg DECIMAL(5,2) COMMENT '湿度平均值',
    co2_max INT COMMENT 'CO2最大值',
    co2_min INT COMMENT 'CO2最小值',
    co2_avg DECIMAL(8,2) COMMENT 'CO2平均值',
    data_count INT DEFAULT 0 COMMENT '数据点数量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_device_date_type (device_id, stat_date, stat_type),
    INDEX idx_device_id (device_id),
    INDEX idx_stat_date (stat_date),
    INDEX idx_stat_type (stat_type),
    FOREIGN KEY (device_id) REFERENCES sensor_device(device_id) ON DELETE CASCADE
) COMMENT '传感器统计数据表';

-- 插入示例设备数据
INSERT INTO sensor_device (device_id, device_name, device_type, ip_address, firmware_version, status) VALUES
('sensor_001', '客厅温湿度传感器', 'combined', '192.168.1.100', 'v1.2.3', 'online'),
('sensor_002', '卧室CO2传感器', 'co2', '192.168.1.101', 'v1.1.0', 'online'),
('sensor_003', '厨房温湿度传感器', 'combined', '192.168.1.102', 'v1.2.1', 'offline');

-- 插入示例传感器数据（最近24小时的数据）
INSERT INTO sensor_data (device_id, temperature, humidity, co2, create_time) VALUES
-- 传感器001的数据
('sensor_001', 25.6, 65.2, 450, DATE_SUB(NOW(), INTERVAL 23 HOUR)),
('sensor_001', 25.8, 64.8, 455, DATE_SUB(NOW(), INTERVAL 22 HOUR)),
('sensor_001', 26.1, 65.5, 460, DATE_SUB(NOW(), INTERVAL 21 HOUR)),
('sensor_001', 25.9, 66.2, 458, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
('sensor_001', 25.7, 65.8, 452, DATE_SUB(NOW(), INTERVAL 19 HOUR)),
('sensor_001', 25.4, 65.1, 448, DATE_SUB(NOW(), INTERVAL 18 HOUR)),
('sensor_001', 25.2, 64.9, 445, DATE_SUB(NOW(), INTERVAL 17 HOUR)),
('sensor_001', 25.5, 65.3, 450, DATE_SUB(NOW(), INTERVAL 16 HOUR)),
('sensor_001', 25.8, 65.7, 455, DATE_SUB(NOW(), INTERVAL 15 HOUR)),
('sensor_001', 26.0, 66.0, 460, DATE_SUB(NOW(), INTERVAL 14 HOUR)),
('sensor_001', 26.2, 66.3, 465, DATE_SUB(NOW(), INTERVAL 13 HOUR)),
('sensor_001', 26.1, 66.1, 462, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
('sensor_001', 25.9, 65.9, 458, DATE_SUB(NOW(), INTERVAL 11 HOUR)),
('sensor_001', 25.7, 65.6, 455, DATE_SUB(NOW(), INTERVAL 10 HOUR)),
('sensor_001', 25.5, 65.4, 452, DATE_SUB(NOW(), INTERVAL 9 HOUR)),
('sensor_001', 25.3, 65.2, 450, DATE_SUB(NOW(), INTERVAL 8 HOUR)),
('sensor_001', 25.1, 65.0, 448, DATE_SUB(NOW(), INTERVAL 7 HOUR)),
('sensor_001', 24.9, 64.8, 445, DATE_SUB(NOW(), INTERVAL 6 HOUR)),
('sensor_001', 24.7, 64.6, 442, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
('sensor_001', 24.5, 64.4, 440, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
('sensor_001', 24.3, 64.2, 438, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('sensor_001', 24.1, 64.0, 435, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('sensor_001', 23.9, 63.8, 432, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
('sensor_001', 23.7, 63.6, 430, NOW()),

-- 传感器002的数据（只有CO2）
('sensor_002', NULL, NULL, 480, DATE_SUB(NOW(), INTERVAL 23 HOUR)),
('sensor_002', NULL, NULL, 485, DATE_SUB(NOW(), INTERVAL 22 HOUR)),
('sensor_002', NULL, NULL, 490, DATE_SUB(NOW(), INTERVAL 21 HOUR)),
('sensor_002', NULL, NULL, 488, DATE_SUB(NOW(), INTERVAL 20 HOUR)),
('sensor_002', NULL, NULL, 485, DATE_SUB(NOW(), INTERVAL 19 HOUR)),
('sensor_002', NULL, NULL, 482, DATE_SUB(NOW(), INTERVAL 18 HOUR)),
('sensor_002', NULL, NULL, 480, DATE_SUB(NOW(), INTERVAL 17 HOUR)),
('sensor_002', NULL, NULL, 478, DATE_SUB(NOW(), INTERVAL 16 HOUR)),
('sensor_002', NULL, NULL, 475, DATE_SUB(NOW(), INTERVAL 15 HOUR)),
('sensor_002', NULL, NULL, 472, DATE_SUB(NOW(), INTERVAL 14 HOUR)),
('sensor_002', NULL, NULL, 470, DATE_SUB(NOW(), INTERVAL 13 HOUR)),
('sensor_002', NULL, NULL, 468, DATE_SUB(NOW(), INTERVAL 12 HOUR)),
('sensor_002', NULL, NULL, 465, DATE_SUB(NOW(), INTERVAL 11 HOUR)),
('sensor_002', NULL, NULL, 462, DATE_SUB(NOW(), INTERVAL 10 HOUR)),
('sensor_002', NULL, NULL, 460, DATE_SUB(NOW(), INTERVAL 9 HOUR)),
('sensor_002', NULL, NULL, 458, DATE_SUB(NOW(), INTERVAL 8 HOUR)),
('sensor_002', NULL, NULL, 455, DATE_SUB(NOW(), INTERVAL 7 HOUR)),
('sensor_002', NULL, NULL, 452, DATE_SUB(NOW(), INTERVAL 6 HOUR)),
('sensor_002', NULL, NULL, 450, DATE_SUB(NOW(), INTERVAL 5 HOUR)),
('sensor_002', NULL, NULL, 448, DATE_SUB(NOW(), INTERVAL 4 HOUR)),
('sensor_002', NULL, NULL, 445, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
('sensor_002', NULL, NULL, 442, DATE_SUB(NOW(), INTERVAL 2 HOUR)),
('sensor_002', NULL, NULL, 440, DATE_SUB(NOW(), INTERVAL 1 HOUR)),
('sensor_002', NULL, NULL, 438, NOW());

-- 创建视图：获取最新传感器数据
CREATE VIEW v_latest_sensor_data AS
SELECT 
    sd.device_id,
    sd.device_name,
    sd.device_type,
    sd.status as device_status,
    sd.last_heartbeat,
    s.temperature,
    s.humidity,
    s.co2,
    s.create_time as data_time
FROM sensor_device sd
LEFT JOIN (
    SELECT 
        device_id,
        temperature,
        humidity,
        co2,
        create_time,
        ROW_NUMBER() OVER (PARTITION BY device_id ORDER BY create_time DESC) as rn
    FROM sensor_data
) s ON sd.device_id = s.device_id AND s.rn = 1;

-- 创建存储过程：更新设备状态
DELIMITER //
CREATE PROCEDURE UpdateDeviceStatus()
BEGIN
    UPDATE sensor_device 
    SET status = 'offline' 
    WHERE last_heartbeat < DATE_SUB(NOW(), INTERVAL 5 MINUTE);
END //
DELIMITER ;

-- 创建事件：定期更新设备状态
CREATE EVENT IF NOT EXISTS update_device_status_event
ON SCHEDULE EVERY 1 MINUTE
DO CALL UpdateDeviceStatus(); 