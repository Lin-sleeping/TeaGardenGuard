import { constantRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView';

const permission = {
  state: {
    routes: [],
    addRoutes: [],
    sidebarRouters: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    },
    SET_SIDEBAR_ROUTERS: (state, routers) => {
      state.sidebarRouters = constantRoutes.concat(routers)
    },
  },
  actions: {
    // 生成路由
    GenerateRoutes({ commit }) {
      return new Promise(resolve => {
        // 向后端请求路由数据
        getRouters().then(res => {
          const sdata = JSON.parse(JSON.stringify(res.data))
          const rdata = JSON.parse(JSON.stringify(res.data))
          
          // 过滤掉系统监控、系统工具、物美官网相关菜单
          const filteredSdata = filterMenuData(sdata)
          const filteredRdata = filterMenuData(rdata)
          
          const sidebarRoutes = filterAsyncRouter(filteredSdata)
          const rewriteRoutes = filterAsyncRouter(filteredRdata, false, true)
          rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })
          commit('SET_ROUTES', rewriteRoutes)
          commit('SET_SIDEBAR_ROUTERS', sidebarRoutes)
          resolve(rewriteRoutes)
        })
      })
    }
  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView') {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

// 过滤菜单数据，去掉系统监控、系统工具、物美官网相关菜单
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

export const loadView = (view) => { // 路由懒加载
  return (resolve) => require([`@/views/${view}`], resolve)
}

export default permission
