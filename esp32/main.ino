

#include <WiFi.h>
#include <PubSubClient.h>
#include "DHT.h"

#define MQTT_MAX_PACKET_SIZE 256 // 增加MQTT数据包缓冲区大小

// WiFi 配置
const char* ssid     = "";
const char* password = "";

// MQTT 服务器配置
const char* MQTT_SERVER  = "";
const int   MQTT_PORT    = ;
const char* MQTT_USRNAME = ""; // 用户名
const char* MQTT_PASSWD  = ""; // 密码

// MQTT 主题配置 - 严格按照要求设置
const char* PUB_TEMP_TOPIC = "test/mqtt-dv1/temperature";    // 温度数据主题
const char* PUB_HUMI_TOPIC = "test/mqtt-dv1/humidity";       // 湿度数据主题
const char* PUB_LUX_TOPIC = "test/mqtt-dv2/lux";            // 光照强度主题
const char* PUB_SOIL_TOPIC = "test/mqtt-dv3/soilHumidity";  // 土壤湿度主题
const char* PUB_FLAME_TOPIC = "test/mqtt-dv4/flame";        // 火焰强度主题

// 控制设备订阅主题
const char* SUB_FAN_TOPIC    = "fan";                       // 风扇控制主题
const char* SUB_BUZZER_TOPIC = "buzzer";                    // 蜂鸣器控制主题
const char* SUB_PUMP_TOPIC   = "pump";                      // 水泵控制主题
const char* SUB_LED_TOPIC    = "led";                       // LED控制主题

// 设备标识
const char* CLIENT_ID    = "";  // MQTT客户端ID，必须唯一

// 传感器引脚配置
#define DHTPIN 4           // DHT11 温湿度传感器数据引脚
#define DHTTYPE DHT11      // 传感器类型为 DHT11
#define FLAME_PIN 12       // 火焰传感器模拟输入引脚
#define SOIL_PIN 13        // 土壤湿度传感器模拟输入引脚
#define LDR_PIN 6          // 光敏电阻传感器模拟输入引脚

// 控制设备引脚配置
#define PUMP_IA1 2         // 水泵控制信号1
#define PUMP_IA2 3         // 水泵控制信号2
#define FAN_IB1 15         // 风扇控制信号1（PWM调速引脚）
#define FAN_IB2 16         // 风扇控制信号2
#define BUZZER_PIN 5       // 蜂鸣器引脚
#define LED_PIN 21         // LED控制引脚

// 传感器阈值和校准值
#define FLAME_THRESHOLD 1000   // 火焰检测阈值
#define SOIL_DRY_VALUE 2800    // 土壤干燥基准值
#define SOIL_WET_VALUE 1200    // 土壤湿润基准值

// 风扇转速档位（PWM占空比，0~255）
#define FAN_SPEED_MEDIUM 180   // 中速档位

// 蜂鸣器音符频率定义（修正版）
#define NOTE_C4  262   // 哆 (Do)
#define NOTE_D4  294   // 来 (Re)
#define NOTE_E4  330   // 咪 (Mi)
#define NOTE_F4  349   // 法 (Fa)
#define NOTE_G4  392   // 嗦 (Sol)
#define NOTE_A4  440   // 啦 (La)
#define NOTE_B4  494   // 西 (Si)
#define NOTE_C5  523   // 高音哆 (Do)
// --------------------------

// 蜂鸣器非阻塞播放状态变量
bool isBuzzerPlaying = false;
int currentNoteIndex = 0;
unsigned long lastNoteTime = 0;

// 火焰报警状态变量
bool isFireAlarmActive = false;
bool isAlarmBuzzing = false;
unsigned long lastAlarmBeepTime = 0;
#define ALARM_BEEP_INTERVAL 200  // 报警蜂鸣器间隔（毫秒）

