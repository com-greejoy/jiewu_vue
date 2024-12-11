import request from '@/utils/request'

// 查询奖项设置列表
export function listJwAwardsItem(query) {
  return request({
    url: '/jiewu/JwAwardsItem/list',
    method: 'get',
    params: query
  })
}

// 查询奖项设置详细
export function getJwAwardsItem(id) {
  return request({
    url: '/jiewu/JwAwardsItem/' + id,
    method: 'get'
  })
}

// 新增奖项设置
export function addJwAwardsItem(data) {
  return request({
    url: '/jiewu/JwAwardsItem',
    method: 'post',
    data: data
  })
}

// 修改奖项设置
export function updateJwAwardsItem(data) {
  return request({
    url: '/jiewu/JwAwardsItem',
    method: 'put',
    data: data
  })
}

// 删除奖项设置
export function delJwAwardsItem(id) {
  return request({
    url: '/jiewu/JwAwardsItem/' + id,
    method: 'delete'
  })
}
