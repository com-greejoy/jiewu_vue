package com.ruoyi.project.jiewu.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.project.jiewu.service.JwScheduleItemService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.jiewu.domain.JwGameItem;
import com.ruoyi.project.jiewu.service.JwGameItemService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwGameItem")
public class JwGameItemController extends BaseController {

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwScheduleItemService jwScheduleItemService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwGameItem jwGameItem) {
        startPage();
        List<JwGameItem> list = jwGameItemService.selectJwGameItemListWithCount(jwGameItem);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:export')")
    @Log(title = "比赛项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwGameItem jwGameItem) {
        List<JwGameItem> list = jwGameItemService.selectJwGameItemList(jwGameItem);
        ExcelUtil<JwGameItem> util = new ExcelUtil<JwGameItem>(JwGameItem.class);
        util.exportExcel(response, list, "比赛项目数据");
    }

    // 比赛项目重新分组
    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:edit')")
    @Log(title = "比赛项目重新分组", businessType = BusinessType.UPDATE)
    @PostMapping("/reGroupJwGameItem")
    @ResponseBody
    public AjaxResult reGroupJwGameItem(Long id) {
        JwGameItem queryItem = new JwGameItem();
        queryItem.setId(id);
        List<JwGameItem> jwGameItemList = jwGameItemService.selectJwGameItemListWithCount(queryItem);

        if(jwGameItemList != null && jwGameItemList.size() > 0){
            return AjaxResult.success(jwScheduleItemService.reGroupJwGameItem(jwGameItemList.get(0)));
        }
        return AjaxResult.error("没有选择组别");
    }


    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwGameItemService.selectJwGameItemById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:add')")
    @Log(title = "比赛项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwGameItem jwGameItem) {
        return toAjax(jwGameItemService.insertJwGameItem(jwGameItem));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:edit')")
    @Log(title = "比赛项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwGameItem jwGameItem) {
        return toAjax(jwGameItemService.updateJwGameItem(jwGameItem));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:remove')")
    @Log(title = "比赛项目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwGameItemService.deleteJwGameItemByIds(ids));
    }
}
