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
import com.ruoyi.project.jiewu.domain.JwWxUser;
import com.ruoyi.project.jiewu.service.JwWxUserService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 微信用户Controller
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
@RestController
@RequestMapping("/jiewu/JwWxUser")
public class JwWxUserController extends BaseController {

    @Autowired
    private JwWxUserService jwWxUserService;

    /**
     * 查询微信用户列表
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwWxUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwWxUser jwWxUser) {
        startPage();
        List<JwWxUser> list = jwWxUserService.selectJwWxUserList(jwWxUser);
        return getDataTable(list);
    }

    /**
     * 导出微信用户列表
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwWxUser:export')")
    @Log(title = "微信用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwWxUser jwWxUser) {
        List<JwWxUser> list = jwWxUserService.selectJwWxUserList(jwWxUser);
        ExcelUtil<JwWxUser> util = new ExcelUtil<JwWxUser>(JwWxUser.class);
        util.exportExcel(response, list, "微信用户数据");
    }

    /**
     * 获取微信用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwWxUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwWxUserService.selectJwWxUserById(id));
    }

    /**
     * 新增微信用户
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwWxUser:add')")
    @Log(title = "微信用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwWxUser jwWxUser) {
        return toAjax(jwWxUserService.insertJwWxUser(jwWxUser));
    }

    /**
     * 修改微信用户
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwWxUser:edit')")
    @Log(title = "微信用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwWxUser jwWxUser) {
        return toAjax(jwWxUserService.updateJwWxUser(jwWxUser));
    }

    /**
     * 删除微信用户
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwWxUser:remove')")
    @Log(title = "微信用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwWxUserService.deleteJwWxUserByIds(ids));
    }
}
