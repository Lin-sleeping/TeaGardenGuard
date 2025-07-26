<template>
  <div style="position:relative; width:100%;">
    <div style="position:absolute; left:0; top:0; width:100%; z-index:2; pointer-events:none;">
      <div ref="legendContainer" style="height:32px;"></div>
    </div>
    <div ref="scrollWrapper" style="overflow-x:auto; width:100%;">
      <div :class="className" :style="{height:height, width: chartWidth, minWidth: '600px'}" />
    </div>
  </div>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true
    },
    timeLabels: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      chart: null
      , chartWidth: '100%'
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    },
    timeLabels: {
      handler(val) {
        this.updateChartWidth(val)
      },
      immediate: true
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.updateChartWidth(this.timeLabels)
      this.initChart()
      this.renderFixedLegend()
    })
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    renderFixedLegend() {
      // 只渲染一次legend，固定在顶部
      if (!this.$refs.legendContainer) return
      this.$refs.legendContainer.innerHTML = `
        <div style="display:flex;align-items:center;gap:24px;font-size:15px;justify-content:center;width:100%;">
          <span style="display:inline-flex;align-items:center;gap:8px;font-weight:bold;">
            <span style="display:inline-flex;align-items:center;">
              <span style="display:inline-block;width:32px;height:4px;background:#3888fa;position:relative;border-radius:2px;">
                <span style="display:inline-block;width:12px;height:12px;border-radius:50%;background:#fff;position:absolute;left:50%;top:50%;transform:translate(-50%,-50%);box-shadow:0 2px 8px #a0c4ff55;border:3px solid #3888fa;"></span>
              </span>
            </span>
            <span style="color:#3888fa;font-weight:bold;letter-spacing:1px;margin-left:6px;">actual</span>
          </span>
        </div>
      `
    },
    updateChartWidth(labels) {
      // 动态计算每个label所需宽度，保证所有时间点完整显示
      this.$nextTick(() => {
        const wrapper = this.$refs.scrollWrapper
        const containerWidth = wrapper ? wrapper.offsetWidth : 600
        let maxLabelLen = 0
        if (labels && labels.length > 0) {
          maxLabelLen = Math.max(...labels.map(l => (l ? l.length : 0)))
        }
        // 每个label宽度 = 字符数*10 + 30（旋转后留白），最小60px
        const labelWidth = Math.max(maxLabelLen * 10 + 30, 60)
        const w = Math.max(labels.length * labelWidth, 600)
        this.chartWidth = w + 'px'
        if (this.chart) {
          this.chart.resize()
        }
      })
    },
    initChart() {
      this.chart = echarts.init(this.$el.querySelector('.' + this.className), 'macarons')
      this.setOptions(this.chartData)
      this.$nextTick(() => {
        if (this.chart) this.chart.resize()
      })
    },
    setOptions({ expectedData, actualData } = {}) {
      this.chart.setOption({
        xAxis: {
          data: Array.isArray(this.timeLabels) && this.timeLabels.length > 0 ? this.timeLabels : [],
          boundaryGap: false,
          axisTick: {
            show: false
          },
          axisLabel: {
            interval: 0,
            rotate: 60,
            fontSize: 14,
            margin: 12,
            overflow: 'none',
            showMaxLabel: false,
            showMinLabel: false,
            inside: false
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 30,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 10],
          formatter: function(params) {
            // params是数组，找actual系列
            let time = params[0] && params[0].axisValueLabel ? params[0].axisValueLabel : ''
            let actual = ''
            for (let i = 0; i < params.length; i++) {
              if (params[i].seriesName === 'actual') {
                actual = params[i].data
                break
              }
            }
            return `<div style='font-size:16px;font-weight:bold;margin-bottom:4px;'>${time}</div>` +
                   `<div style='color:#4FC3F7;font-weight:bold;'>● actual: ${actual}</div>`
          }
        },
        yAxis: {
          axisTick: {
            show: false
          }
        },
        legend: { show: false },
        series: [{
          name: 'expected', itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: expectedData,
          animationDuration: 2800,
          animationEasing: 'cubicInOut'
        },
        {
          name: 'actual',
          smooth: true,
          type: 'line',
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2
              },
              areaStyle: {
                color: '#f3f8ff'
              }
            }
          },
          data: actualData,
          animationDuration: 2800,
          animationEasing: 'quadraticOut'
        }]
      })
    },
    handleResize() {
      this.updateChartWidth(this.timeLabels)
    }
  }
}
</script>
