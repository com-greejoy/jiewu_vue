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

// 计算成绩
export function jiSuanGameItem(data) {
  return request({
    url: '/jiewu/JwHaiScore/jiSuanGameItem',
    method: 'post',
    params: data
  })
}

// 修改排名
export function saveCustomOrder(data) {
  return request({
    url: '/jiewu/JwHaiScore/saveCustomOrder',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

// 获取名次
export function listGameItemGradeDes(data) {
  return request({
    url: '/jiewu/JwHaiScore/listGameItemGradeDes',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}


// 海选完成
export function haiXuanComplete(data) {
  return request({
    url: '/jiewu/JwHaiScore/haiXuanComplete',
    method: 'post',
    params: data
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

// 获取代表队成绩统计
export function listTeamGradeDes(data) {
  return request({
    url: '/jiewu/JwHaiScore/listTeamGradeDes',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

// 获取比赛全部证书
export function listAllGameItemGradeDes(data) {
  return request({
    url: '/jiewu/JwHaiScore/listAllGameItemGradeDes',
    method: 'post',
    params: data
  })
}
