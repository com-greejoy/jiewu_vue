package com.ruoyi.project.jiewu.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.BigDecimalUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.PdfGenerator;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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

    @Autowired
    private JwTeamService jwTeamService;

    @Autowired
    private JwMatchService jwMatchService;

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
    public void export(HttpServletResponse response, JwTeam jwTeam) {
//        List<JwMatchTeam> list = jwMatchTeamService.selectJwMatchTeamList(jwMatchTeam);

        List<JwTeam> list = jwTeamService.selectJwTeamList(jwTeam);
        ExcelUtil<JwTeam> util = new ExcelUtil<JwTeam>(JwTeam.class);
        util.exportExcel(response, list, "比赛参赛的队伍数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchTeam:export')")
    @Log(title = "比赛参赛的队伍", businessType = BusinessType.EXPORT)
    @PostMapping("/exportTeamBackNum")
    public void exportTeamBackNum(HttpServletResponse response, JwTeam jwTeam) {
        List<JwTeam> list = jwTeamService.selectJwTeamList(jwTeam);

        if (list != null && list.size() > 0) {
            list.forEach(jwTeam1 -> {

            });
        }
        ExcelUtil<JwTeam> util = new ExcelUtil<JwTeam>(JwTeam.class);
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
        List<JwSignRecord> jwSignRecords = genJwSignRecords(teamId, matchId, jwSportList);

        Map<String, Object> result = new HashMap();
        result.put("jwGameItemList", jwGameItemList);
        result.put("jwSignRecordList", jwSignRecords);
        result.put("jwSportList", jwSportList);
        return AjaxResult.success(result);
    }

    private List<JwSignRecord> genJwSignRecords(Long teamId, Long matchId, List<JwSport> jwSportList) {

        return jwSignRecordService.genJwSignRecords(teamId, matchId, jwSportList);
//        List<JwSignRecord> jwSignRecords = jwSignRecordService.selectJwSignRecordListWithUserMatch(teamId, matchId);
//
//        if (jwSignRecords != null && jwSignRecords.size() > 0) {
//            for (JwSignRecord jwSignRecord : jwSignRecords) {
//                JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(jwSignRecord.getGameItemId());
//                jwSignRecord.setJwGameItem(jwGameItem);
//                if ("1".equals(jwGameItem.getSportLimit())) {
//                    //  单人
//                    if (BigDecimalUtil.isNotNull(jwGameItem.getFee())) jwSignRecord.setFee(jwGameItem.getFee());
//
//                    // 按选手查看
//                    Long sportId = jwSignRecord.getJwSignRecordSportList().get(0).getSportId();
//                    List<JwSport> finalJwSportList = jwSportList;
//                    int index = IntStream.range(0, jwSportList.size())
//                            .filter(i -> sportId.equals(finalJwSportList.get(i).getId()))
//                            .findFirst()
//                            .orElse(-1);
//                    JwSport jwSport = null;
//                    if (index >= 0) {
//                        jwSport = jwSportList.get(index);
//                    } else {
//                        jwSport = jwSportService.selectJwSportById(sportId);
//                    }
//
//                    List<JwGameItem> jwGameItems = jwSport.getJwGameItemList();
//                    if (jwGameItems == null) {
//                        jwGameItems = new ArrayList<>();
//                    }
//                    jwGameItems.add(jwGameItem);
//                    jwSport.setJwGameItemList(jwGameItems);
//                    if (index >= 0) {
//                        jwSportList.set(index, jwSport);
//                    } else {
//                        jwSportList.add(jwSport);
//                    }
//
//                } else if ("5".equals(jwGameItem.getSportLimit()) || "4".equals(jwGameItem.getSportLimit()) || "3".equals(jwGameItem.getSportLimit()) || "2".equals(jwGameItem.getSportLimit())) {
//                    // 多人
//                    if (jwSignRecord.getJwSignRecordSportList() != null && jwSignRecord.getJwSignRecordSportList().size() > 0) {
//                        int sportCount = jwSignRecord.getJwSignRecordSportList().size();
//                        BigDecimal fee = jwGameItem.getFee();
//
//                        fee = jwTeamService.getSignRecordFee(jwGameItem, jwSignRecord);
//
//                        // 如果报名人数超过规定人数, 就重新计算价格   价格 / 规定人数 * 实际人数
////                        if (sportCount > jwGameItem.getFeeMaxSport() && BigDecimalUtil.isNotNull(fee)) {
////                            fee = fee.multiply(new BigDecimal(sportCount)).divide(new BigDecimal(jwGameItem.getFeeMaxSport()), 0, RoundingMode.DOWN);
////                        }
//                        jwSignRecord.setFee(fee);
//
//                        BigDecimal avgfee = jwSignRecord.getFee().divide(new BigDecimal(sportCount), 0, RoundingMode.DOWN);
//                        jwSignRecord.setAvgFee(avgfee);
//
//                        for (JwSignRecordSport jwSignRecordSport : jwSignRecord.getJwSignRecordSportList()) {
//
//                            Long sportId = jwSignRecordSport.getSportId();
//
//                            List<JwSport> finalJwSportList = jwSportList;
//                            int index = IntStream.range(0, jwSportList.size())
//                                    .filter(i -> sportId.equals(finalJwSportList.get(i).getId()))
//                                    .findFirst()
//                                    .orElse(-1);
//                            JwSport jwSport = null;
//                            if (index >= 0) {
//                                jwSport = jwSportList.get(index);
//                            } else {
//                                jwSport = jwSportService.selectJwSportById(sportId);
//                            }
//
//                            List<JwGameItem> jwGameItems = jwSport.getJwGameItemList();
//                            if (jwGameItems == null) {
//                                jwGameItems = new ArrayList<>();
//                            }
//                            jwGameItem.setFee(avgfee);
//                            jwGameItems.add(jwGameItem);
//
//                            jwSport.setJwGameItemList(jwGameItems);
//                            if (index >= 0) {
//                                jwSportList.set(index, jwSport);
//                            } else {
//                                jwSportList.add(jwSport);
//                            }
//                        }
//                    }
//                }
//            }
//            jwSignRecords.sort(Comparator.comparing(s -> s.getJwGameItem().getCode()));
//        }
//        return jwSignRecords;
    }

    // 获取代表队赛程
    @PostMapping("/getTeamScheduleInfoList")
    @ResponseBody
    public AjaxResult getTeamScheduleInfoList(Long matchId, Long teamId) {
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.getTeamScheduleInfoList(matchId, teamId);
        return AjaxResult.success(jwSignRecordList);
    }

    // 获取代表队赛程 下载
    @PostMapping("/getTeamScheduleInfoListDownload")
    @ResponseBody
    public ResponseEntity<byte[]> getTeamScheduleInfoListDownload(Long matchId, Long teamId, String type, String type2) {
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.getTeamScheduleInfoList(matchId, teamId);
        try {
            if (StringUtils.isNotEmpty(type)) {
                jwSignRecordList = jwSignRecordList.stream().filter(jwSignRecord -> jwSignRecord.getScheduleName().contains(type)).collect(Collectors.toList());
            }
            // 调用服务生成Word文档并返回字节数组
            byte[] wordBytes = PdfGenerator.generateWord(jwSignRecordList, jwMatchService.selectJwMatchById(matchId).getMatchName(), jwTeamService.selectJwTeamById(teamId), type2);
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "schedule.docx");
            // 返回带有字节数据和响应头的ResponseEntity
            return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // 处理异常情况
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchTeam:export')")
    @Log(title = "选手赛程表XLS", businessType = BusinessType.EXPORT)
    @PostMapping("/getTeamScheduleInfoListDownloadExcel")
    public void getTeamScheduleInfoListDownloadExcel(HttpServletResponse response, Long matchId, Long teamId, String type) {
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.getTeamScheduleInfoList(matchId, teamId);
        if (StringUtils.isNotEmpty(type)) {
            jwSignRecordList = jwSignRecordList.stream().filter(jwSignRecord -> jwSignRecord.getScheduleName().contains(type)).collect(Collectors.toList());
        }
        List<JwSignRecordExportXLS> jwSignRecordExportXLSList = new ArrayList<>();
        jwSignRecordList.forEach(jwSignRecord -> {
            JwSignRecordExportXLS jwSignRecordExportXLS = new JwSignRecordExportXLS();
            jwSignRecordExportXLS.setPlaceOrder(jwSignRecord.getPlaceOrder());
            jwSignRecordExportXLS.setScheduleIndex(jwSignRecord.getJwScheduleItem().getScheduleIndex());
            jwSignRecordExportXLS.setSchedulePlace("第" + jwSignRecord.getPlaceOrder() + "场 第" + jwSignRecord.getJwScheduleItem().getScheduleIndex() + "组");
            jwSignRecordExportXLS.setGameItemCode(jwSignRecord.getItemName().split(":")[0]);
            jwSignRecordExportXLS.setGameItemName(jwSignRecord.getItemName().split(":")[1]);
            jwSignRecordExportXLS.setIndexTime(jwSignRecord.getIndexTime());
            jwSignRecordExportXLS.setIndexOrder(jwSignRecord.getIndexOrder());
            jwSignRecordExportXLS.setScheduleName(jwSignRecord.getScheduleName());
            jwSignRecordExportXLS.setBackNumber(jwSignRecord.getBackNumber());
            jwSignRecordExportXLS.setTeamName(jwSignRecord.getTeamName());
            if (jwSignRecord.getJwSignRecordSportList() != null && jwSignRecord.getJwSignRecordSportList().size() > 0) {
                jwSignRecordExportXLS.setPlayerName(jwSignRecord.getJwSignRecordSportList().stream().map(JwSignRecordSport::getPlayerName).collect(Collectors.joining(";")));
            }
            jwSignRecordExportXLSList.add(jwSignRecordExportXLS);
        });

        ExcelUtil<JwSignRecordExportXLS> util = new ExcelUtil<>(JwSignRecordExportXLS.class);
        List<JwSignRecordExportXLS> jwSignRecordExportXLSListRe = jwSignRecordExportXLSList.stream().sorted(
                Comparator.comparing((JwSignRecordExportXLS jw) -> parseScheduleNameForSort(jw.getScheduleName()).getKey())
                        .thenComparing(r -> parseScheduleNameForSort(r.getScheduleName()).getValue())
                        .thenComparing(JwSignRecordExportXLS::getPlaceOrder)
                        .thenComparing(JwSignRecordExportXLS::getScheduleIndex)
                        .thenComparing(JwSignRecordExportXLS::getIndexOrder)).collect(Collectors.toList());

        util.exportExcel(response, jwSignRecordExportXLSListRe, "比赛参赛的队伍数据");
    }

    public static AbstractMap.SimpleEntry<MonthDay, Integer> parseScheduleNameForSort(String name) {
        MonthDay dateKey = MonthDay.of(1, 1); // 默认 1月1日
        int placeNum = 999;

        // 1. 提取日期：如 "5月1日"
        Pattern datePattern = Pattern.compile("(\\d{1,2}月\\d{1,2}日)");
        Matcher m1 = datePattern.matcher(name);
        if (m1.find()) {
            String ds = m1.group(1).replace("月", "-").replace("日", "");
            try {
                dateKey = MonthDay.parse(ds, DateTimeFormatter.ofPattern("M-d"));
            } catch (Exception ignored) {
            }
        }

        // 2. 提取“第X场地”
        Pattern placePattern = Pattern.compile("第([一二三四五六七八九十]+)场地");
        Matcher m2 = placePattern.matcher(name);
        if (m2.find()) {
            placeNum = chineseNumToArabic(m2.group(1));
        }

        return new AbstractMap.SimpleEntry<>(dateKey, placeNum);
    }

    public static int chineseNumToArabic(String cn) {
        if (cn == null || cn.isEmpty()) return 999;
        switch (cn) {
            case "一":
                return 1;
            case "二":
                return 2;
            case "三":
                return 3;
            case "四":
                return 4;
            case "五":
                return 5;
            case "六":
                return 6;
            case "七":
                return 7;
            case "八":
                return 8;
            case "九":
                return 9;
            case "十":
                return 10;
            case "十一":
                return 11;
            case "十二":
                return 12;
            case "十三":
                return 13;
            case "十四":
                return 14;
            case "十五":
                return 15;
            case "十六":
                return 16;
            case "十七":
                return 17;
            case "十八":
                return 18;
            case "十九":
                return 19;
            case "二十":
                return 20;
            case "二十一":
                return 21;
            case "二十二":
                return 22;
            case "二十三":
                return 23;
            case "二十四":
                return 24;
            case "二十五":
                return 25;
            case "二十六":
                return 26;
            case "二十七":
                return 27;
            default:
                return 999;
        }
    }

    // 下载 代表队收费单
    @PostMapping("/getTeamFeeDownload")
    @ResponseBody
    public ResponseEntity<byte[]> getTeamFeeDownload(Long matchId, Long teamId) {
        //按选手查看
        List<JwSport> jwSportList = new ArrayList<>();
        // 全部报名记录
        List<JwSignRecord> jwSignRecordList = genJwSignRecords(teamId, matchId, jwSportList);
        try {
            // 调用服务生成Word文档并返回字节数组
            byte[] wordBytes = PdfGenerator.generateFeeWord(jwSignRecordList, jwMatchService.selectJwMatchById(matchId), jwTeamService.selectJwTeamById(teamId));
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "fee.docx");
            // 返回带有字节数据和响应头的ResponseEntity
            return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // 处理异常情况
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @PostMapping("/removeTeam")
    @ResponseBody
    public AjaxResult removeTeam(Long matchId, Long indexOrder) {

        return AjaxResult.success(jwMatchTeamService.deleteJwMatchTeamByTeamMatch(indexOrder, matchId));
    }

    @PostMapping("/reOrderMatchTeam")
    @ResponseBody
    public AjaxResult reOrderMatchTeam(Long matchId) {
        return AjaxResult.success(jwMatchTeamService.reOrderMatchTeam(matchId));
    }


}
