package com.ruoyi.project.jiewu.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.jiewu.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jiewu/screensend")
public class JwScreenController extends BaseController {


    // 发送播放音乐
    @PostMapping("/sendMusic")
    @ResponseBody
    public AjaxResult sendMusic(String worksMusic, Long matchId) {
        JSONObject msg = new JSONObject();
        msg.put("type", "sendMusic");
        msg.put("worksMusic", worksMusic);
        WebsocketServe.sendUserTypeMessage("playMusicEr-" + matchId, msg.toJSONString());
        return AjaxResult.success(1);
    }

    // 投屏对阵图
    @PostMapping("/sendBattle")
    @ResponseBody
    public AjaxResult sendBattle(Long gameItemId, Long matchId) {
        JSONObject msg = new JSONObject();
        msg.put("type", "sendBattle");
        msg.put("gameItemId", gameItemId);
        WebsocketServe.sendUserTypeMessage("playMusicEr-" + matchId, msg.toJSONString());
        return AjaxResult.success(1);
    }

    // 投屏海选成绩
    @PostMapping("/sendHaiXuanGrade")
    @ResponseBody
    public AjaxResult sendHaiXuanGrade(Long gameItemId, Long matchId, Long minOrder, Long maxOrder) {
        JSONObject msg = new JSONObject();
        msg.put("type", "sendHaiXuanGrade");
        msg.put("gameItemId", gameItemId);
        msg.put("minOrder", minOrder);
        msg.put("maxOrder", maxOrder);
        WebsocketServe.sendUserTypeMessage("playMusicEr-" + matchId, msg.toJSONString());
        return AjaxResult.success(1);
    }

    // 投屏晋级名单
    @PostMapping("/sendJinJiSport")
    @ResponseBody
    public AjaxResult sendJinJiSport(Long gameItemId, Long matchId) {
        JSONObject msg = new JSONObject();
        msg.put("type", "sendJinJiSport");
        msg.put("gameItemId", gameItemId);
        WebsocketServe.sendUserTypeMessage("playMusicEr-" + matchId, msg.toJSONString());
        return AjaxResult.success(1);
    }
}