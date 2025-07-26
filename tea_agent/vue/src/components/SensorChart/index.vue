<template>
  <div class="sensor-chart">
    <div class="today-info" style="font-weight:bold;font-size:16px;margin-bottom:10px;">{{ todayStr }}</div>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span>温度趋势</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button>
          </div>
          <div class="chart-container">
            <div style="overflow-x:auto; width:100%;">
              <line-chart :chart-data="temperatureChartData" :timeLabels="tempTimeLabels" height="300px" />
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span>湿度趋势</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button>
          </div>
          <div class="chart-container">
            <div style="overflow-x:auto; width:100%;">
              <line-chart :chart-data="humidityChartData" :timeLabels="humTimeLabels" height="300px" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="8">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span>光照强度趋势</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button>
          </div>
          <div class="chart-container">
            <div style="overflow-x:auto; width:100%;">
              <line-chart :chart-data="lightChartData" :timeLabels="lightTimeLabels" height="220px" />
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span>土壤湿度趋势</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button>
          </div>
          <div class="chart-container">
            <div style="overflow-x:auto; width:100%;">
              <line-chart :chart-data="soilChartData" :timeLabels="soilTimeLabels" height="220px" />
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <div slot="header" class="clearfix">
            <span>火焰强度趋势</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button>
          </div>
          <div class="chart-container">
            <div style="overflow-x:auto; width:100%;">
              <line-chart :chart-data="flameChartData" :timeLabels="flameTimeLabels" height="220px" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import LineChart from '@/views/dashboard/LineChart'
import { getSensorHistory } from '@/api/sensor'
import { parseTime } from '@/utils/ruoyi'

