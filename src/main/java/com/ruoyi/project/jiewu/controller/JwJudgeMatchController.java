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
import com.ruoyi.project.jiewu.domain.JwJudgeMatch;
import com.ruoyi.project.jiewu.service.JwJudgeMatchService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwJudgeMatch")
public class JwJudgeMatchController extends BaseController {

    @Autowired
    private JwJudgeMatchService jwJudgeMatchService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudgeMatch:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwJudgeMatch jwJudgeMatch) {
        startPage();
        List<JwJudgeMatch> list = jwJudgeMatchService.selectJwJudgeMatchList(jwJudgeMatch);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudgeMatch:export')")
    @Log(title = "比赛裁判", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwJudgeMatch jwJudgeMatch) {
        List<JwJudgeMatch> list = jwJudgeMatchService.selectJwJudgeMatchList(jwJudgeMatch);
        ExcelUtil<JwJudgeMatch> util = new ExcelUtil<JwJudgeMatch>(JwJudgeMatch.class);
        util.exportExcel(response, list, "比赛裁判数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudgeMatch:add')")
    @Log(title = "比赛裁判", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwJudgeMatch jwJudgeMatch) {

        JwJudgeMatch jwMatchTeamOld = jwJudgeMatchService.selectJwJudgeMatchByJudgeId(jwJudgeMatch.getMatchId(), jwJudgeMatch.getJudgeId());
        if (jwMatchTeamOld == null) {
            jwJudgeMatchService.insertJwJudgeMatch(jwJudgeMatch);
        }
        return toAjax(1);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudgeMatch:add')")
    @Log(title = "比赛裁判", businessType = BusinessType.INSERT)
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody JwJudgeMatch jwJudgeMatch) {
        return toAjax(jwJudgeMatchService.deleteJwJudgeMatch(jwJudgeMatch.getMatchId(), jwJudgeMatch.getJudgeId()));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudgeMatch:edit')")
    @Log(title = "比赛裁判", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwJudgeMatch jwJudgeMatch) {
        return toAjax(jwJudgeMatchService.updateJwJudgeMatch(jwJudgeMatch));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwJudgeMatch:remove')")
    @Log(title = "比赛裁判", businessType = BusinessType.DELETE)
	@DeleteMapping("/{judgeIds}")
    public AjaxResult remove(@PathVariable Long[] judgeIds) {
        return toAjax(jwJudgeMatchService.deleteJwJudgeMatchByJudgeIds(judgeIds));
    }
}
