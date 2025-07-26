<template>
  <el-row :gutter="20" class="sensor-panel" justify="center" align="middle">
    <el-col :xs="24" :sm="12" :md="8" :lg="4" :xl="4" v-for="(item, idx) in sensorCardList" :key="idx" class="card-panel-col">
      <div :class="['card-panel', item.panelClass]">
        <div class="card-panel-icon-wrapper" :class="item.iconClass">
          <svg-icon v-if="item.icon && item.icon !== 'sun' && item.icon !== 'fire'" :icon-class="item.icon" class-name="card-panel-icon" />
          <svg v-else-if="item.icon === 'sun'" class="card-panel-icon" viewBox="0 0 48 48" width="38" height="38">
            <circle cx="24" cy="24" r="14" fill="#FFB300"/>
            <g stroke="#FFA000" stroke-width="3">
              <line x1="24" y1="4" x2="24" y2="12"/>
              <line x1="24" y1="36" x2="24" y2="44"/>
              <line x1="4" y1="24" x2="12" y2="24"/>
              <line x1="36" y1="24" x2="44" y2="24"/>
              <line x1="10.93" y1="10.93" x2="16.97" y2="16.97"/>
              <line x1="31.03" y1="31.03" x2="37.07" y2="37.07"/>
              <line x1="10.93" y1="37.07" x2="16.97" y2="31.03"/>
              <line x1="31.03" y1="16.97" x2="37.07" y2="10.93"/>
            </g>
          </svg>
          <svg v-else-if="item.icon === 'fire'" class="card-panel-icon" viewBox="0 0 48 48" width="38" height="38">
            <defs>
              <linearGradient id="fireGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" stop-color="#FF9800"/>
                <stop offset="100%" stop-color="#F44336"/>
              </linearGradient>
            </defs>
            <path d="M24 6C24 6 34 16 28 28C28 28 32 26 34 32C36 38 24 42 24 42C24 42 12 38 14 32C16 26 20 28 20 28C14 16 24 6 24 6Z" fill="url(#fireGrad)"/>
          </svg>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">{{ item.label }}</div>
          <div class="card-panel-num">
            <span class="value">{{ item.value }}</span>
            <span class="unit">{{ item.unit }}</span>
          </div>
          <div class="card-panel-trend">
            <span :class="getTrendClass(item.trend)">{{ getTrendText(item.trend) }}</span>
          </div>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: 'SensorPanel',
  props: {
    sensorData: {
      type: Object,
      default: () => ({})
    }
  },
  computed: {
    sensorCardList() {
      return [
        {
          label: '温度',
          value: this.sensorData.temperature != null ? this.sensorData.temperature : '--',
          unit: '°C',
          trend: this.sensorData.temperatureTrend,
          icon: 'chart',
          iconClass: 'icon-temperature',
          panelClass: 'temperature-panel'
        },
        {
          label: '湿度',
          value: this.sensorData.humidity != null ? this.sensorData.humidity : '--',
          unit: '%',
          trend: this.sensorData.humidityTrend,
          icon: 'component',
          iconClass: 'icon-humidity',
          panelClass: 'humidity-panel'
        },
        {
          label: '光照强度',
          value: this.sensorData.light != null ? this.sensorData.light : '--',
          unit: 'lux',
          trend: this.sensorData.lightTrend,
          icon: 'sun',
          iconClass: 'icon-light',
          panelClass: 'light-panel'
        },
        {
          label: '土壤湿度',
          value: this.sensorData.soil != null ? this.sensorData.soil : '--',
          unit: '',
          trend: this.sensorData.soilTrend,
          icon: 'tree',
          iconClass: 'icon-soil',
          panelClass: 'soil-panel'
        },
        {
          label: '火焰强度',
          value: this.sensorData.flame != null ? this.sensorData.flame : '--',
          unit: '',
          trend: this.sensorData.flameTrend,
          icon: 'fire',
          iconClass: 'icon-flame',
          panelClass: 'flame-panel'
        }
      ]
    }
  },
  methods: {
    getTrendClass(trend) {
      const classes = {
        up: 'trend-up',
        down: 'trend-down',
        stable: 'trend-stable'
      }
      return classes[trend] || 'trend-stable'
    },
    getTrendText(trend) {
      const texts = {
        up: '↑ 上升',
        down: '↓ 下降',
        stable: '→ 稳定'
      }
      return texts[trend] || '→ 稳定'
    }
  }
}
</script>

<style lang="scss" scoped>
.sensor-panel {
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  .card-panel-col {
    margin-bottom: 20px;
    display: flex;
    justify-content: center;
    flex: 1 1 0%;
    max-width: 20%;
    padding-left: 18px;
    padding-right: 18px;
  }
  .card-panel {
    height: 140px;
    width: 100%;
    min-width: 0;
    max-width: none;
    cursor: pointer;
    font-size: 13px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.08);
    border-radius: 12px;
    transition: all 0.3s ease;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
    }
    .card-panel-icon-wrapper {
      margin-bottom: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      .card-panel-icon {
        font-size: 38px;
      }
    }
    .card-panel-description {
      text-align: center;
      .card-panel-text {
        font-weight: bold;
        font-size: 15px;
        margin-bottom: 2px;
      }
      .card-panel-num {
        font-size: 22px;
        font-weight: bold;
        margin-bottom: 2px;
        .unit {
          font-size: 14px;
          font-weight: normal;
          margin-left: 2px;
        }
      }
      .card-panel-trend {
        font-size: 13px;
        color: #888;
      }
    }
  }
  // 彩色卡片背景
  .temperature-panel .card-panel-icon-wrapper {
    background: linear-gradient(135deg, #ff6b6b, #ee5a24);
    color: #fff;
    border-radius: 50%;
    width: 56px;
    height: 56px;
    margin: 0 auto 8px auto;
    justify-content: center;
    align-items: center;
    display: flex;
  }
  .humidity-panel .card-panel-icon-wrapper {
    background: linear-gradient(135deg, #4ecdc4, #44a08d);
    color: #fff;
    border-radius: 50%;
    width: 56px;
    height: 56px;
    margin: 0 auto 8px auto;
    justify-content: center;
    align-items: center;
    display: flex;
  }
  .light-panel .card-panel-icon-wrapper {
    background: linear-gradient(135deg, #ffe259, #ffa751);
    color: #fff;
    border-radius: 50%;
    width: 56px;
    height: 56px;
    margin: 0 auto 8px auto;
    justify-content: center;
    align-items: center;
    display: flex;
  }
  .soil-panel .card-panel-icon-wrapper {
    background: linear-gradient(135deg, #43cea2, #185a9d);
    color: #fff;
    border-radius: 50%;
    width: 56px;
    height: 56px;
    margin: 0 auto 8px auto;
    justify-content: center;
    align-items: center;
    display: flex;
  }
  .flame-panel .card-panel-icon-wrapper {
    background: linear-gradient(135deg, #ff512f, #dd2476);
    color: #fff;
    border-radius: 50%;
    width: 56px;
    height: 56px;
    margin: 0 auto 8px auto;
    justify-content: center;
    align-items: center;
    display: flex;
  }
}
</style> 