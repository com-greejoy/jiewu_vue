package com.ruoyi.project.jiewu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.IDCardUtils;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/jiewu/JwSignRecord")
public class JwSignRecordController extends BaseController {

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwSignRecordSportService jwSignRecordSportService;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwTeamService jwTeamService;

    @Autowired
    private JwSportService jwSportService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwSignRecord jwSignRecord) {
        if (!StringUtils.isLongNotNull(jwSignRecord.getMatchId())) {
            return getDataTable(new ArrayList<>());
        }

        // 以组别的形式显示
        List<JwGameItem> list = jwGameItemService.selectJwGameItemListWithTeamSignRecordByMatchId(jwSignRecord.getMatchId(), jwSignRecord.getGameItemId(), jwSignRecord.getTeamId(), jwSignRecord.getBackNumber());
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:list')")
    @GetMapping("/listQiWuMusic")
    public TableDataInfo listQiWuMusic(JwSignRecord jwSignRecord) {
        if (!StringUtils.isLongNotNull(jwSignRecord.getMatchId())) {
            return getDataTable(new ArrayList<>());
        }
        // 以组别的形式显示
        List<JwSignRecord> list = jwSignRecordService.selectJwSignRecordList(jwSignRecord);
        return getDataTable(list);
    }

    // 获取比赛项目的报名数据
    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:list')")
    @PostMapping("/listJwSignRecordByGameItem")
    @ResponseBody
    public AjaxResult listJwSignRecordByGameItem(Long gameItemId, Long scheduleItemId) {
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.selectJwSignRecordListWithAllInfo(gameItemId, scheduleItemId);
        return AjaxResult.success(jwSignRecordList);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:export')")
    @Log(title = "报名记录" , businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwSignRecord jwSignRecord) {
        List<JwSignRecord> list = jwSignRecordService.selectJwSignRecordList(jwSignRecord);
        List<JwSignRecordExport> res = new ArrayList<>();

        ExcelUtil<JwSignRecordExport> util = new ExcelUtil<JwSignRecordExport>(JwSignRecordExport.class);
        if (list != null && list.size() > 0) {
            for (JwSignRecord jwSignRecord1 : list) {
                JwSignRecordExport jwSignRecordExport = new JwSignRecordExport();
                jwSignRecordExport.setGameItemName(jwGameItemService.selectJwGameItemById(jwSignRecord1.getGameItemId()).getName());
                jwSignRecordExport.setTeamName(jwTeamService.selectJwTeamById(jwSignRecord1.getTeamId()).getTeamName());
                jwSignRecordExport.setBackNumber(jwSignRecord1.getBackNumber());

                List<JwSignRecordSport> jwSignRecordSportList = jwSignRecordSportService.selectJwSignRecordSportListById(jwSignRecord1.getId());
                if (jwSignRecordSportList != null && jwSignRecordSportList.size() > 0) {
                    jwSignRecordExport.setPlayerName(jwSignRecordSportList.stream().map(jwSignRecordSport -> jwSignRecordSport.getPlayerName() + ":" + jwSignRecordSport.getIdCard()).collect(Collectors.joining(";")));
                }
                res.add(jwSignRecordExport);
            }
        }
        util.exportExcel(response, res, "报名记录数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwSignRecordService.selectJwSignRecordById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:add')")
    @Log(title = "报名记录" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwSignRecord jwSignRecord) {
        JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(jwSignRecord.getGameItemId());
        return jwSignRecordService.saveSign(jwGameItem, jwSignRecord.getSportIds(), jwSignRecord.getTeamId(), null);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:add')")
    @Log(title = "改组" , businessType = BusinessType.INSERT)
    @PostMapping("/changeGameItem")
    @ResponseBody
    public AjaxResult changeGameItem(Long id, Long changeGameItemId) {
        jwSignRecordService.changeGameItem(id, changeGameItemId);
        return AjaxResult.success(1);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:add')")
    @Log(title = "改小项" , businessType = BusinessType.UPDATE)
    @PostMapping("/changeJwScheduleItem")
    @ResponseBody
    public AjaxResult changeJwScheduleItem(Long id, Long changeScheduleItemId) {
        jwSignRecordService.changeJwScheduleItem(id, changeScheduleItemId);
        return AjaxResult.success(1);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:edit')")
    @Log(title = "报名记录" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwSignRecord jwSignRecord) {
        return toAjax(jwSignRecordService.updateJwSignRecord(jwSignRecord));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:remove')")
    @Log(title = "报名记录" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(jwSignRecordService.deleteJwSignRecordById(id));
    }

    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, Long matchId) throws Exception {
        if (matchId != null && matchId != 0) {

            ExcelUtil<JwSportImport> util = new ExcelUtil<>(JwSportImport.class);
            List<JwSportImport> userList = util.importExcel(file.getInputStream());

            for (JwSportImport jwSportImport : userList) {

                JwGameItem jwGameItem = jwGameItemService.selectJwGameItemByName(jwSportImport.getPlayerGroup(), matchId);
                JwTeam jwTeam = jwTeamService.selectJwTeamByName(jwSportImport.getPlayerTeam());
                List<Long> sportIds = new ArrayList<>();
                String[] playerNames = jwSportImport.getPlayerName().replaceAll(" " , "").split("&");
                for (String playerName : playerNames) {
                    JwSport jwSport = jwSportService.selectJwSportByName(playerName, null, jwTeam.getCreateUserId());
                    if (jwSport != null && StringUtils.isNotEmpty(jwSport.getPlayerName())) {
                        sportIds.add(jwSport.getId());
                    } else {
                        jwSport = new JwSport();
                        jwSport.setPlayerName(playerName);
                        jwSport.setIdCard(IDCardUtils.RandomIdCard());
                        jwSport.setCreateUserId(jwTeam.getCreateUserId());
                        jwSportService.insertJwSport(jwSport);
                        sportIds.add(jwSport.getId());
                    }
                }
                jwSignRecordService.saveSign(jwGameItem, sportIds.toArray(new Long[0]), jwTeam.getId(), null);
            }
        }
        return success();
    }
}