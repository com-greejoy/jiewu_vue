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
import com.ruoyi.project.jiewu.domain.JwSignRecordSport;
import com.ruoyi.project.jiewu.service.JwSignRecordSportService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 报名记录选手Controller
 * 
 * @author ruoyi
 * @date 2024-06-05
 */
@RestController
@RequestMapping("/jiewu/JwSignRecordSport")
public class JwSignRecordSportController extends BaseController {

    @Autowired
    private JwSignRecordSportService jwSignRecordSportService;

    /**
     * 查询报名记录选手列表
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecordSport:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwSignRecordSport jwSignRecordSport) {
        startPage();
        List<JwSignRecordSport> list = jwSignRecordSportService.selectJwSignRecordSportList(jwSignRecordSport);
        return getDataTable(list);
    }

    /**
     * 导出报名记录选手列表
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecordSport:export')")
    @Log(title = "报名记录选手", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwSignRecordSport jwSignRecordSport) {
        List<JwSignRecordSport> list = jwSignRecordSportService.selectJwSignRecordSportList(jwSignRecordSport);
        ExcelUtil<JwSignRecordSport> util = new ExcelUtil<JwSignRecordSport>(JwSignRecordSport.class);
        util.exportExcel(response, list, "报名记录选手数据");
    }

    /**
     * 获取报名记录选手详细信息
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecordSport:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwSignRecordSportService.selectJwSignRecordSportById(id));
    }

    /**
     * 新增报名记录选手
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecordSport:add')")
    @Log(title = "报名记录选手", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwSignRecordSport jwSignRecordSport) {
        return toAjax(jwSignRecordSportService.insertJwSignRecordSport(jwSignRecordSport));
    }

    /**
     * 修改报名记录选手
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecordSport:edit')")
    @Log(title = "报名记录选手", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwSignRecordSport jwSignRecordSport) {
        return toAjax(jwSignRecordSportService.updateJwSignRecordSport(jwSignRecordSport));
    }

    /**
     * 删除报名记录选手
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecordSport:remove')")
    @Log(title = "报名记录选手", businessType = BusinessType.DELETE)
	@DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(jwSignRecordSportService.deleteJwSignRecordSportById(id));
    }
}
