package com.ruoyi.project.jiewu.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.IDCardUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.jiewu.service.JwScheduleItemService;
import com.ruoyi.project.jiewu.service.JwSignRecordSportService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.jiewu.service.JwGameItemService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/jiewu/JwGameItem")
public class JwGameItemController extends BaseController {

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwScheduleItemService jwScheduleItemService;

    @Autowired
    private JwSignRecordSportService jwSignRecordSportService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwGameItem jwGameItem) {
        startPage();
        List<JwGameItem> list = jwGameItemService.selectJwGameItemListWithCount(jwGameItem);

        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:export')")
    @Log(title = "比赛项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwGameItem jwGameItem) {
        List<JwGameItem> list = jwGameItemService.selectJwGameItemList(jwGameItem);
        ExcelUtil<JwGameItem> util = new ExcelUtil<JwGameItem>(JwGameItem.class);
        util.exportExcel(response, list, "比赛项目数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:export')")
    @Log(title = "比赛项目", businessType = BusinessType.EXPORT)
    @PostMapping("/handleExportGameItemCount")
    public void handleExportGameItemCount(HttpServletResponse response, JwGameItem jwGameItem) {
        List<JwGameItem> list = jwGameItemService.selectJwGameItemListWithCount(jwGameItem);
        List<JwGameItemCount> jwGameItemCountList = list.stream()
                .map(item -> {
                    JwSignRecordSport jwSignRecordSport = new JwSignRecordSport();
                    jwSignRecordSport.setGameItemId(item.getId());
                    jwSignRecordSport.setMatchId(item.getMatchId());
                    return new JwGameItemCount(item.getCode(), item.getName(), item.getSignCount(), jwSignRecordSportService.selectJwSignRecordSportList(jwSignRecordSport).size());
                }).collect(Collectors.toList());;
        ExcelUtil<JwGameItemCount> util = new ExcelUtil<>(JwGameItemCount.class);
        util.exportExcel(response, jwGameItemCountList, "组别报名数量");
    }

    // 比赛项目重新分组
    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:edit')")
    @Log(title = "比赛项目重新分组", businessType = BusinessType.UPDATE)
    @PostMapping("/reGroupJwGameItem")
    @ResponseBody
    public AjaxResult reGroupJwGameItem(Long id) {
        JwGameItem queryItem = new JwGameItem();
        queryItem.setId(id);
        List<JwGameItem> jwGameItemList = jwGameItemService.selectJwGameItemListWithCount(queryItem);

        if(jwGameItemList != null && jwGameItemList.size() > 0){
            return AjaxResult.success(jwScheduleItemService.reGroupJwGameItem(jwGameItemList.get(0)));
        }
        return AjaxResult.error("没有选择组别");
    }

    // 锁定一个项目打分
    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:edit')")
    @Log(title = "锁定一个项目打分", businessType = BusinessType.UPDATE)
    @PostMapping("/lockJwGameItem")
    @ResponseBody
    public AjaxResult lockJwGameItem(Long id) {
        if(StringUtils.isLongNotNull(id)){
            return AjaxResult.success(jwScheduleItemService.lockScoreByGameItem(id));
        }else{
            return AjaxResult.success(1);
        }
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwGameItemService.selectJwGameItemById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:add')")
    @Log(title = "比赛项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwGameItem jwGameItem) {
        return toAjax(jwGameItemService.insertJwGameItem(jwGameItem));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:edit')")
    @Log(title = "比赛项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwGameItem jwGameItem) {
        return toAjax(jwGameItemService.updateJwGameItem(jwGameItem));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwGameItem:remove')")
    @Log(title = "比赛项目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwGameItemService.deleteJwGameItemByIds(ids));
    }


    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {

            ExcelUtil<JwGameItem> util = new ExcelUtil<>(JwGameItem.class);
            List<JwGameItem> jwGameItemList = util.importExcel(file.getInputStream());

            for (JwGameItem jwGameItem : jwGameItemList) {
                jwGameItem.setShowMinYear(jwGameItem.getMinYear());
                jwGameItem.setShowMaxYear(jwGameItem.getMaxYear());

                // ------ 李金鑫的 年龄上下浮动2岁
                jwGameItem.setMinYear(jwGameItem.getMinYear() - 2);
                jwGameItem.setMaxYear(jwGameItem.getMaxYear() + 2);
                // ------  李金鑫的 年龄上下浮动2岁



                if(jwGameItem.getName().contains("团体") || jwGameItem.getName().contains("齐舞")){
                    jwGameItem.setSportLimit("3"); //比赛模式
                }else{
                    jwGameItem.setSportLimit("1"); //项目类型
                }

                jwGameItem.setMatchType("1"); //比赛模式
                jwGameItem.setGroupMode("1"); // 分组模式
                jwGameItem.setGroupLimit(1l);
                jwGameItem.setSingleDuration(60l);
                jwGameItem.setSexCon("0");
                jwGameItem.setMinSport(1);
                jwGameItem.setMaxSport(1);
//                if(jwGameItem.getRemark().contains("小齐舞")){
//                    jwGameItem.setMinSport(3);
//                    jwGameItem.setMaxSport(5);
//                }
                if(jwGameItem.getRemark().contains("齐舞")){
                    jwGameItem.setMinSport(5);
                    jwGameItem.setMaxSport(99);
                }
//                if(jwGameItem.getRemark().contains("六人一组")){
//                    jwGameItem.setMinSport(6);
//                    jwGameItem.setMaxSport(6);
//                }
//                if(Long.valueOf(jwGameItem.getCode()) <= 27){
//                    jwGameItem.setMinSport(1);
//                    jwGameItem.setMaxSport(99);
//                }
                jwGameItem.setCode(new DecimalFormat("000").format(Long.valueOf(jwGameItem.getCode())));
                jwGameItemService.insertJwGameItem(jwGameItem);
            }

        return success();
    }
}