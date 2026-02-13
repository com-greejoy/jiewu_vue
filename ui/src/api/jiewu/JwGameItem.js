import request from '@/utils/request'

// 查询比赛项目列表
export function listJwGameItem(query) {
  return request({
    url: '/jiewu/JwGameItem/list',
    method: 'get',
    params: query
  })
}

// 查询比赛项目详细
export function getJwGameItem(id) {
  return request({
    url: '/jiewu/JwGameItem/' + id,
    method: 'get'
  })
}

// 新增比赛项目
export function addJwGameItem(data) {
  return request({
    url: '/jiewu/JwGameItem',
    method: 'post',
    data: data
  })
}

// 修改比赛项目
export function updateJwGameItem(data) {
  return request({
    url: '/jiewu/JwGameItem',
    method: 'put',
    data: data
  })
}

// 删除比赛项目
export function delJwGameItem(id) {
  return request({
    url: '/jiewu/JwGameItem/' + id,
    method: 'delete'
  })
}

// 比赛项目重新分组
export function reGroupJwGameItem(data) {
  return request({
    url: '/jiewu/JwGameItem/reGroupJwGameItem',
    method: 'post',
    params: data
  })
}

// 锁定一个项目打分
export function lockJwGameItem(data) {
  return request({
    url: '/jiewu/JwGameItem/lockJwGameItem',
    method: 'post',
    params: data
  })
}

// 获取一个组别线上线下的成绩做对比
export function getGameItemGradeComPar(data) {
  return request({
    url: '/jiewu/JwGameItem/getGameItemGradeComPar',
    method: 'post',
    params: data
  })
}

// 同步一个组别的成绩
export function uploadGameItemGrade(data) {
  return request({
    url: '/jiewu/JwGameItem/uploadGameItemGrade',
    method: 'post',
    params: data
  })
}
