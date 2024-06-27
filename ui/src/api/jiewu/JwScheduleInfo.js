import request from '@/utils/request'

// 查询阶段列表
export function listJwScheduleInfo(query) {
  return request({
    url: '/jiewu/JwScheduleInfo/list',
    method: 'get',
    params: query
  })
}

// 查询阶段详细
export function getJwScheduleInfo(id) {
  return request({
    url: '/jiewu/JwScheduleInfo/' + id,
    method: 'get'
  })
}

// 新增阶段
export function addJwScheduleInfo(data) {
  return request({
    url: '/jiewu/JwScheduleInfo',
    method: 'post',
    data: data
  })
}

// 修改阶段
export function updateJwScheduleInfo(data) {
  return request({
    url: '/jiewu/JwScheduleInfo',
    method: 'put',
    data: data
  })
}

// 删除阶段
export function delJwScheduleInfo(id) {
  return request({
    url: '/jiewu/JwScheduleInfo/' + id,
    method: 'delete'
  })
}
