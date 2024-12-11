import request from '@/utils/request'

// 查询对阵打分列表
export function listJwEightScore(query) {
  return request({
    url: '/jiewu/JwEightScore/list',
    method: 'get',
    params: query
  })
}

// 查询对阵打分详细
export function getJwEightScore(id) {
  return request({
    url: '/jiewu/JwEightScore/' + id,
    method: 'get'
  })
}

// 新增对阵打分
export function addJwEightScore(data) {
  return request({
    url: '/jiewu/JwEightScore',
    method: 'post',
    data: data
  })
}

// 修改对阵打分
export function updateJwEightScore(data) {
  return request({
    url: '/jiewu/JwEightScore',
    method: 'put',
    data: data
  })
}

// 删除对阵打分
export function delJwEightScore(id) {
  return request({
    url: '/jiewu/JwEightScore/' + id,
    method: 'delete'
  })
}
