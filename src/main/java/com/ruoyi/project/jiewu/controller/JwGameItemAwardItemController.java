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
import com.ruoyi.project.jiewu.domain.JwGameItemAwardItem;
import com.ruoyi.project.jiewu.service.JwGameItemAwardItemService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwGameItemAwardItem")
public class JwGameItemAwardItemController extends BaseController {

    @Autowired
    private JwGameItemAwardItemService jwGameItemAwardItemService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItemAwardItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwGameItemAwardItem jwGameItemAwardItem) {
        startPage();
        List<JwGameItemAwardItem> list = jwGameItemAwardItemService.selectJwGameItemAwardItemList(jwGameItemAwardItem);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItemAwardItem:export')")
    @Log(title = "项目奖项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwGameItemAwardItem jwGameItemAwardItem) {
        List<JwGameItemAwardItem> list = jwGameItemAwardItemService.selectJwGameItemAwardItemList(jwGameItemAwardItem);
        ExcelUtil<JwGameItemAwardItem> util = new ExcelUtil<JwGameItemAwardItem>(JwGameItemAwardItem.class);
        util.exportExcel(response, list, "项目奖项数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItemAwardItem:query')")
    @GetMapping(value = "/{gameItemId}")
    public AjaxResult getInfo(@PathVariable("gameItemId") Long gameItemId) {
        return success(jwGameItemAwardItemService.selectJwGameItemAwardItemByGameItemId(gameItemId));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItemAwardItem:add')")
    @Log(title = "项目奖项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwGameItemAwardItem jwGameItemAwardItem) {
        return toAjax(jwGameItemAwardItemService.insertJwGameItemAwardItem(jwGameItemAwardItem));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItemAwardItem:edit')")
    @Log(title = "项目奖项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwGameItemAwardItem jwGameItemAwardItem) {
        return toAjax(jwGameItemAwardItemService.updateJwGameItemAwardItem(jwGameItemAwardItem));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItemAwardItem:remove')")
    @Log(title = "项目奖项", businessType = BusinessType.DELETE)
	@DeleteMapping("/{gameItemIds}")
    public AjaxResult remove(@PathVariable Long[] gameItemIds) {
        return toAjax(jwGameItemAwardItemService.deleteJwGameItemAwardItemByGameItemIds(gameItemIds));
    }
}