int lanhua_melody[] = {
  NOTE_C5, NOTE_A4, NOTE_G4, NOTE_A4, NOTE_C5, NOTE_A4, NOTE_G4, 0,
  NOTE_F4, NOTE_G4, NOTE_A4, NOTE_C5, NOTE_A4, NOTE_G4, NOTE_F4, 0,
  
  NOTE_C5, NOTE_A4, NOTE_G4, NOTE_A4, NOTE_C5, NOTE_A4, NOTE_G4, 0,
  NOTE_F4, NOTE_A4, NOTE_G4, NOTE_F4, NOTE_E4, NOTE_D4, NOTE_C4, 0,
  
  NOTE_C4, NOTE_D4, NOTE_F4, NOTE_G4, NOTE_A4, NOTE_C5, 0,
  NOTE_A4, NOTE_G4, NOTE_F4, NOTE_G4, NOTE_A4, NOTE_F4, NOTE_E4, 0,
  
  NOTE_C4, NOTE_D4, NOTE_F4, NOTE_G4, NOTE_A4, NOTE_C5, 0,
  NOTE_A4, NOTE_G4, NOTE_F4, NOTE_E4, NOTE_D4, NOTE_C4, 0
};

int lanhua_noteDurations[] = {
  400, 400, 400, 600, 400, 400, 600, 800,  // 句间间隔800ms
  400, 400, 400, 600, 400, 400, 800, 1000, // 段间间隔1000ms
  
  400, 400, 400, 600, 400, 400, 600, 800,  // 句间间隔800ms
  400, 400, 400, 400, 400, 400, 800, 1000, // 段间间隔1000ms
  
  400, 400, 400, 400, 400, 800, 800,       // 句间间隔800ms
  400, 400, 400, 400, 400, 400, 800, 1000, // 段间间隔1000ms
  
  400, 400, 400, 400, 400, 800, 800,       // 句间间隔800ms
  400, 400, 400, 400, 400, 1000, 1200      // 结尾延长1200ms
};

// 全局对象和变量
WiFiClient espClient;
PubSubClient client(espClient);
DHT dht(DHTPIN, DHTTYPE);
long lastMsgTime = 0;
long lastSensorReadTime = 0;  // 添加传感器读取时间变量
char msg[50];

// 传感器数据变量
float temperature = 0.0;
float humidity = 0.0;
int flameValue = 0;
int soilValue = 0;
int ldrValue = 0;
int soilMoisturePercent = 0;
int lightPercent = 0;

// WiFi连接函数
void setup_wifi() {
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
}

// MQTT消息回调函数
void callback(char* topic, byte* payload, unsigned int length) {
  // 将payload转换为字符串
  String message;
  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }
  
  // 打印接收到的消息
  Serial.print("收到MQTT消息 [");
  Serial.print(topic);
  Serial.print("]: ");
  Serial.println(message);
  
  // 根据主题和消息内容控制相应设备
  if (strcmp(topic, "fan") == 0) {
    if (message == "1") {
      fanOn();
    } else if (message == "0") {
      fanOff();
    }
  }
  else if (strcmp(topic, "buzzer") == 0) {
    if (message == "1") {
      buzzerOn();
    } else if (message == "0") {
      buzzerOff();
    }
  }
  else if (strcmp(topic, "pump") == 0) {
    if (message == "1") {
      pumpOn();
    } else if (message == "0") {
      pumpOff();
      // 如果手动关闭水泵且当前在火警状态，则解除火警
      if (isFireAlarmActive) {
        isFireAlarmActive = false;
        isAlarmBuzzing = false;
        noTone(BUZZER_PIN);
        Serial.println("✅ 火警已手动解除");
      }
    }
  }
  else if (strcmp(topic, "led") == 0) {
    if (message == "1") {
      ledOn();
    } else if (message == "0") {
      ledOff();
    }
  }
  else {
    Serial.println("未知的控制主题");
  }
}// MQTT重连函数
void reconnect() {
  while (!client.connected()) {
    if (client.connect(CLIENT_ID, MQTT_USRNAME, MQTT_PASSWD)) {
      Serial.println("MQTT已连接");
      // 连接成功后，订阅所有控制主题
      client.subscribe(SUB_FAN_TOPIC);
      client.subscribe(SUB_BUZZER_TOPIC);
      client.subscribe(SUB_PUMP_TOPIC);
      client.subscribe(SUB_LED_TOPIC);
      Serial.println("已订阅所有控制主题");
    } else {
      Serial.print("MQTT连接失败, rc=");
      Serial.println(client.state());
      delay(5000);
    }
  }
}

