package com.ruoyi.project.jiewu.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.jiewu.mapper.JwEightScoreMapper;
import com.ruoyi.project.jiewu.service.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/app/score/api")
public class JwAppScoreController extends BaseController {

    @Autowired
    private JwMatchService jwMatchService;

    @Autowired
    private JwJudgeMatchService jwJudgeMatchService;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwScheduleInfoService jwScheduleInfoService;

    @Autowired
    private JwSchedulePlaceService jwSchedulePlaceService;

    @Autowired
    private JwScheduleItemService jwScheduleItemService;

    @Autowired
    private JwJudgeService jwJudgeService;

    @Autowired
    private JwHaiScoreService jwHaiScoreService;

    @Autowired
    private JwEightService jwEightService;

    @Autowired
    private JwEightScoreService jwEightScoreService;

    @Autowired
    private JwEightScoreMapper jwEightScoreMapper;

    @Autowired
    private RedisCache redisCache;


    @PostMapping("/getGameInfo")
    @ResponseBody
    public AjaxResult getGameInfo(JwJudgeMatch jwJudgeMatch) {
        Map<String, Object> result = new HashMap<>();
        JwMatch jwMatch = jwMatchService.selectJwMatchById(jwJudgeMatch.getMatchId());
//        jwMatch.setMatchName(jwMatch.getMatchName().replaceAll("<br/>", " "));

        result.put("match", jwMatch);
        result.put("judgeList", jwJudgeMatchService.selectJwJudgeMatchList(jwJudgeMatch));
        return AjaxResult.success(result);
    }

    @PostMapping("/getJudgeList")
    @ResponseBody
    public AjaxResult getJudgeList() {

        return AjaxResult.success(1);
    }

    @PostMapping("/judgeLogin")
    @ResponseBody
    public AjaxResult judgeLogin(JwJudgeMatch jwJudgeMatch) {
        JwJudge jwJudge = jwJudgeService.selectJwJudgeById(jwJudgeMatch.getJudgeId());
        return AjaxResult.success(jwJudge);
    }

    // 获取全部比赛组别
    @PostMapping("/getScheduleItems")
    @ResponseBody
    public AjaxResult getScheduleItems(JwJudgeMatch jwJudgeMatch) {
        List<JwScheduleInfo> jwScheduleInfoList = jwScheduleInfoService.selectJwScheduleInfoByMatchId(jwJudgeMatch.getMatchId());
        if (jwScheduleInfoList != null && jwScheduleInfoList.size() > 0) {
            jwScheduleInfoList.forEach(jwScheduleInfo -> {
                JwSchedulePlace query = new JwSchedulePlace();
                query.setMatchId(jwJudgeMatch.getMatchId());
                query.setScheduleInfoId(jwScheduleInfo.getId());
                query.setLockScore("N");
                jwScheduleInfo.setJwSchedulePlaceList(jwSchedulePlaceService.listJwSchedulePlaceWithScheduleItem(query));
            });
        }
        return AjaxResult.success(jwScheduleInfoList);
    }

    // 根据组别获取选手名单

    @PostMapping("/getSports")
    @ResponseBody
    public AjaxResult getSports(Long judgeId, Long matchId, Long scheduleItemId) {

        JwScheduleItem jwScheduleItem = jwScheduleItemService.selectJwScheduleItemById(scheduleItemId);
        if("2".equals(jwScheduleItem.getItemProcess())){
            // 决赛对阵  获取全部对阵选手，拿到前端去处理
            JwEight query = new JwEight();
            query.setGameItemId(jwScheduleItem.getGameItemId());
            List<JwEight> list = jwEightService.selectJwEightListWithSport(query);
            Map<String, Object> result = new HashMap<>();
            result.put("isJueSai", true);
            result.put("list", list);
            return AjaxResult.success(result);

        }else{
            List<JwSignRecord> signRecordList = jwSignRecordService.selectJwSignRecordListWithJudgeScore(judgeId, scheduleItemId);
            return AjaxResult.success(signRecordList);
        }
    }

    // 保存打分
    @PostMapping("/saveScore")
    @ResponseBody
    public AjaxResult saveScore(Long judgeId, String score, Long sportId) {
        return AjaxResult.success(jwHaiScoreService.saveJudgeScore(judgeId, score, sportId));
    }


