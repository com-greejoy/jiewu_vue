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
import com.ruoyi.project.jiewu.domain.JwMatchUser;
import com.ruoyi.project.jiewu.service.JwMatchUserService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwMatchUser")
public class JwMatchUserController extends BaseController {

    @Autowired
    private JwMatchUserService jwMatchUserService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwMatchUser jwMatchUser) {
        List<JwMatchUser> list = jwMatchUserService.selectJwMatchUserList(jwMatchUser);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchUser:export')")
    @Log(title = "比赛管理员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwMatchUser jwMatchUser) {
        List<JwMatchUser> list = jwMatchUserService.selectJwMatchUserList(jwMatchUser);
        ExcelUtil<JwMatchUser> util = new ExcelUtil<JwMatchUser>(JwMatchUser.class);
        util.exportExcel(response, list, "比赛管理员数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchUser:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId) {
        return success(jwMatchUserService.selectJwMatchUserByUserId(userId));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchUser:add')")
    @Log(title = "比赛管理员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwMatchUser jwMatchUser) {
        return toAjax(jwMatchUserService.insertJwMatchUser(jwMatchUser));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchUser:edit')")
    @Log(title = "比赛管理员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwMatchUser jwMatchUser) {
        return toAjax(jwMatchUserService.updateJwMatchUser(jwMatchUser));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwMatchUser:remove')")
    @Log(title = "比赛管理员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        return toAjax(jwMatchUserService.deleteJwMatchUserByUserIds(userIds));
    }
}
