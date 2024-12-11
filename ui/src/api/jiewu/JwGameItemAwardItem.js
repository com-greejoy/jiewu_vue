import request from '@/utils/request'

// 查询项目奖项列表
export function listJwGameItemAwardItem(query) {
  return request({
    url: '/jiewu/JwGameItemAwardItem/list',
    method: 'get',
    params: query
  })
}

// 查询项目奖项详细
export function getJwGameItemAwardItem(gameItemId) {
  return request({
    url: '/jiewu/JwGameItemAwardItem/' + gameItemId,
    method: 'get'
  })
}

// 新增项目奖项
export function addJwGameItemAwardItem(data) {
  return request({
    url: '/jiewu/JwGameItemAwardItem',
    method: 'post',
    data: data
  })
}

// 修改项目奖项
export function updateJwGameItemAwardItem(data) {
  return request({
    url: '/jiewu/JwGameItemAwardItem',
    method: 'put',
    data: data
  })
}

// 删除项目奖项
export function delJwGameItemAwardItem(gameItemId) {
  return request({
    url: '/jiewu/JwGameItemAwardItem/' + gameItemId,
    method: 'delete'
  })
}
