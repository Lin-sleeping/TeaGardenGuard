// 切换设备enable状态
import request from '@/utils/request'

export function changeDeviceEnable(deviceId, enable) {
  return request({
    url: '/system/device/changeEnable',
    method: 'post',
    data: { deviceId, enable }
  })
} 