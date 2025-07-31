import request from '@/utils/request'

// 查询选手列表
export function listJwSport(query) {
  return request({
    url: '/jiewu/JwSport/list',
    method: 'get',
    params: query
  })
}

// 获取参赛了的选手列表
export function listMatchJwSport(query) {
  return request({
    url: '/jiewu/JwSport/listMatchJwSport',
    method: 'get',
    params: query
  })
}


// 查询选手详细
export function getJwSport(id) {
  return request({
    url: '/jiewu/JwSport/' + id,
    method: 'get'
  })
}

// 新增选手
export function addJwSport(data) {
  return request({
    url: '/jiewu/JwSport',
    method: 'post',
    data: data
  })
}

// 修改选手
export function updateJwSport(data) {
  return request({
    url: '/jiewu/JwSport',
    method: 'put',
    data: data
  })
}

// 修改选手队伍
export function changeTeam(data) {
  return request({
    url: '/jiewu/JwSport/changeTeam',
    method: 'post',
    params: data
  })
}


// 删除选手
export function delJwSport(id) {
  return request({
    url: '/jiewu/JwSport/' + id,
    method: 'delete'
  })
}
