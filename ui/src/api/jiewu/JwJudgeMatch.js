import request from '@/utils/request'

// 查询比赛裁判列表
export function listJwJudgeMatch(query) {
  return request({
    url: '/jiewu/JwJudgeMatch/list',
    method: 'get',
    params: query
  })
}

// 查询比赛裁判详细
export function getJwJudgeMatch(judgeId) {
  return request({
    url: '/jiewu/JwJudgeMatch/' + judgeId,
    method: 'get'
  })
}

// 新增比赛裁判
export function addJwJudgeMatch(data) {
  return request({
    url: '/jiewu/JwJudgeMatch',
    method: 'post',
    data: data
  })
}

// 修改比赛裁判
export function updateJwJudgeMatch(data) {
  return request({
    url: '/jiewu/JwJudgeMatch',
    method: 'put',
    data: data
  })
}

// 删除比赛裁判
export function delJwJudgeMatch(data) {
  return request({
    url: '/jiewu/JwJudgeMatch/delete',
    method: 'post',
    data: data
  })
}
