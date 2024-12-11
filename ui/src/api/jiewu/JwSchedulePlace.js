import request from '@/utils/request'

// 查询场次列表
export function listJwSchedulePlace(query) {
  return request({
    url: '/jiewu/JwSchedulePlace/list',
    method: 'get',
    params: query
  })
}

export function listJwSchedulePlaceWithScheduleItem(query) {
  return request({
    url: '/jiewu/JwSchedulePlace/listJwSchedulePlaceWithScheduleItem',
    method: 'get',
    params: query
  })
}

// 查询场次详细
export function getJwSchedulePlace(id) {
  return request({
    url: '/jiewu/JwSchedulePlace/' + id,
    method: 'get'
  })
}

// 新增场次
export function addJwSchedulePlace(data) {
  return request({
    url: '/jiewu/JwSchedulePlace',
    method: 'post',
    data: data,
    headers: {repeatSubmit: false}
  })
}

// 修改场次
export function updateJwSchedulePlace(data) {
  return request({
    url: '/jiewu/JwSchedulePlace',
    method: 'put',
    data: data
  })
}

// 删除场次
export function delJwSchedulePlace(id) {
  return request({
    url: '/jiewu/JwSchedulePlace/' + id,
    method: 'delete'
  })
}
