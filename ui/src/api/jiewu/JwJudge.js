import request from '@/utils/request'

// 查询打分裁判列表
export function listJwJudge(query) {
  return request({
    url: '/jiewu/JwJudge/list',
    method: 'get',
    params: query
  })
}

// 查询打分裁判详细
export function getJwJudge(id) {
  return request({
    url: '/jiewu/JwJudge/' + id,
    method: 'get'
  })
}

// 新增打分裁判
export function addJwJudge(data) {
  return request({
    url: '/jiewu/JwJudge',
    method: 'post',
    data: data
  })
}

// 修改打分裁判
export function updateJwJudge(data) {
  return request({
    url: '/jiewu/JwJudge',
    method: 'put',
    data: data
  })
}

// 删除打分裁判
export function delJwJudge(id) {
  return request({
    url: '/jiewu/JwJudge/' + id,
    method: 'delete'
  })
}
