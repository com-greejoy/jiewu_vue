import request from '@/utils/request'

// 播放音乐
export function sendMusic(data) {
  return request({
    url: '/jiewu/screensend/sendMusic',
    method: 'post',
    params: data
  })
}

// 屏幕操作
export function sendScreenOpt(data) {
  return request({
    url: '/jiewu/screensend/sendScreenOpt',
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

// 投屏一个选手的打分详情
export function sendScoreScreen(data) {
  return request({
    url: '/jiewu/screensend/sendScoreScreen',
    method: 'post',
    params: data
  })
}



