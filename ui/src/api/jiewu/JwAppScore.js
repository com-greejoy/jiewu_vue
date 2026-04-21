import request from '@/utils/request'

export function getMatchInfo(data) {
  return request({
    url: '/app/score/api/getGameInfo',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

export function startPk(data) {
  return request({
    url: '/app/score/api/startPk',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

export function xianshidafen(data) {
  return request({
    url: '/app/score/api/xianshidafen',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

export function xianshi3dafen(data) {
  return request({
    url: '/app/score/api/xianshi3dafen',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}


export function getScheduleItems(data) {
  return request({
    url: '/app/score/api/getScheduleItems',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

export function getSports(data) {
  return request({
    url: '/app/score/api/getSports',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

export function getGameItemAwards(data) {
  return request({
    url: '/app/score/api/getGameItemAwards',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

export function saveScore(data) {
  return request({
    url: '/app/score/api/saveScore',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

export function saveAward(data) {
  return request({
    url: '/app/score/api/saveAward',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

export function getCurrentPk(data) {
  return request({
    url: '/app/score/api/getCurrentPk',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

export function saveEightScore(data) {
  return request({
    url: '/app/score/api/saveEightScore',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}

export function getPkScores(data) {
  return request({
    url: '/app/score/api/getPkScores',
    method: 'post',
    params: data,
    headers: {repeatSubmit: false}
  })
}


