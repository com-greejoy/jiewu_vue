import request from '@/utils/request'

// 查询比赛管理员列表
export function listJwMatchUser(query) {
  return request({
    url: '/jiewu/JwMatchUser/list',
    method: 'get',
    params: query
  })
}

// 查询比赛管理员详细
export function getJwMatchUser(userId) {
  return request({
    url: '/jiewu/JwMatchUser/' + userId,
    method: 'get'
  })
}

// 新增比赛管理员
export function addJwMatchUser(data) {
  return request({
    url: '/jiewu/JwMatchUser',
    method: 'post',
    data: data
  })
}

// 修改比赛管理员
export function updateJwMatchUser(data) {
  return request({
    url: '/jiewu/JwMatchUser',
    method: 'put',
    data: data
  })
}

// 删除比赛管理员
export function delJwMatchUser(userId) {
  return request({
    url: '/jiewu/JwMatchUser/' + userId,
    method: 'delete'
  })
}
