import request from '@/utils/request'

// 查询报名记录选手列表
export function listJwSignRecordSport(query) {
  return request({
    url: '/jiewu/JwSignRecordSport/list',
    method: 'get',
    params: query
  })
}

// 查询报名记录选手详细
export function getJwSignRecordSport(id) {
  return request({
    url: '/jiewu/JwSignRecordSport/' + id,
    method: 'get'
  })
}

// 新增报名记录选手
export function addJwSignRecordSport(data) {
  return request({
    url: '/jiewu/JwSignRecordSport',
    method: 'post',
    data: data
  })
}

// 修改报名记录选手
export function updateJwSignRecordSport(data) {
  return request({
    url: '/jiewu/JwSignRecordSport',
    method: 'put',
    data: data
  })
}

// 删除报名记录选手
export function delJwSignRecordSport(id) {
  return request({
    url: '/jiewu/JwSignRecordSport/' + id,
    method: 'delete'
  })
}