// 初始化设置
void setup() {
  Serial.begin(115200);
  
  // 初始化DHT11传感器
  dht.begin();
  
  // 初始化其他传感器引脚
  pinMode(FLAME_PIN, INPUT);
  pinMode(SOIL_PIN, INPUT);
  pinMode(LDR_PIN, INPUT);
  
  // 初始化控制设备引脚
  pinMode(PUMP_IA1, OUTPUT);
  pinMode(PUMP_IA2, OUTPUT);
  pinMode(FAN_IB1, OUTPUT);
  pinMode(FAN_IB2, OUTPUT);
  pinMode(BUZZER_PIN, OUTPUT);
  pinMode(LED_PIN, OUTPUT);
  
  // 初始状态：所有控制设备关闭
  pumpOff();
  fanOff();
  buzzerOff();
  ledOff();
  
  setup_wifi();
  
  client.setServer(MQTT_SERVER, MQTT_PORT);
  client.setCallback(callback);
}

// 主循环
void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
  
  // 处理蜂鸣器非阻塞播放
  handleBuzzerPlayback();
  
  // 处理火焰报警检测
  handleFireAlarm();

  long now = millis();
  
  // 每隔3秒读取一次传感器数据 (或者程序刚启动时)
  if (now - lastSensorReadTime > 3000 || lastSensorReadTime == 0) {
    lastSensorReadTime = now;
    
    // 读取DHT11温湿度
    humidity = dht.readHumidity();
    temperature = dht.readTemperature();

    // 读取火焰传感器
    flameValue = analogRead(FLAME_PIN);
    
    // 读取土壤湿度传感器
    soilValue = analogRead(SOIL_PIN);
    // 修正土壤湿度计算逻辑
    soilMoisturePercent = map(soilValue, SOIL_DRY_VALUE, SOIL_WET_VALUE, 0, 100);
    soilMoisturePercent = constrain(soilMoisturePercent, 0, 100);

    // 读取光敏传感器
    ldrValue = analogRead(LDR_PIN);
    // 修正光敏传感器计算逻辑，参考原始代码
    lightPercent = map(ldrValue, 4095, 0, 0, 100);  // 数值越小光照越强，所以要反转
    lightPercent = constrain(lightPercent, 0, 100);
    
    // 输出采集的数据
    Serial.print("数据采集 - 温度:");
    Serial.print(temperature);
    Serial.print("°C 湿度:");
    Serial.print(humidity);
    Serial.print("% 火焰:");
    Serial.print(flameValue);
    Serial.print(" 土壤:");
    Serial.print(soilMoisturePercent);
    Serial.print("% 光照:");
    Serial.print(lightPercent);
    Serial.println("%");
  }

  // 每隔60秒(1分钟)发布一次数据
  if (now - lastMsgTime > 60000) {
    lastMsgTime = now;

    // 检查DHT11读数是否有效
    if (isnan(humidity) || isnan(temperature)) {
      Serial.println("DHT传感器读取失败");
    } else {
      // 发布温度数据
      snprintf(msg, 50, "%.2f", temperature);
      client.publish(PUB_TEMP_TOPIC, msg);

      // 发布湿度数据
      snprintf(msg, 50, "%.2f", humidity);
      client.publish(PUB_HUMI_TOPIC, msg);
    }

    // 发布火焰传感器数据
    snprintf(msg, 50, "%d", flameValue);
    client.publish(PUB_FLAME_TOPIC, msg);

    // 发布土壤湿度数据
    snprintf(msg, 50, "%d", soilMoisturePercent);
    client.publish(PUB_SOIL_TOPIC, msg);

    // 发布光照强度数据
    snprintf(msg, 50, "%d", lightPercent);
    client.publish(PUB_LUX_TOPIC, msg);
    
    Serial.println("数据已上报到MQTT服务器");
  }
}

// ==================== 设备控制函数 ====================

// 水泵控制函数
void pumpOn() {
  digitalWrite(PUMP_IA1, HIGH);
  digitalWrite(PUMP_IA2, LOW);
  Serial.println("水泵已开启");
}

void pumpOff() {
  digitalWrite(PUMP_IA1, LOW);
  digitalWrite(PUMP_IA2, LOW);
  Serial.println("水泵已关闭");
}

// 风扇控制函数（中速运行）
void fanOn() {
  analogWrite(FAN_IB1, FAN_SPEED_MEDIUM);
  digitalWrite(FAN_IB2, LOW);
  Serial.println("风扇已开启（中速）");
}

void fanOff() {
  analogWrite(FAN_IB1, 0);    // PWM占空比设为0
  digitalWrite(FAN_IB2, LOW);
  Serial.println("风扇已关闭");
}

