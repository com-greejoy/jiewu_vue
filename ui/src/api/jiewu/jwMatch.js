import request from '@/utils/request'

// 查询赛事管理列表
export function listJwMatch(query) {
  return request({
    url: '/jiewu/jwMatch/list',
    method: 'get',
    params: query
  })
}

// 查询赛事管理详细
export function getJwMatch(id) {
  return request({
    url: '/jiewu/jwMatch/' + id,
    method: 'get'
  })
}

// 新增赛事管理
export function addJwMatch(data) {
  return request({
    url: '/jiewu/jwMatch',
    method: 'post',
    data: data
  })
}

// 修改赛事管理
export function updateJwMatch(data) {
  return request({
    url: '/jiewu/jwMatch',
    method: 'put',
    data: data
  })
}
export function genBackNumPDF(data) {
  return request({
    url: '/jiewu/jwMatch/genBackNumPDF',
    method: 'post',
    data: data
  })
}


// 删除赛事管理
export function delJwMatch(id) {
  return request({
    url: '/jiewu/jwMatch/' + id,
    method: 'delete'
  })
}
