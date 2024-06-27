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
import com.ruoyi.project.jiewu.domain.JwSchedulePlace;
import com.ruoyi.project.jiewu.service.JwSchedulePlaceService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwSchedulePlace")
public class JwSchedulePlaceController extends BaseController {

    @Autowired
    private JwSchedulePlaceService jwSchedulePlaceService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwSchedulePlace:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwSchedulePlace jwSchedulePlace) {
        startPage();
        List<JwSchedulePlace> list = jwSchedulePlaceService.selectJwSchedulePlaceList(jwSchedulePlace);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSchedulePlace:list')")
    @GetMapping("/listJwSchedulePlaceWithScheduleItem")
    public AjaxResult listJwSchedulePlaceWithScheduleItem(JwSchedulePlace jwSchedulePlace) {
        List<JwSchedulePlace> list = jwSchedulePlaceService.listJwSchedulePlaceWithScheduleItem(jwSchedulePlace);
        return AjaxResult.success(list);
    }


    @PreAuthorize("@ss.hasPermi('jiewu:JwSchedulePlace:export')")
    @Log(title = "场次", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwSchedulePlace jwSchedulePlace) {
        List<JwSchedulePlace> list = jwSchedulePlaceService.selectJwSchedulePlaceList(jwSchedulePlace);
        ExcelUtil<JwSchedulePlace> util = new ExcelUtil<JwSchedulePlace>(JwSchedulePlace.class);
        util.exportExcel(response, list, "场地数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSchedulePlace:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwSchedulePlaceService.selectJwSchedulePlaceById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSchedulePlace:add')")
    @Log(title = "场次", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwSchedulePlace jwSchedulePlace) {
        return toAjax(jwSchedulePlaceService.insertJwSchedulePlace(jwSchedulePlace));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSchedulePlace:edit')")
    @Log(title = "场次", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwSchedulePlace jwSchedulePlace) {
        return toAjax(jwSchedulePlaceService.updateJwSchedulePlace(jwSchedulePlace));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSchedulePlace:remove')")
    @Log(title = "场次", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(jwSchedulePlaceService.deleteJwSchedulePlaceById(id));
    }
}
