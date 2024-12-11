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
import com.ruoyi.project.jiewu.domain.JwEightScore;
import com.ruoyi.project.jiewu.service.JwEightScoreService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwEightScore")
public class JwEightScoreController extends BaseController {

    @Autowired
    private JwEightScoreService jwEightScoreService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwEightScore:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwEightScore jwEightScore) {
        startPage();
        List<JwEightScore> list = jwEightScoreService.selectJwEightScoreList(jwEightScore);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwEightScore:export')")
    @Log(title = "对阵打分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwEightScore jwEightScore) {
        List<JwEightScore> list = jwEightScoreService.selectJwEightScoreList(jwEightScore);
        ExcelUtil<JwEightScore> util = new ExcelUtil<JwEightScore>(JwEightScore.class);
        util.exportExcel(response, list, "对阵打分数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwEightScore:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwEightScoreService.selectJwEightScoreById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwEightScore:add')")
    @Log(title = "对阵打分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwEightScore jwEightScore) {
        return toAjax(jwEightScoreService.insertJwEightScore(jwEightScore));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwEightScore:edit')")
    @Log(title = "对阵打分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwEightScore jwEightScore) {
        return toAjax(jwEightScoreService.updateJwEightScore(jwEightScore));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwEightScore:remove')")
    @Log(title = "对阵打分", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwEightScoreService.deleteJwEightScoreByIds(ids));
    }
}