// LED控制函数
void ledOn() {
  digitalWrite(LED_PIN, HIGH);
  Serial.println("LED已开启");
}

void ledOff() {
  digitalWrite(LED_PIN, LOW);
  Serial.println("LED已关闭");
}

// 蜂鸣器控制函数
void buzzerOn() {
  if (isFireAlarmActive) {
    Serial.println("火焰报警激活中，停止报警声，开始播放音乐");
    // 火警期间允许播放音乐，但会停止报警声
    isAlarmBuzzing = false;
    noTone(BUZZER_PIN);
  }
  if (isBuzzerPlaying) return; // 如果已在播放，则不重复启动
  
  Serial.println("蜂鸣器开始演奏播放");
  isBuzzerPlaying = true;
  currentNoteIndex = 0;
  lastNoteTime = millis();

  // 立即播放第一个音符
  int noteToPlay = lanhua_melody[currentNoteIndex];
  if (noteToPlay > 0) {
    tone(BUZZER_PIN, noteToPlay);
  } else {
    noTone(BUZZER_PIN);
  }
}

void buzzerOff() {
  if (!isBuzzerPlaying && !isFireAlarmActive) return; // 如果未在播放且无火警，则不执行操作
  
  isBuzzerPlaying = false;
  noTone(BUZZER_PIN);
  
  if (isFireAlarmActive) {
    Serial.println("蜂鸣器已停止，恢复火警报警声");
    // 如果在火警状态，停止音乐后恢复报警声
    isAlarmBuzzing = false;
    lastAlarmBeepTime = millis();
  } else {
    Serial.println("蜂鸣器播放已停止");
  }
}

// 非阻塞蜂鸣器播放处理函数
void handleBuzzerPlayback() {
  if (isFireAlarmActive) {
    // 火焰报警期间停止音乐播放
    if (isBuzzerPlaying) {
      isBuzzerPlaying = false;
      Serial.println("火焰报警：停止音乐播放");
    }
    return;
  }
  
  if (!isBuzzerPlaying) {
    return;
  }

  // 检查当前音符的持续时间是否已到
  if (millis() - lastNoteTime >= lanhua_noteDurations[currentNoteIndex]) {
    // 切换到下一个音符
    currentNoteIndex++;

    // 检查歌曲是否播放完毕
    if (currentNoteIndex >= sizeof(lanhua_melody) / sizeof(lanhua_melody[0])) {
      buzzerOff(); // 歌曲结束，自动停止
      Serial.println("完毕");
      return;
    }

    // 播放新音符
    lastNoteTime = millis();
    int noteToPlay = lanhua_melody[currentNoteIndex];
    
    if (noteToPlay > 0) {
      tone(BUZZER_PIN, noteToPlay); // 播放音符
    } else {
      noTone(BUZZER_PIN); // 如果是休止符，则静音
    }
  }
}

// 火焰报警处理函数
void handleFireAlarm() {
  // 检查火焰传感器读数，只用于触发报警，不用于解除
  bool fireDetected = (flameValue < FLAME_THRESHOLD);
  
  if (fireDetected && !isFireAlarmActive) {
    // 检测到火情，启动报警
    isFireAlarmActive = true;
    isAlarmBuzzing = false;
    lastAlarmBeepTime = millis();
    
    // 立即启动水泵
    pumpOn();
    Serial.println("🔥 火焰报警！检测到火情，启动自动灭火系统");
    Serial.print("火焰传感器读数: ");
    Serial.println(flameValue);
    Serial.println("注意：火警需要手动解除（通过MQTT关闭水泵）");
    
    // 停止当前的音乐播放
    if (isBuzzerPlaying) {
      isBuzzerPlaying = false;
    }
  }
  
  // 处理报警蜂鸣器（只有在火警激活且没有播放音乐时才响）
  if (isFireAlarmActive && !isBuzzerPlaying) {
    unsigned long currentTime = millis();
    
    if (currentTime - lastAlarmBeepTime >= ALARM_BEEP_INTERVAL) {
      lastAlarmBeepTime = currentTime;
      
      if (isAlarmBuzzing) {
        // 当前在响，切换到静音
        noTone(BUZZER_PIN);
        isAlarmBuzzing = false;
      } else {
        // 当前静音，切换到响
        tone(BUZZER_PIN, 2000); // 2000Hz 急促报警音
        isAlarmBuzzing = true;
      }
    }
  }
}