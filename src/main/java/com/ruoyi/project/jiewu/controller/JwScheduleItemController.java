package com.ruoyi.project.jiewu.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.jiewu.domain.JwScheduleItem;
import com.ruoyi.project.jiewu.service.JwScheduleItemService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwScheduleItem")
public class JwScheduleItemController extends BaseController {

    @Autowired
    private JwScheduleItemService jwScheduleItemService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwScheduleItem jwScheduleItem) {
        startPage();
        List<JwScheduleItem> list = jwScheduleItemService.selectJwScheduleItemList(jwScheduleItem);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:add')")
    @Log(title = "赛程小项", businessType = BusinessType.INSERT)
    @PostMapping("/initScheduleItem")
    @ResponseBody
    public AjaxResult initScheduleItem(Long matchId) {
        return jwScheduleItemService.initScheduleItem(matchId);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:add')")
    @Log(title = "赛程小项", businessType = BusinessType.INSERT)
    @PostMapping("/initBackNum")
    @ResponseBody
    public AjaxResult initBackNum(Long matchId) {
        return jwScheduleItemService.initBackNum(matchId);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:add')")
    @Log(title = "赛程小项", businessType = BusinessType.INSERT)
    @PostMapping("/listNoPlaceJwScheduleItem")
    @ResponseBody
    public AjaxResult listNoPlaceJwScheduleItem(Long matchId) {
        return AjaxResult.success(jwScheduleItemService.listNoPlaceJwScheduleItem(matchId));
    }

    // 更新小项 的 场地 单元
    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:add')")
    @Log(title = "赛程小项", businessType = BusinessType.UPDATE)
    @PostMapping("/updateJwScheduleItemPlace")
    @ResponseBody
    public AjaxResult updateJwScheduleItemPlace(Long[] jwScheduleItemIds, Long scheduleInfoId, Long schedulePlaceId) {
        if(jwScheduleItemIds != null && jwScheduleItemIds.length > 0){
            return AjaxResult.success(jwScheduleItemService.updateJwScheduleItemPlace(jwScheduleItemIds, scheduleInfoId, schedulePlaceId));
        }else{
            return AjaxResult.error("错误");
        }
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:add')")
    @Log(title = "赛程小项", businessType = BusinessType.UPDATE)
    @PostMapping("/clearSchedulePlaceById")
    @ResponseBody
    public AjaxResult clearSchedulePlaceById(Long id) {
        if(StringUtils.isLongNotNull(id)){
            return AjaxResult.success(jwScheduleItemService.clearSchedulePlaceById(id));
        }else{
            return AjaxResult.error("错误");
        }
    }

    // 计算赛程时间
    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:add')")
    @Log(title = "赛程小项", businessType = BusinessType.UPDATE)
    @PostMapping("/calculateTime")
    @ResponseBody
    public AjaxResult calculateTime(Long matchId) {
        if(StringUtils.isLongNotNull(matchId)){
            return jwScheduleItemService.calculateTime(matchId);
        }else{
            return AjaxResult.error("错误");
        }
    }


    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:export')")
    @Log(title = "赛程小项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwScheduleItem jwScheduleItem) {
        List<JwScheduleItem> list = jwScheduleItemService.selectJwScheduleItemList(jwScheduleItem);
        ExcelUtil<JwScheduleItem> util = new ExcelUtil<JwScheduleItem>(JwScheduleItem.class);
        util.exportExcel(response, list, "赛程小项数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwScheduleItemService.selectJwScheduleItemById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:add')")
    @Log(title = "赛程小项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwScheduleItem jwScheduleItem) {
        return toAjax(jwScheduleItemService.insertJwScheduleItem(jwScheduleItem));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:edit')")
    @Log(title = "赛程小项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwScheduleItem jwScheduleItem) {
        return toAjax(jwScheduleItemService.updateJwScheduleItem(jwScheduleItem));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleItem:remove')")
    @Log(title = "赛程小项", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwScheduleItemService.deleteJwScheduleItemByIds(ids));
    }
}
