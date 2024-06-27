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
import com.ruoyi.project.jiewu.domain.JwJudge;
import com.ruoyi.project.jiewu.service.JwJudgeService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwJudge")
public class JwJudgeController extends BaseController {

    @Autowired
    private JwJudgeService jwJudgeService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudge:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwJudge jwJudge) {
        startPage();
        List<JwJudge> list = jwJudgeService.selectJwJudgeList(jwJudge);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudge:export')")
    @Log(title = "打分裁判", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwJudge jwJudge) {
        List<JwJudge> list = jwJudgeService.selectJwJudgeList(jwJudge);
        ExcelUtil<JwJudge> util = new ExcelUtil<JwJudge>(JwJudge.class);
        util.exportExcel(response, list, "打分裁判数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudge:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwJudgeService.selectJwJudgeById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudge:add')")
    @Log(title = "打分裁判", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwJudge jwJudge) {
        return toAjax(jwJudgeService.insertJwJudge(jwJudge));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudge:edit')")
    @Log(title = "打分裁判", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwJudge jwJudge) {
        return toAjax(jwJudgeService.updateJwJudge(jwJudge));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudge:remove')")
    @Log(title = "打分裁判", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwJudgeService.deleteJwJudgeByIds(ids));
    }
}