import request from '@/utils/request'

// 查询队伍人员列表
export function listJwTeamLeader(query) {
  return request({
    url: '/jiewu/JwTeamLeader/list',
    method: 'get',
    params: query
  })
}

// 查询队伍人员详细
export function getJwTeamLeader(id) {
  return request({
    url: '/jiewu/JwTeamLeader/' + id,
    method: 'get'
  })
}

// 新增队伍人员
export function addJwTeamLeader(data) {
  return request({
    url: '/jiewu/JwTeamLeader',
    method: 'post',
    data: data
  })
}

// 修改队伍人员
export function updateJwTeamLeader(data) {
  return request({
    url: '/jiewu/JwTeamLeader',
    method: 'put',
    data: data
  })
}

// 删除队伍人员
export function delJwTeamLeader(id) {
  return request({
    url: '/jiewu/JwTeamLeader/' + id,
    method: 'delete'
  })
}
