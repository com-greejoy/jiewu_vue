package com.ruoyi.project.jiewu.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.jiewu.service.JwTeamLeaderService;
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
import com.ruoyi.project.jiewu.domain.JwTeam;
import com.ruoyi.project.jiewu.service.JwTeamService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 代表队Controller
 * 
 * @author ruoyi
 * @date 2024-05-29
 */
@RestController
@RequestMapping("/jiewu/JwTeam")
public class JwTeamController extends BaseController {

    @Autowired
    private JwTeamService jwTeamService;

    @Autowired
    private JwTeamLeaderService jwTeamLeaderService;

    /**
     * 查询代表队列表
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwTeam:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwTeam jwTeam) {

        if(!StringUtils.isLongNotNull(jwTeam.getMatchId())){
            startPage();
        }
        List<JwTeam> list = jwTeamService.selectJwTeamList(jwTeam);
        if(list != null && list.size() > 0){
            if(StringUtils.isLongNotNull(jwTeam.getMatchId())){
                list.forEach(jwTeam1 -> {
                    jwTeam1.setJwTeamLeaderList(jwTeamLeaderService.selectJwTeamLeaderListByWxUser(jwTeam1.getCreateUserId(), null, jwTeam.getMatchId()));
                });
            }

        }
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwTeam:export')")
    @Log(title = "代表队", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwTeam jwTeam) {
        List<JwTeam> list = jwTeamService.selectJwTeamList(jwTeam);
        ExcelUtil<JwTeam> util = new ExcelUtil<JwTeam>(JwTeam.class);
        util.exportExcel(response, list, "代表队数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwTeam:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwTeamService.selectJwTeamById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwTeam:add')")
    @Log(title = "代表队", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwTeam jwTeam) {
        return toAjax(jwTeamService.insertJwTeam(jwTeam));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwTeam:edit')")
    @Log(title = "代表队", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwTeam jwTeam) {
        return toAjax(jwTeamService.updateJwTeam(jwTeam));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwTeam:remove')")
    @Log(title = "代表队", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwTeamService.deleteJwTeamByIds(ids));
    }
}