    // 主持点击开始打分
    @PostMapping("/startPk")
    @ResponseBody
    public AjaxResult startPk(Long gameItemId, String currentPkGroup, String currentPosition) {
        JSONObject msg = new JSONObject();
        msg.put("type", "startPk");
        msg.put("gameItemId", gameItemId);
        msg.put("currentPkGroup", currentPkGroup);
        msg.put("currentPosition", currentPosition);

        String area  = JSONObject.parse(currentPkGroup).getString("area");
        JwEight query = new JwEight();
        query.setGameItemId(gameItemId);
        query.setPlayerPosition(currentPosition);

        List<JwEight> list = jwEightService.selectJwEightListWithSport(query);
        msg.put("list", list);

        if(StringUtils.isEmpty(area)){
            redisCache.setCacheObject("currentPk:A", null);
            redisCache.setCacheObject("currentPk:B", null);
        }else{
            if("全".equals(area)){
                redisCache.setCacheObject("currentPk:A", msg);
                redisCache.setCacheObject("currentPk:B", msg);
            }else{
                redisCache.setCacheObject("currentPk:" + area, msg);
            }
        }

        WebsocketServe.sendUserListTypeMessage("juesai-", msg.toJSONString());
        return AjaxResult.success(1);
    }

    // 显示对阵的裁判打分
    @PostMapping("/xianshidafen")
    @ResponseBody
    public AjaxResult xianshidafen() {
        JSONObject msg = new JSONObject();
        msg.put("type", "showScore");

        WebsocketServe.sendUserListTypeMessage("juesai-", msg.toJSONString());
        return AjaxResult.success(1);
    }


    @PostMapping("/getCurrentPk")
    @ResponseBody
    public AjaxResult getCurrentPk(String area, Long judgeId) {
        JSONObject msg = redisCache.getCacheObject("currentPk:" + area);
        JSONObject currentPkGroup = msg.getJSONObject("currentPkGroup");
        if(currentPkGroup != null){
            String currentPk = currentPkGroup.getString("currentPk");
            if(StringUtils.isNotEmpty(currentPk)){
                JwEightScore query = new JwEightScore();
                query.setJudgeId(judgeId);
                query.setPlayerPkGroup(currentPk);
                query.setPlayerGroup(msg.getLong("gameItemId"));
                List<JwEightScore> jwEightScoreList = jwEightScoreService.selectJwEightScoreList(query);
                msg.put("jwEightScoreList", jwEightScoreList);
            }
        }
        return AjaxResult.success(msg);
    }

    // 保存对阵打分
    @PostMapping("/saveEightScore")
    @ResponseBody
    public AjaxResult saveEightScore(String judgeId, String currentPkGroup, Long jinJiId , Long lun, String subScore) {
        return AjaxResult.success(jwEightScoreService.saveEightScore(judgeId, currentPkGroup, jinJiId, lun, subScore));
    }

    // 获取对阵打分记录
    @PostMapping("/getPkScores")
    @ResponseBody
    public AjaxResult getPkScores(String currentPkGroup, Long gameItemId) {
//        List<JwJudge> judgeList = new ArrayList<>();

//        JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(gameItemId);

//        if(StringUtils.isNotEmpty(jwGameItem.getJudgeId())){
////             judgeList = jwJudgeService.selectJwJudgeByIds(jwGameItem.getJudgeId().split(","));
//        }

        List<JwEightScore> jwEightScoreList = jwEightScoreMapper.selectJwEightScoreListByPkGroup(currentPkGroup, gameItemId);
//        if(jwEightScoreList != null && jwEightScoreList.size() > 0){
//            for(JwEightScore  jwEightScore : jwEightScoreList){
//                judgeList.removeIf(jwJudge -> jwJudge.getId().equals(jwEightScore.getJudgeId()));
//            }
//
//            if(judgeList.size() > 0){
//                judgeList.forEach(jwJudge -> {
//                    JwEightScore jwEightScore = new JwEightScore();
//                    jwEightScore.setJudgeId(jwJudge.getId());
//                    jwEightScore.setJudgeName(jwJudge.getJudgeName());
//                    jwEightScore.setLun(1l);
//                    jwEightScoreList.add(jwEightScore);
//                });
//            }
//        }

        return AjaxResult.success(jwEightScoreList);
    }
}