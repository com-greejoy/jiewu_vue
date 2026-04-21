package com.ruoyi.project.jiewu.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.mchange.lang.LongUtils;
import com.ruoyi.common.utils.BigDecimalUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.jiewu.mapper.JwMatchUserMapper;
import com.ruoyi.project.jiewu.service.*;
import me.chanjar.weixin.common.error.WxErrorException;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/mini/match/api")
public class JwMiNiMatchController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(JwMiNiMatchController.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired JwWxUserService jwWxUserService;

    @Autowired
    private JwMatchService jwMatchService;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwTeamService jwTeamService;

    @Autowired
    private JwHaiScoreService jwHaiScoreService;

    @Autowired
    private JwMatchUserMapper jwMatchUserMapper;

    @Autowired
    private JwMatchTeamService jwMatchTeamService;

    @PostMapping("/listMatchGameItem")
    @ResponseBody
    public AjaxResult listMatchGameItem(Long teamId, Long matchId) {

        List<JwGameItem> list = jwGameItemService.selectJwGameItemListByMatchId(matchId, null);

        // 如果 有 teamId 就把 项目的报名人员加上
        if(StringUtils.isLongNotNull(teamId)){

            // 获取所有报名数据
            List<JwSignRecord> jwSignRecords = jwSignRecordService.selectJwSignRecordListWithUserMatch(teamId, matchId);
            for(JwSignRecord jwSignRecord : jwSignRecords){
                JwGameItem jwGameItem = list.stream().filter(jwGameItem1 -> jwGameItem1.getId().equals(jwSignRecord.getGameItemId())).findFirst().orElse(null);
                if(jwGameItem != null){
                    List<Long> o = jwGameItem.getSportIds();
                    if(o == null) o=new ArrayList<>();
                    List<Long> ids = jwSignRecord.getJwSignRecordSportList().stream().map(JwSignRecordSport::getSportId).collect(Collectors.toList());
                    o.addAll(ids);
                    jwGameItem.setSportIds(o);
                }
            }
        }

        return AjaxResult.success(list);
    }

    @PostMapping("/listMatchItem")
    @ResponseBody
    public AjaxResult listMatchItem() {
        JwMatch jwMatch = new JwMatch();
        jwMatch.setIsShow("Y");
        List<JwMatch> jwMatchList = jwMatchService.selectJwMatchList(jwMatch);
        if(jwMatchList != null && jwMatchList.size() > 0){
            jwMatchList.forEach(jwMatch1 -> jwMatch1.setMatchName(jwMatch1.getMatchName().replaceAll("<br/>", " ")));
            jwMatchList.sort(Comparator.comparing(JwMatch::getBeginTime).reversed());
        }
        return AjaxResult.success(jwMatchList);
    }

    @PostMapping("/listAllMatchItem")
    @ResponseBody
    public AjaxResult listAllMatchItem() {
        JwMatch jwMatch = new JwMatch();
        jwMatch.setIsShowGrade("Y");
        List<JwMatch> jwMatchList = jwMatchService.selectJwMatchList(jwMatch);
        if(jwMatchList != null && jwMatchList.size() > 0){
            jwMatchList.forEach(jwMatch1 -> jwMatch1.setMatchName(jwMatch1.getMatchName().replaceAll("<br/>", " ")));
            jwMatchList.sort(Comparator.comparing(JwMatch::getBeginTime).reversed());
        }
        return AjaxResult.success(jwMatchList);
    }


    @PostMapping("/getMatchItem")
    @ResponseBody
    public AjaxResult getMatchItem(Long id) {
        JwMatch jwMatch = jwMatchService.selectJwMatchById(id);
        jwMatch.setMatchName(jwMatch.getMatchName().replaceAll("<br/>", " "));
        return AjaxResult.success(jwMatch);
    }

    // 获取微信用户管理的比赛
    @PostMapping("/listWxUserMatchList")
    @ResponseBody
    public AjaxResult listWxUserMatchList(@RequestHeader(value = "Authorization", required = false) String openId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if(zwWxUser != null){
            List<JwMatch> jwMatchList = jwMatchService.listWxUserMatchList(zwWxUser.getId());
            if(jwMatchList != null && jwMatchList.size() > 0){
                jwMatchList.forEach(jwMatch1 -> jwMatch1.setMatchName(jwMatch1.getMatchName().replaceAll("<br/>", " ")));
                jwMatchList.sort(Comparator.comparing(JwMatch::getBeginTime).reversed());
            }
            return AjaxResult.success(jwMatchList);
        }
        return AjaxResult.success("拜拜");
    }

    // 获取比赛报名数据
    @PostMapping("/getMatchSignInfo")
    @ResponseBody
    public AjaxResult getMatchSignInfo(@RequestHeader("Authorization") String openId, Long matchId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if(zwWxUser != null && StringUtils.isLongNotNull(matchId)){
            return AjaxResult.success(jwMatchService.getMatchSignInfo(matchId));
        }
        return AjaxResult.success("拜拜");
    }
    // 获取比赛 组别报名统计
    @PostMapping("/listMatchGameItemWithSignCount")
    @ResponseBody
    public AjaxResult listMatchGameItemWithSignCount(@RequestHeader("Authorization") String openId, Long matchId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if(zwWxUser != null && StringUtils.isLongNotNull(matchId)){
            JwGameItem jwGameItem = new JwGameItem();
            jwGameItem.setMatchId(matchId);
            return AjaxResult.success(jwGameItemService.selectJwGameItemListWithCount(jwGameItem));
        }
        return AjaxResult.success("拜拜");
    }

    // 获取比赛 组别报名数据
    @PostMapping("/listMatchGameItemWithSignData")
    @ResponseBody
    public AjaxResult listMatchGameItemWithSignData(@RequestHeader("Authorization") String openId, Long matchId, Long gameItemId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if(zwWxUser != null && StringUtils.isLongNotNull(matchId)){
            return AjaxResult.success(jwSignRecordService.selectJwSignRecordListWithAllInfo(gameItemId, null));
        }
        return AjaxResult.success("拜拜");
    }

    // 获取比赛的全部参赛队伍
    @PostMapping("/listMatchteamInfo")
    @ResponseBody
    public AjaxResult listMatchteamInfo(@RequestHeader("Authorization") String openId, JwTeam jwTeam) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if(zwWxUser != null && StringUtils.isLongNotNull(jwTeam.getMatchId())){
            return AjaxResult.success(jwTeamService.selectJwTeamList(jwTeam));
        }
        return AjaxResult.success("拜拜");
    }

    // 获取比赛一个组别的成绩
    @PostMapping("/listGameItemGradeDes")
    @ResponseBody
    public AjaxResult listGameItemGradeDes(@RequestHeader("Authorization") String openId, JwSignRecord jwSignRecord) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if(zwWxUser != null){
            return AjaxResult.success(jwHaiScoreService.listGameItemGradeDes(jwSignRecord));
        }
        return AjaxResult.success("拜拜");
    }

    // 根据背号查成绩
    @PostMapping("/getGradeByBackNum")
    @ResponseBody
    public AjaxResult getGradeByBackNum(@RequestHeader("Authorization") String openId, JwSignRecord jwSignRecord) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if(zwWxUser != null){
            return AjaxResult.success(jwSignRecordService.selectJwSignRecordHaiScore(jwSignRecord));
        }
        return AjaxResult.success("拜拜");
    }

    @PostMapping("/checkMatchInvitationCode")
    @ResponseBody
    public AjaxResult checkMatchInvitationCode(@RequestHeader("Authorization") String openId, Long matchId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if(zwWxUser != null && StringUtils.isLongNotNull(matchId)){
            JwMatch jwMatch = jwMatchService.selectJwMatchById(matchId);
            if(StringUtils.isNotEmpty(jwMatch.getInvitationList()) && jwMatch.getInvitationList().contains(openId)){
                return  AjaxResult.success(1);
            }else{
                return AjaxResult.success(0);
            }
        }
        return AjaxResult.success("拜拜");
    }

    @PostMapping("/verifyMatchInvitationCode")
    @ResponseBody
    public AjaxResult verifyMatchInvitationCode(@RequestHeader("Authorization") String openId, String invitationCode, Long matchId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if(StringUtils.isEmpty(invitationCode)){
            return AjaxResult.error("请输入邀请码");
        }
        if(zwWxUser != null && StringUtils.isLongNotNull(matchId)){
            JwMatch jwMatch = jwMatchService.selectJwMatchById(matchId);
            if(jwMatch.getInvitationCode().equals(invitationCode)){
                String invitationList = jwMatch.getInvitationList();
                if(StringUtils.isEmpty(invitationList)){
                    invitationList = "";
                }
                invitationList +=  openId + ",";

                return AjaxResult.success(jwMatchService.updateJwMatchInvitationList(matchId, invitationList));
            }else{
                return AjaxResult.success(0);
            }
        }
        return AjaxResult.success("拜拜");
    }

    @PostMapping("/verifyMatchManageCode")
    @ResponseBody
    public AjaxResult verifyMatchManageCode(@RequestHeader("Authorization") String openId, String invitationCode, Long matchId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if(StringUtils.isEmpty(invitationCode)){
            return AjaxResult.error("请输入密码");
        }
        if(zwWxUser != null && StringUtils.isLongNotNull(matchId)){
            JwMatch jwMatch = jwMatchService.selectJwMatchById(matchId);
            if(jwMatch.getManageCode().equals(invitationCode)){
                JwMatchUser jwMatchUser = new JwMatchUser();
                jwMatchUser.setMatchId(matchId);
                jwMatchUser.setUserId(zwWxUser.getId());
                List<JwMatchUser> JwMatchUsers =  jwMatchUserMapper.selectJwMatchUserList(jwMatchUser);
                if(JwMatchUsers == null || JwMatchUsers.size() <= 0){
                    jwMatchUserMapper.insertJwMatchUser(jwMatchUser);
                }
                return AjaxResult.success(1);
            }else{
                return AjaxResult.success(0);
            }
        }
        return AjaxResult.success("拜拜");
    }

    @PostMapping("/deleteMatchTeam")
    @ResponseBody
    public AjaxResult deleteMatchTeam(@RequestHeader("Authorization") String openId, Long teamId, Long matchId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        Assert.notNull(teamId, "拜拜");
        Assert.notNull(matchId, "拜拜");
        Assert.notNull(zwWxUser, "拜拜");

        jwMatchTeamService.deleteJwMatchTeam(teamId, matchId);

        return AjaxResult.success("拜拜");
    }

}