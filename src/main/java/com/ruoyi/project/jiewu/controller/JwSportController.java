package com.ruoyi.project.jiewu.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.utils.IDCardUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.project.jiewu.domain.JwSignRecordSport;
import com.ruoyi.project.jiewu.domain.JwSportExport;
import com.ruoyi.project.jiewu.domain.JwTeam;
import com.ruoyi.project.jiewu.service.JwTeamService;
import com.ruoyi.project.system.domain.SysUser;
import me.chanjar.weixin.common.annotation.Required;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private JwTeamService jwTeamService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwSport:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwSport jwSport, Long gameItemId) {

        if (StringUtils.isLongNotNull(gameItemId)) {
            List<JwSport> jwSportList = jwSportService.getWxSportListWithGameItem(jwSport.getCreateUserId(), gameItemId, -999999l, 999999l, null);
            return getDataTable(jwSportList);
        } else {
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

    @PreAuthorize("@ss.hasPermi('jiewu:JwSport:add')")
    @Log(title = "修改选手队伍", businessType = BusinessType.UPDATE)
    @PostMapping("/changeTeam")
    @ResponseBody
    public AjaxResult changeTeam(Long[] sportIds, Long newTeamId) {
        if(sportIds != null && sportIds.length > 0 && newTeamId != null && newTeamId != 0l){
            return AjaxResult.success(jwSportService.changeTeam(sportIds, newTeamId));
        }else{
            return AjaxResult.error("错误");
        }
    }

    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, Long createUserId) throws Exception {
        ExcelUtil<JwSport> util = new ExcelUtil<>(JwSport.class);
        List<JwSport> userList = util.importExcel(file.getInputStream());
        for (JwSport jwSport : userList) {
            JwTeam jwTeam = jwTeamService.selectJwTeamByName(jwSport.getTeamName());
            if(jwTeam == null){
                throw new GlobalException(jwSport.getTeamName());
            }
            JwSport jwSport1 = jwSportService.selectJwSportByName(jwSport.getPlayerName(), null, jwTeam.getCreateUserId());
            if (jwSport1 == null) {
                System.out.println(jwSport.getPlayerName() +"---------------"+ jwSport.getIdCard());
            }else{

                jwSport.setCreateUserId(jwTeamService.selectJwTeamByName(jwSport.getTeamName()).getCreateUserId());
                JwSport up = new JwSport();
                up.setPlayerName(jwSport.getPlayerName());
                up.setPlayerPhone(jwSport.getPlayerPhone());
                up.setIdCard(jwSport.getIdCard());
                up.setId(jwSport1.getId());
                jwSportService.updateJwSport(up);
            }


//            JwSport jwSport1 = jwSportService.selectJwSportByIdCard(jwSport.getIdCard(), null, null);
//            if (jwSport1 == null) {
//                if(StringUtils.isNotEmpty(jwSport.getIdCard()) && jwSport.getIdCard().length() > 15){
//                    try{
//                        jwSport.setSex(IDCardUtils.getGender(jwSport.getIdCard()));
//                        jwSport.setAge(IDCardUtils.getAge(jwSport.getIdCard()));
//                    }catch (Exception e){
//                        System.out.println(jwSport.getIdCard());
//                        throw new GlobalException(jwSport.getIdCard());
//                    }
//                }else{
//                    jwSport.setSex("m");
//                    jwSport.setAge(5l);
//                }
//
//                if (jwSport.getAge() <= 0 || jwSport.getAge() >= 99) {
//                    jwSport.setAge(10l);
//                }
//               JwTeam jwTeam = jwTeamService.selectJwTeamByName(jwSport.getTeamName());
//                if(jwTeam == null){
//                    throw new GlobalException(jwSport.getTeamName());
//                }
//                jwSport.setCreateUserId(jwTeamService.selectJwTeamByName(jwSport.getTeamName()).getCreateUserId());
//
//                jwSportService.insertJwSport(jwSport);
//            }else{
//                System.out.println(jwSport.getPlayerName() +"---------------"+ jwSport.getIdCard());
//            }
        }
        return success();
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSport:export')")
    @Log(title = "选手", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwSport jwSport) {
        List<JwSport> jwSportList = jwSportService.getWxSportListByMatchTeam(jwSport);
        List<JwSportExport> jwSportExportList = new ArrayList<>();
        if (jwSportList != null && jwSportList.size() > 0) {
            jwSportList.forEach(jwSport1 -> {
                jwSportExportList.add(new JwSportExport(jwSport1.getTeamName(), jwSport1.getPlayerName(), jwSport1.getIdCard()));
            });
        }
        jwSportExportList.sort(Comparator.comparing(JwSportExport::getTeamName));

        ExcelUtil<JwSportExport> util = new ExcelUtil<JwSportExport>(JwSportExport.class);

        util.exportExcel(response, jwSportExportList, "报名记录选手数据");
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
