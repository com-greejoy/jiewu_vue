package com.ruoyi.project.jiewu.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;

import cn.hutool.core.lang.Validator;
import com.mchange.lang.LongUtils;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.exception.file.InvalidExtensionException;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.common.utils.file.PdfGenerator;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.jiewu.service.*;
import me.chanjar.weixin.common.annotation.Required;
import me.chanjar.weixin.common.error.WxErrorException;

import net.bytebuddy.implementation.bind.annotation.Default;
import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.util.MapUtil;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/mini/user/api")
public class JwMiNiUserController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(JwMiNiUserController.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private JwWxUserService jwWxUserService;

    @Autowired
    private JwTeamService jwTeamService;

    @Autowired
    private JwSportService jwSportService;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwSignRecordSportService jwSignRecordSportService;

    @Autowired
    private JwHaiScoreService jwHaiScoreService;

    @Autowired
    private JwMatchService jwMatchService;

    @Autowired
    private JwTeamLeaderService jwTeamLeaderService;

    @Autowired
    private JwMatchUserService jwMatchUserService;

    @PostMapping("/auth")
    @ResponseBody
    public AjaxResult auth(String code) {

        WxMaJscode2SessionResult sessionResult = null;
        try {
            sessionResult = wxMaService.jsCode2SessionInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        String openId = sessionResult.getOpenid();
        if (StringUtils.isEmpty(openId)) {
            return AjaxResult.error("授权错误");
        }

//        openId = "op3oK7doMbiZPD2QyiXwyWtPDTLg";

        JwWxUser jwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (jwWxUser == null) {
            jwWxUser = new JwWxUser();
            jwWxUser.setOpenId(openId);
            jwWxUser.setCreateTime(DateUtils.getNowDate());

            jwWxUserService.insertJwWxUser(jwWxUser);
        }

        ServletUtils.getSession().setAttribute("OPENID", jwWxUser.getOpenId());
        return AjaxResult.success(jwWxUser);
    }

    @PostMapping("/phoneSave")
    @ResponseBody
    public AjaxResult phoneSave(String iv, String rawData, String code, @RequestHeader("Authorization") String openId) {

        JwWxUser jwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (jwWxUser != null) {
            WxMaPhoneNumberInfo phoneNumberInfo = null;
            try {
                phoneNumberInfo = wxMaService.getUserService().getNewPhoneNoInfo(code);
            } catch (WxErrorException e) {
                e.printStackTrace();
                return AjaxResult.error("获取手机号失败");
            }

            String phone = phoneNumberInfo.getPhoneNumber();
            jwWxUser.setMobile(phone);

            jwWxUserService.updateJwWxUser(jwWxUser);
            return AjaxResult.success(jwWxUser);
        }
        return AjaxResult.error("获取手机号失败");

    }

    @PostMapping("/getUserInfo")
    @ResponseBody
    public AjaxResult getUserInfo(@RequestHeader("Authorization") String openId) {

        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            return AjaxResult.success(zwWxUser);
        } else {
            return AjaxResult.error("错误");
        }
    }

    @PostMapping("/wxsaveuser")
    @ResponseBody
    public AjaxResult wxsaveuser(JwWxUser jwWxUser, @RequestHeader("Authorization") String openId) throws Exception {
        if (StringUtils.isEmpty(openId)) {
            return AjaxResult.error("请从微信小程序中访问");
        }

        jwWxUserService.updateJwWxUser(jwWxUser);
        return AjaxResult.success(jwWxUserService.selectJwWxUserById(jwWxUser.getId()));
    }

    @PostMapping("/upLoadFile")
    @ResponseBody
    public AjaxResult upLoadFile(@RequestHeader("Authorization") String openId, MultipartFile file) {
        JwWxUser jwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (jwWxUser == null) return AjaxResult.error("错误");
        String avatar = "";
        try {
            avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("错误");
        }
        return AjaxResult.success(avatar);
    }

    @PostMapping("/createTeam")
    @ResponseBody
    public AjaxResult createTeam(@RequestHeader("Authorization") String openId, @Validated JwTeam jwTeam) {

        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {

            if (!Validator.isMobile(jwTeam.getUserPhone())) {
                return AjaxResult.error("手机号格式错误");
            }

            if (StringUtils.isLongNotNull(jwTeam.getId())) {
                jwTeamService.updateJwTeam(jwTeam);
            } else {
                jwTeam.setCreateUserId(zwWxUser.getId());
                jwTeamService.insertJwTeam(jwTeam);
            }
            return AjaxResult.success(jwTeam);
        } else {
            return AjaxResult.error("错误");
        }
    }

    @GetMapping("/getMatchTeamInviteErCode")
    public void getMatchTeamInviteErCode(HttpServletResponse response, Long matchId, Long teamId) {

        try {
            //• "develop" 开发版
            //• "trial" 体验版
            //• "release"
            byte[] wordBytes = wxMaService.getQrcodeService().createWxaCodeUnlimitBytes("inviteTeamId="+teamId+"&matchId=" + matchId,
                    "pages/sign/signMatchGameItemSportSuiNingInvite/signMatchGameItemSportSuiNingInvite",
                    false,
                    "release",
                    600,
                    false, new WxMaCodeLineColor("255", "153", "51"), false);
            if (wordBytes == null || wordBytes.length == 0) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "生成二维码失败");
                return;
            }
            response.setContentType("image/png");
            response.setContentLength(wordBytes.length);
            try (ServletOutputStream out = response.getOutputStream()) {
                out.write(wordBytes);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PostMapping("/getWxUserTeam")
    @ResponseBody
    public AjaxResult getWxUserTeam(@RequestHeader("Authorization") String openId, Long teamId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            if(StringUtils.isLongNotNull(teamId)){
                return AjaxResult.success(jwTeamService.selectJwTeamById(teamId));
            }else{
                return AjaxResult.success(jwTeamService.selectJwTeamByUserId(zwWxUser.getId()));
            }
        } else {
            return AjaxResult.error("错误");
        }
    }


    @PostMapping("/createSportInvite")
    @ResponseBody
    public AjaxResult createSportInvite(@RequestHeader("Authorization") String openId, @Validated JwSport jwSport) {

        if (!IDCardUtils.isIdCard(jwSport.getIdCard())) {
            return AjaxResult.error("身份证格式错误");
        }

        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {

//            JwSport jwSport1 = jwSportService.selectJwSportByIdCard(jwSport.getIdCard(), jwSport.getId(), jwSport.getCreateUserId());
//            if (jwSport1 != null) {
////                return AjaxResult.error("身份证已经存在");
//            }

            if (StringUtils.isLongNotNull(jwSport.getId())) {
                jwSport.setCreateAddId(zwWxUser.getId());
                jwSportService.updateJwSport(jwSport);
            } else {
//                jwSport.setCreateUserId(zwWxUser.getId());
                jwSport.setCreateAddId(zwWxUser.getId());
                jwSportService.insertJwSport(jwSport);
            }
            return AjaxResult.success(jwSport);
        } else {
            return AjaxResult.error("错误");
        }
    }

    @PostMapping("/createSport")
    @ResponseBody
    public AjaxResult createSport(@RequestHeader("Authorization") String openId, @Validated JwSport jwSport) {

        if (!IDCardUtils.isIdCard(jwSport.getIdCard())) {
            return AjaxResult.error("身份证格式错误");
        }

        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {

            JwSport jwSport1 = jwSportService.selectJwSportByIdCard(jwSport.getIdCard(), jwSport.getId(), zwWxUser.getId());
            if (jwSport1 != null) {
                return AjaxResult.error("身份证已经存在");
            }

            if (StringUtils.isLongNotNull(jwSport.getId())) {
                jwSportService.updateJwSport(jwSport);
            } else {
                jwSport.setCreateUserId(zwWxUser.getId());
                jwSportService.insertJwSport(jwSport);
            }
            return AjaxResult.success(jwSport);
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 删除选手
    @PostMapping("/deleteSport")
    @ResponseBody
    public AjaxResult deleteSport(@RequestHeader("Authorization") String openId, Long id) {

        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {

            // 删除选手的时候，要判断他有没有报名，如果有报名，就把他的报名数据一起删除了
            List<JwSignRecordSport> jwSignRecordSportList = jwSignRecordSportService.selectJwSignRecordSportBySportId(id);
            if (jwSignRecordSportList != null && jwSignRecordSportList.size() > 0) {
                for (JwSignRecordSport jwSignRecordSport : jwSignRecordSportList) {
                    // 如果是单人报名，就直接把报名记录一起删了
                    jwSignRecordSportService.deleteJwSignRecordSportById(jwSignRecordSport.getId());
                }
            }
            if (StringUtils.isLongNotNull(id)) {
                jwSportService.deleteJwSportById(id);
            }
            return AjaxResult.success(1);
        } else {
            return AjaxResult.error("错误");
        }
    }

    @PostMapping("/getWxSportList")
    @ResponseBody
    public AjaxResult getWxSportList(@RequestHeader("Authorization") String openId, String searchName,
                                     @RequestParam(defaultValue = "false") Boolean createAddId,
                                     Long createUserId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            if(createAddId){

                JwSport jwSport = new JwSport();
                jwSport.setCreateAddId(zwWxUser.getId());
                jwSport.setPlayerName(searchName);
                jwSport.setCreateUserId(createUserId);

                List<JwSport> jwSportList = jwSportService. selectJwSportList(jwSport);
                return AjaxResult.success(jwSportList);
            }else{
                List<JwSport> jwSportList = jwSportService.selectJwSportByUserId(zwWxUser.getId(), searchName);
                return AjaxResult.success(jwSportList);
            }
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 获取运动员 没有报名该项目的
    @PostMapping("/getWxSportListWithGameItem")
    @ResponseBody
    public AjaxResult getWxSportListWithGameItem(@RequestHeader("Authorization") String openId, Long teamId, Long gameItemId, Long editId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {

            JwTeam jwTeam = null;
            if(StringUtils.isLongNotNull(teamId)){
                jwTeam = jwTeamService.selectJwTeamById(teamId);
            }

            JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(gameItemId);

            // 过滤年龄
            Long minAge = StringUtils.isLongNotNull(jwGameItem.getMinYear()) ? jwGameItem.getMinYear() : 0l;
            Long maxAge = StringUtils.isLongNotNull(jwGameItem.getMaxYear()) ? jwGameItem.getMaxYear() : 99l;

            // 过滤性别
            String sex = "";
            if ("m".equals(jwGameItem.getSexCon()) || "f".equals(jwGameItem.getSexCon())) {
                sex = jwGameItem.getSexCon();
            }

            List<JwSport> jwSportList = jwSportService.getWxSportListWithGameItem(jwTeam != null ? jwTeam.getCreateUserId() : zwWxUser.getId(), gameItemId, minAge, maxAge, sex);

            // 修改多人报名
            if (StringUtils.isLongNotNull(editId)) {

                List<JwSport> jwSignRecordSportList = jwSportService.getSportListWithsignRecord(editId);
                if (jwSignRecordSportList != null && jwSignRecordSportList.size() >= 0) {
                    jwSignRecordSportList.forEach(jwSport -> jwSport.setSelect(true));
                    jwSignRecordSportList.addAll(jwSportList);
                    jwSportList = jwSignRecordSportList;
                }
            }

            return AjaxResult.success(jwSportList);
        } else {
            return AjaxResult.error("错误");
        }
    }

    @PostMapping("/saveSign")
    @ResponseBody
    public AjaxResult saveSign(@RequestHeader("Authorization") String openId, Long[] sportIds, Long[] gameItemIds, Long teamId, Long editId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            for (Long gameItemId : gameItemIds) {
                JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(gameItemId);


                // 判断报名时间过没过
                JwMatch jwMatch = jwMatchService.selectJwMatchById(jwGameItem.getMatchId());

                // 如果是管理员就不需要判断时间
                if(!jwMatchUserService.checkIfManagerMatch(zwWxUser.getId(), jwGameItem.getMatchId())){

                    Date now = DateUtils.getNowDate();
                    if (now.after(jwMatch.getSignEndTime())) {
                        return AjaxResult.error("报名已结束");
                    }
                    if (now.before(jwMatch.getSignBeginTime())) {
                        return AjaxResult.error("报名未开始");
                    }
                }

                jwSignRecordService.saveSign(jwGameItem, sportIds, teamId, editId, null, zwWxUser.getId());
            }

            return AjaxResult.success();
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 获取组别的报名名单
    @PostMapping("/getWxGameItemSignRecord")
    @ResponseBody
    public AjaxResult getWxGameItemSignRecord(@RequestHeader("Authorization") String openId, Long teamId, Long gameItemId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            List<JwSignRecord> jwSignRecords = jwSignRecordService.selectJwSignRecordListWithUserGameItem(teamId, gameItemId, null);
            return AjaxResult.success(jwSignRecords);
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 删除单人报名
    @PostMapping("/deleteSignRecord")
    @ResponseBody
    public AjaxResult deleteSignRecord(@RequestHeader("Authorization") String openId, Long id) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {

            // 判断报名时间过没过
            JwMatch jwMatch = jwMatchService.selectJwMatchById(jwSignRecordService.selectJwSignRecordById(id).getMatchId());
            if(!jwMatchUserService.checkIfManagerMatch(zwWxUser.getId(), jwMatch.getId())){
                Date now = DateUtils.getNowDate();
                if (now.after(jwMatch.getSignEndTime())) {
                    return AjaxResult.error("报名已结束");
                }
                if (now.before(jwMatch.getSignBeginTime())) {
                    return AjaxResult.error("报名未开始");
                }
            }
            return AjaxResult.success(jwSignRecordService.deleteJwSignRecordById(id));
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 获取代表队报名统计
    @PostMapping("/getSignCount")
    @ResponseBody
    public AjaxResult getSignCount(@RequestHeader("Authorization") String openId, Long matchId, Long teamId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            Map<String, Object> result = new HashMap<>();

            // 人数
            Long sportNum = jwSignRecordService.selectJwSignRecordSportNum(teamId, matchId);

            // 人次
            Long sportTime = jwSignRecordService.selectJwSignRecordSportTime(teamId, matchId);

            // 费用
            BigDecimal allFee = new BigDecimal("0");
            List<JwSignRecord> jwSignRecords = jwSignRecordService.selectJwSignRecordListWithUserMatch(teamId, matchId);
            if (jwSignRecords != null && jwSignRecords.size() > 0) {
                for (JwSignRecord jwSignRecord : jwSignRecords) {
                    JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(jwSignRecord.getGameItemId());
                    if (BigDecimalUtil.isNotNull(jwGameItem.getFee())) {

                        allFee = allFee.add(jwTeamService.getSignRecordFee(jwGameItem, jwSignRecord));

//                        if("1".equals(jwGameItem.getSportLimit())){
//                            //  单人
//                            allFee = allFee.add(jwGameItem.getFee());
//                        }else if("3".equals(jwGameItem.getSportLimit())){
//                            // 多人
//                            if(jwSignRecord.getJwSignRecordSportList() != null && jwSignRecord.getJwSignRecordSportList().size() > 0){
//                                int sportCount = jwSignRecord.getJwSignRecordSportList().size();
//                                BigDecimal fee = jwGameItem.getFee();
//                                if(BigDecimalUtil.isNotNull(fee)){
//                                    // 如果报名人数超过规定人数, 就重新计算价格   价格 / 规定人数 * 实际人数
//                                    if(sportCount > jwGameItem.getFeeMaxSport()){
//                                        fee = fee.multiply(new BigDecimal(sportCount)).divide(new BigDecimal(jwGameItem.getFeeMaxSport()), 0, RoundingMode.DOWN);
//                                    }
//                                    allFee = allFee.add(fee);
//                                }
//                            }
//                        }
                    }
                }
            }

            result.put("sportNum", sportNum);
            result.put("sportTime", sportTime);
            result.put("allFee", allFee);
            return AjaxResult.success(result);
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 获取总费用
    @PostMapping("/getTeamFee")
    @ResponseBody
    public AjaxResult getTeamFee(@RequestHeader("Authorization") String openId, Long matchId, Long teamId, @RequestParam(defaultValue = "false") Boolean onlyInvite) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            List<JwSignRecord> jwSignRecords = jwSignRecordService.selectJwSignRecordListWithUserMatch(teamId, matchId);

            // 如果是被邀请的人来查，就只查他自己数据
            if(onlyInvite){
                jwSignRecords = jwSignRecords.stream().filter(jwSignRecord -> zwWxUser.getId().equals(jwSignRecord.getCreateAddId())).collect(Collectors.toList());
            }

            List<JwSport> jwSportList = new ArrayList<>();

            if (jwSignRecords != null && jwSignRecords.size() > 0) {
                for (JwSignRecord jwSignRecord : jwSignRecords) {
                    JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(jwSignRecord.getGameItemId());
                    jwSignRecord.setJwGameItem(jwGameItem);
//                    if(BigDecimalUtil.isNotNull(jwGameItem.getFee())){
                    if ("1".equals(jwGameItem.getSportLimit())) {
                        //  单人
                        jwSignRecord.setFee(jwGameItem.getFee());

                        Long sportId = jwSignRecord.getJwSignRecordSportList().get(0).getSportId();

                        List<JwSport> finalJwSportList = jwSportList;
                        int index = IntStream.range(0, jwSportList.size())
                                .filter(i -> sportId.equals(finalJwSportList.get(i).getId()))
                                .findFirst()
                                .orElse(-1);
                        JwSport jwSport = null;
                        if (index >= 0) {
                            jwSport = jwSportList.get(index);
                        } else {
                            jwSport = jwSportService.selectJwSportById(sportId);
                        }

                        List<JwGameItem> jwGameItems = jwSport.getJwGameItemList();
                        if (jwGameItems == null) {
                            jwGameItems = new ArrayList<>();
                        }
                        jwGameItems.add(jwGameItem);
                        jwSport.setJwGameItemList(jwGameItems);
                        if (index >= 0) {
                            jwSportList.set(index, jwSport);
                        } else {
                            jwSportList.add(jwSport);
                        }

                    } else if ("5".equals(jwGameItem.getSportLimit()) || "3".equals(jwGameItem.getSportLimit()) || "4".equals(jwGameItem.getSportLimit()) || "2".equals(jwGameItem.getSportLimit())) {

                        // 多人
                        if (jwSignRecord.getJwSignRecordSportList() != null && jwSignRecord.getJwSignRecordSportList().size() > 0) {
                            int sportCount = jwSignRecord.getJwSignRecordSportList().size();
                            BigDecimal fee = jwGameItem.getFee();

                            fee = jwTeamService.getSignRecordFee(jwGameItem, jwSignRecord);

                            // 如果报名人数超过规定人数, 就重新计算价格   价格 / 规定人数 * 实际人数
//                                if(sportCount > jwGameItem.getFeeMaxSport() && BigDecimalUtil.isNotNull(fee)){
//                                    fee = fee.multiply(new BigDecimal(sportCount)).divide(new BigDecimal(jwGameItem.getFeeMaxSport()), 0, RoundingMode.DOWN);
//                                }
                            jwSignRecord.setFee(fee);

                            BigDecimal avgfee = jwSignRecord.getFee().divide(new BigDecimal(sportCount), 0, RoundingMode.DOWN);
                            for (JwSignRecordSport jwSignRecordSport : jwSignRecord.getJwSignRecordSportList()) {

                                Long sportId = jwSignRecordSport.getSportId();

                                List<JwSport> finalJwSportList = jwSportList;
                                int index = IntStream.range(0, jwSportList.size())
                                        .filter(i -> sportId.equals(finalJwSportList.get(i).getId()))
                                        .findFirst()
                                        .orElse(-1);
                                JwSport jwSport = null;
                                if (index >= 0) {
                                    jwSport = jwSportList.get(index);
                                } else {
                                    jwSport = jwSportService.selectJwSportById(sportId);
                                }

                                List<JwGameItem> jwGameItems = jwSport.getJwGameItemList();
                                if (jwGameItems == null) {
                                    jwGameItems = new ArrayList<>();
                                }
                                jwGameItem.setFee(avgfee);
                                jwGameItems.add(jwGameItem);

                                jwSport.setJwGameItemList(jwGameItems);
                                if (index >= 0) {
                                    jwSportList.set(index, jwSport);
                                } else {
                                    jwSportList.add(jwSport);
                                }
                            }
                        }

                    }
//                    }
                }
            }
            Map<String, Object> result = new HashMap();
            result.put("jwSportList", jwSportList);
            result.put("jwSignRecords", jwSignRecords);
            return AjaxResult.success(result);
        } else {
            return AjaxResult.error("错误");
        }
    }

    @PostMapping("/listMatchGameItemSignRecord")
    @ResponseBody
    public AjaxResult listMatchGameItemSignRecord(@RequestHeader("Authorization") String openId, Long matchId, Long teamId, @RequestParam(defaultValue = "false") Boolean onlyInvite) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            // 获取所有运动员的报名项目
            List<JwSport> jwSportList = new ArrayList<>();
            if(onlyInvite){
               jwSportList = jwSportService.selectJwSignRecordSportGameItemList(matchId, teamId, zwWxUser.getId());
            }else{
               jwSportList = jwSportService.selectJwSignRecordSportGameItemList(matchId, teamId, null);
            }
            return AjaxResult.success(jwSportList);
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 获取组别的报名名单
    @PostMapping("/listMatchGameItemSport")
    @ResponseBody
    public AjaxResult listMatchGameItemSport(@RequestHeader("Authorization") String openId, Long matchId, Long teamId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {

            List<JwGameItem> list = jwGameItemService.selectJwGameItemListByMatchId(matchId, null);

            list.forEach(jwGameItem -> {
                jwGameItem.setSportList(jwSignRecordService.selectJwSignRecordListWithUserGameItem(teamId, jwGameItem.getId(), null));
            });
            list = list.stream().filter(jwGameItem -> jwGameItem.getSportList() != null && jwGameItem.getSportList().size() > 0).collect(Collectors.toList());
            return AjaxResult.success(list);
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 获取齐舞的报名记录
    @PostMapping("/listMatchQiWuSignRecord")
    @ResponseBody
    public AjaxResult listMatchQiWuSignRecord(@RequestHeader("Authorization") String openId, Long matchId, Long teamId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            List<JwSignRecord> jwSignRecords = jwSignRecordService.listMatchQiWuSignRecord(teamId, matchId);
            return AjaxResult.success(jwSignRecords);
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 获取所有的齐舞的报名记录
    @PostMapping("/listMatchQiWuSignRecordAll")
    @ResponseBody
    public AjaxResult listMatchQiWuSignRecordAll(@RequestHeader("Authorization") String openId, Long matchId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            List<JwSignRecord> jwSignRecords = jwSignRecordService.listMatchQiWuSignRecord(null, matchId);
            if (jwSignRecords != null && jwSignRecords.size() > 0) {
                jwSignRecords.sort(Comparator.comparing(JwSignRecord::getTeamName));
            }
            return AjaxResult.success(jwSignRecords);
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 获取代表队赛程
    @PostMapping("/getTeamSchedule")
    @ResponseBody
    public AjaxResult getTeamSchedule(@RequestHeader("Authorization") String openId, Long matchId, Long teamId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            return AjaxResult.success(jwSignRecordService.getTeamScheduleInfoList(matchId, teamId));
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 修改齐舞作品名称
    @PostMapping("/updateQiWuWorksName")
    @ResponseBody
    public AjaxResult updateQiWuWorksName(@RequestHeader("Authorization") String openId, JwSignRecord jwSignRecord) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            return AjaxResult.success(jwSignRecordService.updateJwSignRecord(jwSignRecord));
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 获取代表队成绩
    @PostMapping("/getTeamGrade")
    @ResponseBody
    public AjaxResult getTeamGrade(@RequestHeader("Authorization") String openId, JwSignRecord jwSignRecord) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            List<JwMatchTeamGrade> gradeList = jwHaiScoreService.listTeamGradeDes(jwSignRecord);
            return AjaxResult.success(gradeList != null && gradeList.size() > 0 ? gradeList.get(0) : null);
        } else {
            return AjaxResult.error("错误");
        }
    }

    @PostMapping("/upLoadMusic")
    @ResponseBody
    public AjaxResult upLoadMusic(@RequestHeader("Authorization") String openId, MultipartFile file, Long jwSignRecordId, String fileName) {
        JwWxUser jwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (jwWxUser == null) return AjaxResult.error("错误");
        String avatar = "";
        try {
            avatar = FileUploadUtils.uploadMatch(RuoYiConfig.getMatchPath() + jwSignRecordService.selectJwSignRecordById(jwSignRecordId).getMatchId(), file, MimeTypeUtils.MEDIA_EXTENSION);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("错误");
        }
        JwSignRecord jwSignRecord = new JwSignRecord();
        jwSignRecord.setId(jwSignRecordId);
        jwSignRecord.setWorksMusic(avatar);
        jwSignRecord.setWorksMusicName(fileName);
        jwSignRecordService.updateJwSignRecord(jwSignRecord);
        return AjaxResult.success(avatar);
    }

    @PostMapping("/upLoadSport")
    @ResponseBody
    public AjaxResult upLoadSport(@RequestHeader("Authorization") String openId, MultipartFile file) {
        JwWxUser jwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (jwWxUser == null) return AjaxResult.error("错误");

        ExcelUtil<JwSportWxImport> util = new ExcelUtil<>(JwSportWxImport.class);
        List<JwSportWxImport> userList = null;
        try {
            userList = util.importExcel(file.getInputStream());

            for (JwSportWxImport jwSport : userList) {
                jwSport.setIdCard(StringUtils.trim(jwSport.getIdCard()));
                jwSport.setPlayerName(StringUtils.trim(jwSport.getPlayerName()));
                List<JwTeam> jwTeams = jwTeamService.selectJwTeamByUserId(jwWxUser.getId());
                if(jwTeams != null && jwTeams.size() > 0){
                    JwTeam jwTeam = jwTeams.get(0);

                    JwSport jwSport1 = jwSportService.selectJwSportByName(null, jwSport.getIdCard(), jwTeam.getCreateUserId());
                    if (jwSport1 == null) {
                        JwSport up = new JwSport();
                        up.setPlayerName(jwSport.getPlayerName());
                        up.setIdCard(jwSport.getIdCard());
                        up.setCreateUserId(jwTeam.getCreateUserId());
                        jwSportService.insertJwSport(up);
                    } else {
                        JwSport up = new JwSport();
                        up.setPlayerName(jwSport.getPlayerName());
                        up.setIdCard(jwSport.getIdCard());
                        up.setId(jwSport1.getId());
                        jwSportService.updateJwSport(up);
                    }
                }else{
                    throw new GlobalException("没有队伍");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return AjaxResult.success();
    }


    // 获取 队伍人员
    @PostMapping("/getWxLeaderList")
    @ResponseBody
    public AjaxResult getWxLeaderList(@RequestHeader("Authorization") String openId, String searchName, Long matchId) {
        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {
            List<JwTeamLeader> jwTeamLeaderList = jwTeamLeaderService.selectJwTeamLeaderListByWxUser(zwWxUser.getId(), searchName, matchId);

            return AjaxResult.success(jwTeamLeaderList);
        } else {
            return AjaxResult.error("错误");
        }
    }

    // 添加 队伍人员
    @PostMapping("/addWxLeader")
    @ResponseBody
    public AjaxResult addWxLeader(@RequestHeader("Authorization") String openId, @Validated JwTeamLeader jwTeamLeader) {

        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {

            if (!Validator.isMobile(jwTeamLeader.getLeaderPhone())) {
                return AjaxResult.error("手机号格式错误");
            }

            List<JwTeamLeader> jwTeamLeaderList = jwTeamLeaderService.selectJwTeamLeaderListByNamePhone(jwTeamLeader);
            if (jwTeamLeaderList != null && jwTeamLeaderList.size() >= 1) {
                return AjaxResult.error("人员已经存在");
            }

            if (StringUtils.isLongNotNull(jwTeamLeader.getId())) {
                jwTeamLeaderService.updateJwTeamLeader(jwTeamLeader);
            } else {
                jwTeamLeader.setCreateUserId(zwWxUser.getId());
                jwTeamLeaderService.insertJwTeamLeader(jwTeamLeader);
            }
            return AjaxResult.success(jwTeamLeader);
        } else {
            return AjaxResult.error("错误");
        }
    }


    // 删除 队伍人员
    @PostMapping("/delWxLeader")
    @ResponseBody
    public AjaxResult delWxLeader(@RequestHeader("Authorization") String openId, Long id) {

        JwWxUser zwWxUser = jwWxUserService.selectZwWxUserByOpenId(openId);
        if (zwWxUser != null) {

            if (StringUtils.isLongNotNull(id)) {
                jwTeamLeaderService.deleteJwTeamLeaderById(id);
            }
            return AjaxResult.success(1);
        } else {
            return AjaxResult.error("错误");
        }
    }

    @GetMapping("/getTeamFeeDownload")
    public void getTeamFeeDownload(Long matchId, Long teamId, HttpServletResponse response) {
        //按选手查看
        List<JwSport> jwSportList = new ArrayList<>();
        // 全部报名记录
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.genJwSignRecords(teamId, matchId, jwSportList);

        try {
            byte[] wordBytes = PdfGenerator.generateFeeWord(jwSignRecordList, jwMatchService.selectJwMatchById(matchId), jwTeamService.selectJwTeamById(teamId));
            ServletUtils.downloadFile(response, wordBytes, jwTeamService.selectJwTeamById(teamId).getTeamName() + "_收费通知单" +  ".docx");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/getTeamScheduleDownload")
    public void getTeamScheduleInfoListDownload(Long matchId, Long teamId, HttpServletResponse response) {
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.getTeamScheduleInfoList(matchId, teamId);
        try {
            // 调用服务生成Word文档并返回字节数组
            byte[] wordBytes = PdfGenerator.generateWord(jwSignRecordList, jwMatchService.selectJwMatchById(matchId).getMatchName(), jwTeamService.selectJwTeamById(teamId), "1");
            ServletUtils.downloadFile(response, wordBytes, jwTeamService.selectJwTeamById(teamId).getTeamName() + "_赛程表" +  ".docx");

        } catch (IOException e) {
            // 处理异常情况
            e.printStackTrace();
        }
    }
}