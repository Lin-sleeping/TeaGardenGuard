<template>
  <div class="app-container home">
    <!-- 传感器数据面板 -->
    <el-card class="sensor-overview">
      <div slot="header" class="clearfix">
        <span>传感器实时数据</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshSensorData">
          <i class="el-icon-refresh"></i> 刷新
        </el-button>
      </div>
      <sensor-panel :sensor-data="sensorData" />
    </el-card>

    <!-- 传感器数据图表 -->
    <sensor-chart ref="sensorChart" />


  </div>
</template>

<script>
import SensorPanel from '@/components/SensorPanel'
import SensorChart from '@/components/SensorChart'
import { getSensorDataWithRetry, getDeviceStatus } from '@/api/sensor'

export default {
  name: "index",
  components: {
    SensorPanel,
    SensorChart
  },
  data() {
    return {
      // 版本号
      version: "3.4.0",
      // 传感器数据
      sensorData: {
        temperature: 0,
        humidity: 0,
        co2: 0,
        light: 0, // 新增光照强度
        soil: 0,  // 新增土壤湿度
        flame: 0, // 新增火焰强度
        deviceStatus: 'offline',
        temperatureTrend: 'stable',
        humidityTrend: 'stable',
        co2Trend: 'stable',
        lightTrend: 'stable', // 新增光照趋势
        soilTrend: 'stable',  // 新增土壤湿度趋势
        flameTrend: 'stable', // 新增火焰趋势
        updateTime: ''
      },
      // 数据刷新定时器
      dataTimer: null
    };
  },
  mounted() {
    this.initSensorData()
    this.startAutoRefresh()
  },
  beforeDestroy() {
    if (this.dataTimer) {
      clearInterval(this.dataTimer)
    }
  },
  methods: {
    // 初始化传感器数据
    async initSensorData() {
      try {
        // 从后端API获取传感器数据（带重试机制）
        const response = await getSensorDataWithRetry()
        if (response.code === 200) {
          this.sensorData = {
            temperature: response.data.temperature || 0,
            humidity: response.data.humidity || 0,
            co2: response.data.co2 || 0,
            light: response.data.light || 0,
            soil: response.data.soil || 0,
            flame: response.data.flame || 0,
            deviceStatus: response.data.deviceStatus || 'offline',
            temperatureTrend: response.data.temperatureTrend || 'stable',
            humidityTrend: response.data.humidityTrend || 'stable',
            co2Trend: response.data.co2Trend || 'stable',
            lightTrend: response.data.lightTrend || 'stable',
            soilTrend: response.data.soilTrend || 'stable',
            flameTrend: response.data.flameTrend || 'stable',
            // 优先使用后端返回的时间戳字段
            temperatureTime: response.data.temperatureTime || '--',
            humidityTime: response.data.humidityTime || '--',
            lightTime: response.data.lightTime || '--',
            soilTime: response.data.soilTime || '--',
            flameTime: response.data.flameTime || '--',
            updateTime: response.data.updateTime || new Date().toLocaleString()
          }
        } else {
          throw new Error(response.msg || '获取数据失败')
        }
      } catch (error) {
        console.error('获取传感器数据失败:', error)
        this.$message.error('获取传感器数据失败: ' + error.message)
        // 如果API调用失败，使用默认数据
        this.sensorData = {
          temperature: 0,
          humidity: 0,
          co2: 0,
          light: 0,
          soil: 0,
          flame: 0,
          deviceStatus: 'offline',
          temperatureTrend: 'stable',
          humidityTrend: 'stable',
          co2Trend: 'stable',
          lightTrend: 'stable',
          soilTrend: 'stable',
          flameTrend: 'stable',
          temperatureTime: '--',
          humidityTime: '--',
          lightTime: '--',
          soilTime: '--',
          flameTime: '--',
          updateTime: new Date().toLocaleString()
        }
      }
    },
    
    // 刷新传感器数据
    refreshSensorData() {
      this.initSensorData()
      this.$refs.sensorChart.refreshData()
      this.$message.success('数据已刷新')
    },
    
    // 开始自动刷新
    startAutoRefresh() {
      // 每30秒自动刷新一次数据
      this.dataTimer = setInterval(() => {
        this.initSensorData()
      }, 30 * 1000)
    }
  },
};
</script>

<style scoped lang="scss">
.home {
  .sensor-overview {
    margin-bottom: 20px;
    
    .el-card__header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      
      span {
        font-weight: bold;
        font-size: 16px;
      }
      
      .el-button {
        color: white;
        
        &:hover {
          color: #f0f0f0;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .home .sensor-overview {
    margin: 10px;
  }
}
</style>

