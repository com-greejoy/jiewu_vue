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
import com.ruoyi.project.jiewu.domain.JwTeamLeader;
import com.ruoyi.project.jiewu.service.JwTeamLeaderService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwTeamLeader")
public class JwTeamLeaderController extends BaseController {

    @Autowired
    private JwTeamLeaderService jwTeamLeaderService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwTeamLeader:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwTeamLeader jwTeamLeader) {
        startPage();
        List<JwTeamLeader> list = jwTeamLeaderService.selectJwTeamLeaderList(jwTeamLeader);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwTeamLeader:export')")
    @Log(title = "队伍人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwTeamLeader jwTeamLeader) {
        List<JwTeamLeader> list = jwTeamLeaderService.selectJwTeamLeaderList(jwTeamLeader);
        ExcelUtil<JwTeamLeader> util = new ExcelUtil<JwTeamLeader>(JwTeamLeader.class);
        util.exportExcel(response, list, "队伍人员数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwTeamLeader:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwTeamLeaderService.selectJwTeamLeaderById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwTeamLeader:add')")
    @Log(title = "队伍人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwTeamLeader jwTeamLeader) {
        return toAjax(jwTeamLeaderService.insertJwTeamLeader(jwTeamLeader));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwTeamLeader:edit')")
    @Log(title = "队伍人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwTeamLeader jwTeamLeader) {
        return toAjax(jwTeamLeaderService.updateJwTeamLeader(jwTeamLeader));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwTeamLeader:remove')")
    @Log(title = "队伍人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwTeamLeaderService.deleteJwTeamLeaderByIds(ids));
    }
}
