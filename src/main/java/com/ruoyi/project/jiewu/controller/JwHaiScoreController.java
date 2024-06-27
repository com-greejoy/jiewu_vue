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
import com.ruoyi.project.jiewu.domain.JwHaiScore;
import com.ruoyi.project.jiewu.service.JwHaiScoreService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwHaiScore")
public class JwHaiScoreController extends BaseController {

    @Autowired
    private JwHaiScoreService jwHaiScoreService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwHaiScore jwHaiScore) {
        startPage();
        List<JwHaiScore> list = jwHaiScoreService.selectJwHaiScoreList(jwHaiScore);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:export')")
    @Log(title = "海选打分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwHaiScore jwHaiScore) {
        List<JwHaiScore> list = jwHaiScoreService.selectJwHaiScoreList(jwHaiScore);
        ExcelUtil<JwHaiScore> util = new ExcelUtil<JwHaiScore>(JwHaiScore.class);
        util.exportExcel(response, list, "海选打分数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwHaiScoreService.selectJwHaiScoreById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:add')")
    @Log(title = "海选打分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwHaiScore jwHaiScore) {
        return toAjax(jwHaiScoreService.insertJwHaiScore(jwHaiScore));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:edit')")
    @Log(title = "海选打分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwHaiScore jwHaiScore) {
        return toAjax(jwHaiScoreService.updateJwHaiScore(jwHaiScore));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:remove')")
    @Log(title = "海选打分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwHaiScoreService.deleteJwHaiScoreByIds(ids));
    }
}
