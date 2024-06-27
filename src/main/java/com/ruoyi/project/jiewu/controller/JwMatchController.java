package com.ruoyi.project.jiewu.controller;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.jiewu.domain.JwMatch;
import com.ruoyi.project.jiewu.service.JwMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 赛事管理Controller
 * 
 * @author ruoyi
 * @date 2024-05-27
 */
@RestController
@RequestMapping("/jiewu/jwMatch")
public class JwMatchController extends BaseController {

    @Autowired
    private JwMatchService jwMatchService;

    /**
     * 查询赛事管理列表
     */
    @PreAuthorize("@ss.hasPermi('jiewu:jwMatch:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwMatch jwMatch) {
        startPage();
        List<JwMatch> list = jwMatchService.selectJwMatchList(jwMatch);
        return getDataTable(list);
    }

    /**
     * 导出赛事管理列表
     */
    @PreAuthorize("@ss.hasPermi('jiewu:jwMatch:export')")
    @Log(title = "赛事管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwMatch jwMatch) {
        List<JwMatch> list = jwMatchService.selectJwMatchList(jwMatch);
        ExcelUtil<JwMatch> util = new ExcelUtil<JwMatch>(JwMatch.class);
        util.exportExcel(response, list, "赛事管理数据");
    }

    /**
     * 获取赛事管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('jiewu:jwMatch:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwMatchService.selectJwMatchById(id));
    }

    /**
     * 新增赛事管理
     */
    @PreAuthorize("@ss.hasPermi('jiewu:jwMatch:add')")
    @Log(title = "赛事管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwMatch jwMatch) {
        return toAjax(jwMatchService.insertJwMatch(jwMatch));
    }

    /**
     * 修改赛事管理
     */
    @PreAuthorize("@ss.hasPermi('jiewu:jwMatch:edit')")
    @Log(title = "赛事管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwMatch jwMatch) {
        Date now = DateUtils.getNowDate();
        //  未开始
        if (now.before(jwMatch.getSignBeginTime())) {
            jwMatch.setState("1");
        }
        //正在报名
        if (now.after(jwMatch.getSignBeginTime()) && now.before(jwMatch.getSignEndTime())) {
            jwMatch.setState("2");
        }
        // 报名结束
        if (now.after(jwMatch.getSignEndTime())) {
            jwMatch.setState("3");
        }
        return toAjax(jwMatchService.updateJwMatch(jwMatch));
    }

    /**
     * 删除赛事管理
     */
    @PreAuthorize("@ss.hasPermi('jiewu:jwMatch:remove')")
    @Log(title = "赛事管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwMatchService.deleteJwMatchByIds(ids));
    }
}
