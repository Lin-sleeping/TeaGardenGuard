// 测试菜单过滤逻辑
function filterMenuData(menuData) {
  if (!Array.isArray(menuData)) {
    return menuData
  }
  
  return menuData.filter(menu => {
    // 过滤掉系统监控、系统工具、物美官网相关菜单
    const excludeMenus = ['系统监控', '系统工具', '物美官网', 'monitor', 'tool', 'ruoyi']
    
    // 检查菜单名称是否在排除列表中
    if (menu.meta && menu.meta.title) {
      const title = menu.meta.title
      if (excludeMenus.some(exclude => title.includes(exclude))) {
        return false
      }
    }
    
    // 检查路径是否包含排除的关键词
    if (menu.path) {
      const path = menu.path.toLowerCase()
      if (excludeMenus.some(exclude => path.includes(exclude.toLowerCase()))) {
        return false
      }
    }
    
    // 如果有子菜单，递归过滤
    if (menu.children && menu.children.length > 0) {
      menu.children = filterMenuData(menu.children)
      // 如果过滤后没有子菜单了，也过滤掉这个父菜单
      if (menu.children.length === 0) {
        return false
      }
    }
    
    return true
  })
}

// 测试数据
const testMenuData = [
  {
    path: '/system',
    meta: { title: '系统管理' },
    children: [
      { path: '/system/user', meta: { title: '用户管理' } },
      { path: '/system/role', meta: { title: '角色管理' } }
    ]
  },
  {
    path: '/monitor',
    meta: { title: '系统监控' },
    children: [
      { path: '/monitor/server', meta: { title: '服务监控' } },
      { path: '/monitor/cache', meta: { title: '缓存监控' } }
    ]
  },
  {
    path: '/tool',
    meta: { title: '系统工具' },
    children: [
      { path: '/tool/gen', meta: { title: '代码生成' } }
    ]
  },
  {
    path: '/ruoyi',
    meta: { title: '物美官网' },
    children: [
      { path: '/ruoyi/doc', meta: { title: '开发文档' } }
    ]
  }
]

// 执行测试
const filteredData = filterMenuData(testMenuData)
console.log('原始菜单数据:', JSON.stringify(testMenuData, null, 2))
console.log('过滤后菜单数据:', JSON.stringify(filteredData, null, 2))

// 验证结果
const expectedPaths = ['/system']
const actualPaths = filteredData.map(item => item.path)
console.log('期望保留的路径:', expectedPaths)
console.log('实际保留的路径:', actualPaths)
console.log('测试结果:', JSON.stringify(expectedPaths) === JSON.stringify(actualPaths) ? '通过' : '失败') 