import request from '@/utils/request'

// 查询报名记录列表
export function listJwSignRecord(query) {
  return request({
    url: '/jiewu/JwSignRecord/list',
    method: 'get',
    params: query
  })
}

// 查询报名记录详细
export function getJwSignRecord(id) {
  return request({
    url: '/jiewu/JwSignRecord/' + id,
    method: 'get'
  })
}

// 新增报名记录
export function addJwSignRecord(data) {
  return request({
    url: '/jiewu/JwSignRecord',
    method: 'post',
    data: data
  })
}

// 修改报名记录
export function updateJwSignRecord(data) {
  return request({
    url: '/jiewu/JwSignRecord',
    method: 'put',
    data: data
  })
}

// 删除报名记录
export function delJwSignRecord(id) {
  return request({
    url: '/jiewu/JwSignRecord/' + id,
    method: 'delete'
  })
}

// 改组
export function changeGameItem(data) {
  return request({
    url: '/jiewu/JwSignRecord/changeGameItem',
    method: 'post',
    params: data
  })
}


// 修改小项
export function changeJwScheduleItem(data) {
  return request({
    url: '/jiewu/JwSignRecord/changeJwScheduleItem',
    method: 'post',
    params: data
  })
}

// 查看比赛项目的报名数据
export function listJwSignRecordByGameItem(data) {
  return request({
    url: '/jiewu/JwSignRecord/listJwSignRecordByGameItem',
    method: 'post',
    params: data
  })
}


