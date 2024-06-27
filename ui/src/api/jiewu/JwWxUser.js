import request from '@/utils/request'

// 查询微信用户列表
export function listJwWxUser(query) {
  return request({
    url: '/jiewu/JwWxUser/list',
    method: 'get',
    params: query
  })
}

// 查询微信用户详细
export function getJwWxUser(id) {
  return request({
    url: '/jiewu/JwWxUser/' + id,
    method: 'get'
  })
}

// 新增微信用户
export function addJwWxUser(data) {
  return request({
    url: '/jiewu/JwWxUser',
    method: 'post',
    data: data
  })
}

// 修改微信用户
export function updateJwWxUser(data) {
  return request({
    url: '/jiewu/JwWxUser',
    method: 'put',
    data: data
  })
}

// 删除微信用户
export function delJwWxUser(id) {
  return request({
    url: '/jiewu/JwWxUser/' + id,
    method: 'delete'
  })
}
