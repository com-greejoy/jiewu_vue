package com.ruoyi.project.jiewu.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.ruoyi.common.utils.BigDecimalUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.jiewu.service.JwGameItemService;
import com.ruoyi.project.jiewu.service.JwMatchService;
import com.ruoyi.project.jiewu.service.JwSignRecordService;
import com.ruoyi.project.jiewu.service.JwWxUserService;
import me.chanjar.weixin.common.error.WxErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mini/match/api")
public class JwMiNiMatchController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(JwMiNiMatchController.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private JwMatchService jwMatchService;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwSignRecordService jwSignRecordService;

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
        List<JwMatch> jwMatchList = jwMatchService.selectJwMatchList(null);

        return AjaxResult.success(jwMatchList);
    }

    @PostMapping("/getMatchItem")
    @ResponseBody
    public AjaxResult getMatchItem(Long id) {
        JwMatch jwMatch = jwMatchService.selectJwMatchById(id);
        return AjaxResult.success(jwMatch);
    }

}