import request from '@/utils/request'

// 查询比赛参赛的队伍列表
export function listJwMatchTeam(query) {
  return request({
    url: '/jiewu/JwMatchTeam/list',
    method: 'get',
    params: query
  })
}

// 查询比赛参赛的队伍详细
export function getJwMatchTeam(teamId) {
  return request({
    url: '/jiewu/JwMatchTeam/' + teamId,
    method: 'get'
  })
}

// 新增比赛参赛的队伍
export function addJwMatchTeam(data) {
  return request({
    url: '/jiewu/JwMatchTeam',
    method: 'post',
    data: data
  })
}

// 获取代表队费用
export function getTeamFee(data) {
  return request({
    url: '/jiewu/JwMatchTeam/getTeamFee',
    method: 'post',
    params: data
  })
}

// 获取代表队赛程
export function getTeamScheduleInfoList(data) {
  return request({
    url: '/jiewu/JwMatchTeam/getTeamScheduleInfoList',
    method: 'post',
    params: data
  })
}


// 修改比赛参赛的队伍
export function updateJwMatchTeam(data) {
  return request({
    url: '/jiewu/JwMatchTeam',
    method: 'put',
    data: data
  })
}

// 删除比赛参赛的队伍
export function delJwMatchTeam(teamId) {
  return request({
    url: '/jiewu/JwMatchTeam/' + teamId,
    method: 'delete'
  })
}
