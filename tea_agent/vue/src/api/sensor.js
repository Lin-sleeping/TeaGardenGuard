import request from '@/utils/request'

// 获取传感器实时数据
export const getSensorData = () => {
  return request({
    url: '/sensor/current',
    method: 'get',
    timeout: 10000 // 10秒超时
  })
}

// 获取传感器历史数据
export const getSensorHistory = (params) => {
  return request({
    url: '/sensor/history',
    method: 'get',
    params: {
      hours: 24,
      type: 'all',
      ...params
    },
    timeout: 15000 // 15秒超时
  })
}

// 获取传感器统计数据
export const getSensorStats = () => {
  return request({
    url: '/sensor/stats',
    method: 'get',
    timeout: 10000
  })
}

// 获取设备状态
export const getDeviceStatus = () => {
  return request({
    url: '/device/status',
    method: 'get',
    timeout: 5000 // 5秒超时
  })
}

// 获取传感器数据（带重试机制）
export const getSensorDataWithRetry = async (retryCount = 3) => {
  for (let i = 0; i < retryCount; i++) {
    try {
      const response = await getSensorData()
      return response
    } catch (error) {
      console.warn(`获取传感器数据失败，第${i + 1}次重试:`, error)
      if (i === retryCount - 1) {
        throw error
      }
      // 等待1秒后重试
      await new Promise(resolve => setTimeout(resolve, 1000))
    }
  }
} 