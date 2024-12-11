package com.ruoyi.project.jiewu.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.jiewu.domain.JwEight;
import com.ruoyi.project.jiewu.service.JwEightService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/jwEight")
public class JwEightController extends BaseController {

    @Autowired
    private JwEightService jwEightService;

    @PreAuthorize("@ss.hasPermi('jiewu:jwEight:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwEight jwEight) {
        List<JwEight> list = jwEightService.selectJwEightListWithSport(jwEight);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:jwEight:export')")
    @Log(title = "对阵名单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwEight jwEight) {
        List<JwEight> list = jwEightService.selectJwEightList(jwEight);
        ExcelUtil<JwEight> util = new ExcelUtil<JwEight>(JwEight.class);
        util.exportExcel(response, list, "对阵名单数据");
    }

    // 保存对阵晋级选手
    @PostMapping("/saveEightPro")
    @ResponseBody
    public AjaxResult saveEightPro(Long gameItemId, Long playerId, String playerPosition, String playerIndex) {
        return AjaxResult.success(jwEightService.saveEightPro(gameItemId, playerId, playerPosition, playerIndex));
    }

    @PostMapping("/clearEightPro")
    @ResponseBody
    public AjaxResult clearEightPro(Long gameItemId, String playerPosition) {
        return AjaxResult.success(jwEightService.clearEightPro(gameItemId, playerPosition));
    }



    @PreAuthorize("@ss.hasPermi('jiewu:jwEight:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwEightService.selectJwEightById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:jwEight:add')")
    @Log(title = "对阵名单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwEight jwEight) {
        return toAjax(jwEightService.insertJwEight(jwEight));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:jwEight:edit')")
    @Log(title = "对阵名单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwEight jwEight) {
        return toAjax(jwEightService.updateJwEight(jwEight));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:jwEight:remove')")
    @Log(title = "对阵名单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwEightService.deleteJwEightByIds(ids));
    }
}
