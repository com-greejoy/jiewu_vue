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
import com.ruoyi.project.jiewu.domain.JwScheduleInfo;
import com.ruoyi.project.jiewu.service.JwScheduleInfoService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwScheduleInfo")
public class JwScheduleInfoController extends BaseController {

    @Autowired
    private JwScheduleInfoService jwScheduleInfoService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwScheduleInfo jwScheduleInfo) {
        startPage();
        List<JwScheduleInfo> list = jwScheduleInfoService.selectJwScheduleInfoList(jwScheduleInfo);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleInfo:export')")
    @Log(title = "阶段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwScheduleInfo jwScheduleInfo) {
        List<JwScheduleInfo> list = jwScheduleInfoService.selectJwScheduleInfoList(jwScheduleInfo);
        ExcelUtil<JwScheduleInfo> util = new ExcelUtil<JwScheduleInfo>(JwScheduleInfo.class);
        util.exportExcel(response, list, "阶段数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwScheduleInfoService.selectJwScheduleInfoById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleInfo:add')")
    @Log(title = "阶段", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwScheduleInfo jwScheduleInfo) {
        return toAjax(jwScheduleInfoService.insertJwScheduleInfo(jwScheduleInfo));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleInfo:edit')")
    @Log(title = "阶段", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwScheduleInfo jwScheduleInfo) {
        return toAjax(jwScheduleInfoService.updateJwScheduleInfo(jwScheduleInfo));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwScheduleInfo:remove')")
    @Log(title = "阶段", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(jwScheduleInfoService.deleteJwScheduleInfoById(id));
    }
}
