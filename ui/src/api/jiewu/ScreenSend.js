import request from '@/utils/request'

// 播放音乐
export function sendMusic(data) {
  return request({
    url: '/jiewu/screensend/sendMusic',
    method: 'post',
    params: data
  })
}

// 投屏对阵图
export function sendBattle(data) {
  return request({
    url: '/jiewu/screensend/sendBattle',
    method: 'post',
    params: data
  })
}

// 投屏成绩
export function sendHaiXuanGrade(data) {
  return request({
    url: '/jiewu/screensend/sendHaiXuanGrade',
    method: 'post',
    params: data
  })
}

// 投屏晋级名单
export function sendJinJiSport(data) {
  return request({
    url: '/jiewu/screensend/sendJinJiSport',
    method: 'post',
    params: data
  })
}


