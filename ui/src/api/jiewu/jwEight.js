import request from '@/utils/request'

// 查询对阵名单列表
export function listJwEight(query) {
  return request({
    url: '/jiewu/jwEight/list',
    method: 'get',
    params: query
  })
}

// 查询对阵名单详细
export function getJwEight(id) {
  return request({
    url: '/jiewu/jwEight/' + id,
    method: 'get'
  })
}

// 新增对阵名单
export function addJwEight(data) {
  return request({
    url: '/jiewu/jwEight',
    method: 'post',
    data: data
  })
}

export function saveEightPro(data) {
  return request({
    url: '/jiewu/jwEight/saveEightPro',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}
export function clearEightPro(data) {
  return request({
    url: '/jiewu/jwEight/clearEightPro',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

export function cancelEightPro(data) {
  return request({
    url: '/jiewu/jwEight/cancelEightPro',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}



// 修改对阵名单
export function updateJwEight(data) {
  return request({
    url: '/jiewu/jwEight',
    method: 'put',
    data: data
  })
}

// 删除对阵名单
export function delJwEight(id) {
  return request({
    url: '/jiewu/jwEight/' + id,
    method: 'delete'
  })
}