export default {
  name: 'SensorChart',
  components: {
    LineChart
  },
  data() {
    return {
      temperatureChartData: {
        expectedData: [],
        actualData: []
      },
      humidityChartData: {
        expectedData: [],
        actualData: []
      },
      lightChartData: {
        expectedData: [],
        actualData: []
      },
      soilChartData: {
        expectedData: [],
        actualData: []
      },
      flameChartData: {
        expectedData: [],
        actualData: []
      },
      chartTimer: null,
      tempTimeLabels: [],
      humTimeLabels: [],
      lightTimeLabels: [],
      soilTimeLabels: [],
      flameTimeLabels: [],
      todayStr: ''
    }
  },
  mounted() {
    this.initData()
    this.startAutoRefresh()
    this.setTodayStr()
  },
  beforeDestroy() {
    if (this.chartTimer) {
      clearInterval(this.chartTimer)
    }
  },
  methods: {
    setTodayStr() {
      const now = new Date()
      const weekMap = ['日', '一', '二', '三', '四', '五', '六']
      const y = now.getFullYear()
      const m = (now.getMonth() + 1).toString().padStart(2, '0')
      const d = now.getDate().toString().padStart(2, '0')
      const week = weekMap[now.getDay()]
      this.todayStr = `${y}-${m}-${d} 星期${week}`
    },
    async initData() {
      try {
        // 分别请求每种传感器的历史数据
        const [tempRes, humRes, lightRes, soilRes, flameRes] = await Promise.all([
          getSensorHistory({ type: 'temperature', hours: 1 }),
          getSensorHistory({ type: 'humidity', hours: 1 }),
          getSensorHistory({ type: 'light', hours: 1 }),
          getSensorHistory({ type: 'soil', hours: 1 }),
          getSensorHistory({ type: 'flame', hours: 1 })
        ])
        // 采集时间labels与value一一对应
        const tempLabels = (tempRes.data || []).map(item => item.time)
        const humLabels = (humRes.data || []).map(item => item.time)
        const lightLabels = (lightRes.data || []).map(item => item.time)
        const soilLabels = (soilRes.data || []).map(item => item.time)
        const flameLabels = (flameRes.data || []).map(item => item.time)
        this.temperatureChartData = {
          expectedData: (tempRes.data || []).map(item => item.value).slice(0, -1),
          actualData: (tempRes.data || []).map(item => item.value)
        }
        this.humidityChartData = {
          expectedData: (humRes.data || []).map(item => item.value).slice(0, -1),
          actualData: (humRes.data || []).map(item => item.value)
        }
        this.lightChartData = {
          expectedData: (lightRes.data || []).map(item => item.value).slice(0, -1),
          actualData: (lightRes.data || []).map(item => item.value)
        }
        this.soilChartData = {
          expectedData: (soilRes.data || []).map(item => item.value).slice(0, -1),
          actualData: (soilRes.data || []).map(item => item.value)
        }
        this.flameChartData = {
          expectedData: (flameRes.data || []).map(item => item.value).slice(0, -1),
          actualData: (flameRes.data || []).map(item => item.value)
        }
        // 分别传递给每个LineChart
        this.tempTimeLabels = tempLabels
        this.humTimeLabels = humLabels
        this.lightTimeLabels = lightLabels
        this.soilTimeLabels = soilLabels
        this.flameTimeLabels = flameLabels
      } catch (error) {
        console.error('获取传感器历史数据失败:', error)
        this.$message.error('获取传感器历史数据失败: ' + error.message)
        // 如果API调用失败，使用模拟数据
        const mockData = this.generateMockData()
        this.updateChartData(mockData)
      }
    },
    
    generateMockData() {
      const now = new Date()
      const timeLabels = []
      const temperatureData = []
      const humidityData = []
      const lightData = []
      const soilData = []
      const flameData = []
      // 生成最近24小时的数据
      for (let i = 23; i >= 0; i--) {
        const time = new Date(now.getTime() - i * 60 * 60 * 1000)
        timeLabels.push(time.getHours() + ':00')
        temperatureData.push(Math.round((Math.random() * 10 + 20) * 10) / 10)
        humidityData.push(Math.round(Math.random() * 40 + 40))
        lightData.push(Math.round(Math.random() * 1000 + 300))
        soilData.push(Math.round(Math.random() * 40 + 30))
        flameData.push(Math.round(Math.random() * 10))
      }
      return {
        timeLabels,
        temperatureData,
        humidityData,
        lightData,
        soilData,
        flameData
      }
    },
    
    updateChartData(data) {
      // 处理API返回的数据格式
      if (data && data.timeLabels) {
        this.timeLabels = data.timeLabels
        this.temperatureChartData = {
          expectedData: data.temperatureData ? data.temperatureData.slice(0, -1) : [],
          actualData: data.temperatureData || []
        }
        this.humidityChartData = {
          expectedData: data.humidityData ? data.humidityData.slice(0, -1) : [],
          actualData: data.humidityData || []
        }
        this.lightChartData = {
          expectedData: data.lightData ? data.lightData.slice(0, -1) : [],
          actualData: data.lightData || []
        }
        this.soilChartData = {
          expectedData: data.soilData ? data.soilData.slice(0, -1) : [],
          actualData: data.soilData || []
        }
        this.flameChartData = {
          expectedData: data.flameData ? data.flameData.slice(0, -1) : [],
          actualData: data.flameData || []
        }
      } else if (data && Array.isArray(data)) {
        // 兼容数组格式
        const timeLabels = []
        const temperatureData = []
        const humidityData = []
        const lightData = []
        const soilData = []
        const flameData = []
        data.forEach(item => {
          timeLabels.push(item.time || item.createTime)
          temperatureData.push(item.temperature || 0)
          humidityData.push(item.humidity || 0)
          lightData.push(item.light || 0)
          soilData.push(item.soil || 0)
          flameData.push(item.flame || 0)
        })
        this.temperatureChartData = {
          expectedData: temperatureData.slice(0, -1),
          actualData: temperatureData
        }
        this.humidityChartData = {
          expectedData: humidityData.slice(0, -1),
          actualData: humidityData
        }
        this.lightChartData = {
          expectedData: lightData.slice(0, -1),
          actualData: lightData
        }
        this.soilChartData = {
          expectedData: soilData.slice(0, -1),
          actualData: soilData
        }
        this.flameChartData = {
          expectedData: flameData.slice(0, -1),
          actualData: flameData
        }
      } else {
        // 使用模拟数据作为后备
        const mockData = this.generateMockData()
        this.temperatureChartData = {
          expectedData: mockData.temperatureData.slice(0, -1),
          actualData: mockData.temperatureData
        }
        this.humidityChartData = {
          expectedData: mockData.humidityData.slice(0, -1),
          actualData: mockData.humidityData
        }
        this.lightChartData = {
          expectedData: mockData.lightData.slice(0, -1),
          actualData: mockData.lightData
        }
        this.soilChartData = {
          expectedData: mockData.soilData.slice(0, -1),
          actualData: mockData.soilData
        }
        this.flameChartData = {
          expectedData: mockData.flameData.slice(0, -1),
          actualData: mockData.flameData
        }
      }
    },
    
    refreshData() {
      this.initData()
      this.$message.success('数据已刷新')
    },
    
    startAutoRefresh() {
      // 每5分钟自动刷新一次数据
      this.chartTimer = setInterval(() => {
        this.initData()
      }, 5 * 60 * 1000)
    }
  }
}
</script>

<style lang="scss" scoped>
.sensor-chart {
  .chart-card {
    margin-bottom: 20px;
    
    .chart-container {
      padding: 10px 0;
    }
  }
  
  .el-card__header {
    padding: 15px 20px;
    border-bottom: 1px solid #ebeef5;
    background-color: #fafafa;
    
    span {
      font-weight: bold;
      color: #333;
    }
  }
}
</style> 