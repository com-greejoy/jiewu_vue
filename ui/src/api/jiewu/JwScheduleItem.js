import request from '@/utils/request'

// 查询赛程小项列表
export function listJwScheduleItem(query) {
  return request({
    url: '/jiewu/JwScheduleItem/list',
    method: 'get',
    params: query
  })
}

// 查询赛程小项详细
export function getJwScheduleItem(id) {
  return request({
    url: '/jiewu/JwScheduleItem/' + id,
    method: 'get'
  })
}

// 新增赛程小项
export function addJwScheduleItem(data) {
  return request({
    url: '/jiewu/JwScheduleItem',
    method: 'post',
    data: data
  })
}

// 修改赛程小项
export function updateJwScheduleItem(data) {
  return request({
    url: '/jiewu/JwScheduleItem',
    method: 'put',
    data: data
  })
}

// 删除赛程小项
export function delJwScheduleItem(id) {
  return request({
    url: '/jiewu/JwScheduleItem/' + id,
    method: 'delete'
  })
}

// 初始化赛程小项
export function initScheduleItem(data) {
  return request({
    url: '/jiewu/JwScheduleItem/initScheduleItem',
    method: 'post',
    params: data
  })
}
// 获取没有分配场次的小项
export function listNoPlaceJwScheduleItem(data) {
  return request({
    url: '/jiewu/JwScheduleItem/listNoPlaceJwScheduleItem',
    method: 'post',
    params: data
  })
}

// 初始化所有背号
export function initBackNum(data) {
  return request({
    url: '/jiewu/JwScheduleItem/initBackNum',
    method: 'post',
    params: data
  })
}

// 更新小项 的 场地 单元
export function updateJwScheduleItemPlace(data) {
  return request({
    url: '/jiewu/JwScheduleItem/updateJwScheduleItemPlace',
    method: 'post',
    params: data
  })
}

// 从场地中删除小项
export function clearSchedulePlaceById(data) {
  return request({
    url: '/jiewu/JwScheduleItem/clearSchedulePlaceById',
    method: 'post',
    params: data
  })
}

// 计算赛程时间
export function calculateTime(data) {
  return request({
    url: '/jiewu/JwScheduleItem/calculateTime',
    method: 'post',
    params: data
  })
}

// 整理项目的排序
export function arrangeOrder(data) {
  return request({
    url: '/jiewu/JwScheduleItem/arrangeOrder',
    method: 'post',
    params: data
  })
}

// 赛程小项重新排序
export function orderSignRecord(data) {
  return request({
    url: '/jiewu/JwScheduleItem/orderSignRecord',
    method: 'post',
    params: data
  })
}
