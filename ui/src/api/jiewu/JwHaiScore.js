import request from '@/utils/request'

// 查询海选打分列表
export function listJwHaiScore(query) {
  return request({
    url: '/jiewu/JwHaiScore/list',
    method: 'get',
    params: query
  })
}

// 查询海选打分详细
export function getJwHaiScore(id) {
  return request({
    url: '/jiewu/JwHaiScore/' + id,
    method: 'get'
  })
}

// 新增海选打分
export function addJwHaiScore(data) {
  return request({
    url: '/jiewu/JwHaiScore',
    method: 'post',
    data: data
  })
}

// 修改海选打分
export function updateJwHaiScore(data) {
  return request({
    url: '/jiewu/JwHaiScore',
    method: 'put',
    data: data
  })
}

// 删除海选打分
export function delJwHaiScore(id) {
  return request({
    url: '/jiewu/JwHaiScore/' + id,
    method: 'delete'
  })
}
