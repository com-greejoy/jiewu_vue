package com.ruoyi.project.jiewu.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.jiewu.domain.JwAwardsItem;
import com.ruoyi.project.jiewu.service.JwAwardsItemService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwAwardsItem")
public class JwAwardsItemController extends BaseController {

    @Autowired
    private JwAwardsItemService jwAwardsItemService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwAwardsItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwAwardsItem jwAwardsItem) {
        startPage();
        List<JwAwardsItem> list = jwAwardsItemService.selectJwAwardsItemList(jwAwardsItem);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwAwardsItem:export')")
    @Log(title = "奖项设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwAwardsItem jwAwardsItem) {
        List<JwAwardsItem> list = jwAwardsItemService.selectJwAwardsItemList(jwAwardsItem);
        ExcelUtil<JwAwardsItem> util = new ExcelUtil<JwAwardsItem>(JwAwardsItem.class);
        util.exportExcel(response, list, "奖项设置数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwAwardsItem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwAwardsItemService.selectJwAwardsItemById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwAwardsItem:add')")
    @Log(title = "奖项设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwAwardsItem jwAwardsItem) {
        return toAjax(jwAwardsItemService.insertJwAwardsItem(jwAwardsItem));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwAwardsItem:edit')")
    @Log(title = "奖项设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwAwardsItem jwAwardsItem) {
        return toAjax(jwAwardsItemService.updateJwAwardsItem(jwAwardsItem));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwAwardsItem:remove')")
    @Log(title = "奖项设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwAwardsItemService.deleteJwAwardsItemByIds(ids));
    }

}
