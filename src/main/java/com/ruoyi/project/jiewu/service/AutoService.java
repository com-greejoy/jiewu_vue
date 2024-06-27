package com.ruoyi.project.jiewu.service;


import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.IDCardUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.jiewu.domain.JwMatch;
import com.ruoyi.project.jiewu.domain.JwSport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@EnableScheduling
public class AutoService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwSportService jwSportService;

    @Autowired
    private JwMatchService jwMatchService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void autoSportAge() {
        // 每天凌晨计算选手的年龄
        List<JwSport> jwSportList = jwSportService.selectTodayBirthSport(DateUtils.getDateBirthStr());
        jwSportList.forEach(jwSport -> {
            JwSport jwSport1 = new JwSport();
            jwSport1.setId(jwSport.getId());
            jwSport1.setAge(IDCardUtils.getAge(jwSport.getIdCard()));
            jwSportService.updateJwSport(jwSport1);
        });
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void autoMatchState() {
        // 每5分钟计算报名状态
        // 获取没开始的比赛和正在报名的比赛
        List<JwMatch> jwMatchList = jwMatchService.selectFutureIngList();
        jwMatchList.forEach(jwMatch -> {
            JwMatch jwMatch1 = new JwMatch();
            jwMatch1.setId(jwMatch.getId());
            Date now = DateUtils.getNowDate();
            //  未开始
            if (now.before(jwMatch.getSignBeginTime())) {
                jwMatch1.setState("1");
            }
            //正在报名
            if (now.after(jwMatch.getSignBeginTime()) && now.before(jwMatch.getSignEndTime())) {
                jwMatch1.setState("2");
            }
            // 报名结束
            if (now.after(jwMatch.getSignEndTime())) {
                jwMatch1.setState("3");
            }
            if (!jwMatch1.getState().equals(jwMatch.getState())) {
                jwMatchService.updateJwMatch(jwMatch1);
            }
        });
    }
}