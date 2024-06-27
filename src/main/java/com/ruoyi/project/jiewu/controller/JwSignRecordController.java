package com.ruoyi.project.jiewu.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.jiewu.domain.JwGameItem;
import com.ruoyi.project.jiewu.service.JwGameItemService;
import com.ruoyi.project.jiewu.service.JwSignRecordSportService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.jiewu.domain.JwSignRecord;
import com.ruoyi.project.jiewu.service.JwSignRecordService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwSignRecord")
public class JwSignRecordController extends BaseController {

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwSignRecordSportService jwSignRecordSportService;

    @Autowired
    private JwGameItemService jwGameItemService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwSignRecord jwSignRecord) {
        if(!StringUtils.isLongNotNull(jwSignRecord.getMatchId()) || !StringUtils.isLongNotNull(jwSignRecord.getTeamId())){
            return getDataTable(new ArrayList<>());
        }

        // 以组别的形式显示
        List<JwGameItem> list = jwGameItemService.selectJwGameItemListWithTeamSignRecordByMatchId(jwSignRecord.getMatchId(), jwSignRecord.getGameItemId(), jwSignRecord.getTeamId(), jwSignRecord.getBackNumber());
        return getDataTable(list);
    }

    // 获取比赛项目的报名数据
    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:list')")
    @PostMapping("/listJwSignRecordByGameItem")
    @ResponseBody
    public AjaxResult listJwSignRecordByGameItem(Long gameItemId, Long scheduleItemId) {
        return AjaxResult.success(jwSignRecordService.selectJwSignRecordListWithAllInfo(gameItemId, scheduleItemId));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:export')")
    @Log(title = "报名记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwSignRecord jwSignRecord) {
        List<JwSignRecord> list = jwSignRecordService.selectJwSignRecordList(jwSignRecord);
        ExcelUtil<JwSignRecord> util = new ExcelUtil<JwSignRecord>(JwSignRecord.class);
        util.exportExcel(response, list, "报名记录数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwSignRecordService.selectJwSignRecordById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:add')")
    @Log(title = "报名记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwSignRecord jwSignRecord) {
        JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(jwSignRecord.getGameItemId());
        return jwSignRecordService.saveSign(jwGameItem, jwSignRecord.getSportIds(), jwSignRecord.getTeamId(), null);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:add')")
    @Log(title = "改组", businessType = BusinessType.INSERT)
    @PostMapping("/changeGameItem")
    @ResponseBody
    public AjaxResult changeGameItem(Long id, Long changeGameItemId) {
        jwSignRecordService.changeGameItem(id, changeGameItemId);
        return AjaxResult.success(1);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:add')")
    @Log(title = "改小项", businessType = BusinessType.UPDATE)
    @PostMapping("/changeJwScheduleItem")
    @ResponseBody
    public AjaxResult changeJwScheduleItem(Long id, Long changeScheduleItemId) {
        jwSignRecordService.changeJwScheduleItem(id, changeScheduleItemId);
        return AjaxResult.success(1);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:edit')")
    @Log(title = "报名记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwSignRecord jwSignRecord) {
        return toAjax(jwSignRecordService.updateJwSignRecord(jwSignRecord));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:remove')")
    @Log(title = "报名记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(jwSignRecordService.deleteJwSignRecordById(id));
    }
}
