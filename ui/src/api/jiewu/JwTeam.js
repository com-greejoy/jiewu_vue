import request from '@/utils/request'

// 查询代表队列表
export function listJwTeam(query) {
  return request({
    url: '/jiewu/JwTeam/list',
    method: 'get',
    params: query
  })
}

// 查询代表队详细
export function getJwTeam(id) {
  return request({
    url: '/jiewu/JwTeam/' + id,
    method: 'get'
  })
}

// 新增代表队
export function addJwTeam(data) {
  return request({
    url: '/jiewu/JwTeam',
    method: 'post',
    data: data
  })
}

// 修改代表队
export function updateJwTeam(data) {
  return request({
    url: '/jiewu/JwTeam',
    method: 'put',
    data: data
  })
}

// 删除代表队
export function delJwTeam(id) {
  return request({
    url: '/jiewu/JwTeam/' + id,
    method: 'delete'
  })
}
