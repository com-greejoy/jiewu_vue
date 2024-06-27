package com.ruoyi.project.jiewu.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.IDCardUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.ruoyi.project.jiewu.domain.JwSport;
import com.ruoyi.project.jiewu.service.JwSportService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/jiewu/JwSport")
public class JwSportController extends BaseController {

    @Autowired
    private JwSportService jwSportService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwSport:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwSport jwSport, Long gameItemId) {

        if(StringUtils.isLongNotNull(gameItemId)){
            List<JwSport> jwSportList = jwSportService.getWxSportListWithGameItem(jwSport.getCreateUserId(), gameItemId, 0l, 100l, null);
            return getDataTable(jwSportList);
        }else{
            startPage();
            List<JwSport> list = jwSportService.selectJwSportList(jwSport);
            return getDataTable(list);
        }
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSport:list')")
    @GetMapping("/listMatchJwSport")
    public TableDataInfo listMatchJwSport(JwSport jwSport) {
        startPage();
        return getDataTable(jwSportService.getWxSportListByMatchTeam(jwSport));
    }


    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<JwSport> util = new ExcelUtil<>(JwSport.class);
        List<JwSport> userList = util.importExcel(file.getInputStream());

        for(JwSport jwSport: userList){
            jwSport.setSex(IDCardUtils.getGender(jwSport.getIdCard()));
            jwSport.setAge(IDCardUtils.getAge(jwSport.getIdCard()));
            jwSport.setCreateUserId(9l);
            jwSportService.insertJwSport(jwSport);
        }
        return success();
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSport:export')")
    @Log(title = "选手", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwSport jwSport) {
        List<JwSport> list = jwSportService.selectJwSportList(jwSport);
        ExcelUtil<JwSport> util = new ExcelUtil<JwSport>(JwSport.class);
        util.exportExcel(response, list, "选手数据");
    }

    /**
     * 获取选手详细信息
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwSport:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwSportService.selectJwSportById(id));
    }

    /**
     * 新增选手
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwSport:add')")
    @Log(title = "选手", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody @Validated JwSport jwSport) {
        return toAjax(jwSportService.insertJwSport(jwSport));
    }

    /**
     * 修改选手
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwSport:edit')")
    @Log(title = "选手", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody @Validated JwSport jwSport) {
        return toAjax(jwSportService.updateJwSport(jwSport));
    }

    /**
     * 删除选手
     */
    @PreAuthorize("@ss.hasPermi('jiewu:JwSport:remove')")
    @Log(title = "选手", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwSportService.deleteJwSportByIds(ids));
    }
}
