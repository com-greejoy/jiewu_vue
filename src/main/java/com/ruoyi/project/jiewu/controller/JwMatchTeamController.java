package com.ruoyi.project.jiewu.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.BigDecimalUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.jiewu.service.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwMatchTeam")
public class JwMatchTeamController extends BaseController {

    @Autowired
    private JwMatchTeamService jwMatchTeamService;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwSportService jwSportService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchTeam:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwMatchTeam jwMatchTeam) {
        startPage();
        List<JwMatchTeam> list = jwMatchTeamService.selectJwMatchTeamList(jwMatchTeam);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchTeam:export')")
    @Log(title = "比赛参赛的队伍", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwMatchTeam jwMatchTeam) {
        List<JwMatchTeam> list = jwMatchTeamService.selectJwMatchTeamList(jwMatchTeam);
        ExcelUtil<JwMatchTeam> util = new ExcelUtil<JwMatchTeam>(JwMatchTeam.class);
        util.exportExcel(response, list, "比赛参赛的队伍数据");
    }

    // 获取总费用
    @PostMapping("/getTeamFee")
    @ResponseBody
    public AjaxResult getTeamFee(Long matchId, Long teamId) {

        // 按组别查看
        List<JwGameItem> jwGameItemList = jwGameItemService.selectJwGameItemListWithTeamSignRecordByMatchId(matchId, null, teamId, null);

        //按选手查看
        List<JwSport> jwSportList = new ArrayList<>();

        // 全部报名记录
        List<JwSignRecord> jwSignRecords = jwSignRecordService.selectJwSignRecordListWithUserMatch(teamId, matchId);

        if (jwSignRecords != null && jwSignRecords.size() > 0) {
            for (JwSignRecord jwSignRecord : jwSignRecords) {
                JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(jwSignRecord.getGameItemId());
                jwSignRecord.setJwGameItem(jwGameItem);
                if ("1".equals(jwGameItem.getSportLimit())) {
                    //  单人
                    jwSignRecord.setFee(jwGameItem.getFee());

                    // 按选手查看
                    Long sportId = jwSignRecord.getJwSignRecordSportList().get(0).getSportId();
                    List<JwSport> finalJwSportList = jwSportList;
                    int index = IntStream.range(0, jwSportList.size())
                            .filter(i -> sportId.equals(finalJwSportList.get(i).getId()))
                            .findFirst()
                            .orElse(-1);
                    JwSport jwSport = null;
                    if(index >= 0){
                        jwSport = jwSportList.get(index);
                    }else{
                        jwSport = jwSportService.selectJwSportById(sportId);
                    }

                    List<JwGameItem> jwGameItems = jwSport.getJwGameItemList();
                    if(jwGameItems == null){
                        jwGameItems = new ArrayList<>();
                    }
                    jwGameItems.add(jwGameItem);
                    jwSport.setJwGameItemList(jwGameItems);
                    if(index >= 0){
                        jwSportList.set(index, jwSport);
                    }else{
                        jwSportList.add(jwSport);
                    }

                } else if ("3".equals(jwGameItem.getSportLimit())) {
                    // 多人
                    if (jwSignRecord.getJwSignRecordSportList() != null && jwSignRecord.getJwSignRecordSportList().size() > 0) {
                        int sportCount = jwSignRecord.getJwSignRecordSportList().size();
                        BigDecimal fee = jwGameItem.getFee();
                        // 如果报名人数超过规定人数, 就重新计算价格   价格 / 规定人数 * 实际人数
                        if (sportCount > jwGameItem.getFeeMaxSport() && BigDecimalUtil.isNotNull(fee)) {
                            fee = fee.multiply(new BigDecimal(sportCount)).divide(new BigDecimal(jwGameItem.getFeeMaxSport()), 0, RoundingMode.DOWN);
                        }
                        jwSignRecord.setFee(fee);

                        BigDecimal avgfee = jwSignRecord.getFee().divide(new BigDecimal(sportCount), 0, RoundingMode.DOWN);
                        jwSignRecord.setAvgFee(avgfee);

                        for(JwSignRecordSport jwSignRecordSport : jwSignRecord.getJwSignRecordSportList()){

                            Long sportId = jwSignRecordSport.getSportId();

                            List<JwSport> finalJwSportList = jwSportList;
                            int index = IntStream.range(0, jwSportList.size())
                                    .filter(i -> sportId.equals(finalJwSportList.get(i).getId()))
                                    .findFirst()
                                    .orElse(-1);
                            JwSport jwSport = null;
                            if(index >= 0){
                                jwSport = jwSportList.get(index);
                            }else{
                                jwSport = jwSportService.selectJwSportById(sportId);
                            }

                            List<JwGameItem> jwGameItems = jwSport.getJwGameItemList();
                            if(jwGameItems == null){
                                jwGameItems = new ArrayList<>();
                            }
                            jwGameItem.setFee(avgfee);
                            jwGameItems.add(jwGameItem);

                            jwSport.setJwGameItemList(jwGameItems);
                            if(index >= 0){
                                jwSportList.set(index, jwSport);
                            }else{
                                jwSportList.add(jwSport);
                            }
                        }
                    }
                }
            }
            jwSignRecords.sort(Comparator.comparing(s -> s.getJwGameItem().getCode()));
        }

        Map<String, Object> result = new HashMap();
        result.put("jwGameItemList", jwGameItemList);
        result.put("jwSignRecordList", jwSignRecords);
        result.put("jwSportList", jwSportList);
        return AjaxResult.success(result);
    }

    // 获取代表队赛程
    @PostMapping("/getTeamScheduleInfoList")
    @ResponseBody
    public AjaxResult getTeamScheduleInfoList(Long matchId, Long teamId) {
        return AjaxResult.success(jwSignRecordService.getTeamScheduleInfoList(matchId, teamId));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchTeam:query')")
    @GetMapping(value = "/{teamId}")
    public AjaxResult getInfo(@PathVariable("teamId") Long teamId) {
        return success(jwMatchTeamService.selectJwMatchTeamByTeamId(teamId));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchTeam:add')")
    @Log(title = "比赛参赛的队伍", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwMatchTeam jwMatchTeam) {
        JwMatchTeam jwMatchTeamOld = jwMatchTeamService.getJwMatchTeam(jwMatchTeam.getMatchId(), jwMatchTeam.getTeamId());
        if (jwMatchTeamOld == null) {
            Long lastOrder = jwMatchTeamService.getLastOrder(jwMatchTeam.getMatchId());
            if (!StringUtils.isLongNotNull(lastOrder)) {
                jwMatchTeam.setIndexOrder(1l);
            } else {
                jwMatchTeam.setIndexOrder(lastOrder + 1);
            }
            jwMatchTeamService.insertJwMatchTeam(jwMatchTeam);
        }
        return toAjax(1);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchTeam:edit')")
    @Log(title = "比赛参赛的队伍", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwMatchTeam jwMatchTeam) {
        return toAjax(jwMatchTeamService.updateJwMatchTeam(jwMatchTeam));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchTeam:remove')")
    @Log(title = "比赛参赛的队伍", businessType = BusinessType.DELETE)
    @DeleteMapping("/{teamIds}")
    public AjaxResult remove(@PathVariable Long[] teamIds) {
        return toAjax(jwMatchTeamService.deleteJwMatchTeamByTeamIds(teamIds));
    }
}
