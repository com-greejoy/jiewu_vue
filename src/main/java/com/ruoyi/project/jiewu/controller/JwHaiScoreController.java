package com.ruoyi.project.jiewu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.PdfGenerator;
import com.ruoyi.project.jiewu.domain.JwSignRecord;
import com.ruoyi.project.jiewu.domain.JwSignRecordSportExport;
import com.ruoyi.project.jiewu.service.JwMatchService;
import com.ruoyi.project.jiewu.service.JwSignRecordService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.jiewu.domain.JwHaiScore;
import com.ruoyi.project.jiewu.service.JwHaiScoreService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

@RestController
@RequestMapping("/jiewu/JwHaiScore")
public class JwHaiScoreController extends BaseController {

    @Autowired
    private JwHaiScoreService jwHaiScoreService;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwMatchService jwMatchService;



    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:list')")
    @GetMapping("/list")
    public AjaxResult list(JwSignRecord jwSignRecord) {
        if (StringUtils.isLongNotNull(jwSignRecord.getGameItemId())) {
            List<JwSignRecord> list = jwSignRecordService.selectJwSignRecordHaiScore(jwSignRecord);
            return AjaxResult.success(list);
        } else {
            return AjaxResult.success(new ArrayList<>());
        }
    }

    //    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:list')")
    @PostMapping("/listJwHaiScoreBySportrrr")
    @ResponseBody
    public AjaxResult listJwHaiScoreBySportrrr(JwSignRecord jwSignRecord) {
        if (StringUtils.isLongNotNull(jwSignRecord.getId())) {
            return AjaxResult.success(jwSignRecordService.selectJwSignRecordHaiScore(jwSignRecord));
        } else {
            return AjaxResult.success(0);
        }
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:export')")
    @Log(title = "海选打分", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwHaiScore jwHaiScore) {
        List<JwHaiScore> list = jwHaiScoreService.selectJwHaiScoreList(jwHaiScore);
        ExcelUtil<JwHaiScore> util = new ExcelUtil<JwHaiScore>(JwHaiScore.class);
        util.exportExcel(response, list, "海选打分数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:export')")
    @Log(title = "全部成绩表", businessType = BusinessType.EXPORT)
    @PostMapping("/exportAllGrade")
    public void exportAllGrade(HttpServletResponse response, Long matchId) {
        List<JwSignRecord> jwSignRecordList = jwHaiScoreService.listAllGameItemGradeDes(matchId, null);
        List<JwSignRecordSportExport> jwSignRecordSportExportList = new ArrayList<>();

        jwSignRecordList.forEach(jwSignRecord -> {
            jwSignRecord.getJwSignRecordSportList().forEach(jwSignRecordSport -> {
                jwSignRecordSportExportList.add(new JwSignRecordSportExport(
                        jwSignRecord.getTeamName(),
                        jwSignRecordSport.getPlayerName(),
                        jwSignRecordSport.getIdCard(),
                        jwSignRecordSport.getPlayerPhone(),
                        jwSignRecord.getAvgScore(),
                        jwSignRecord.getRankOrder(),
                        jwSignRecord.getJwGameItem().getName()));
            });
        });
        jwSignRecordSportExportList.sort(Comparator.comparing(JwSignRecordSportExport::getPlayerName));

        ExcelUtil<JwSignRecordSportExport> util = new ExcelUtil<JwSignRecordSportExport>(JwSignRecordSportExport.class);
        util.exportExcel(response, jwSignRecordSportExportList, "全部成绩表");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:export')")
    @Log(title = "全部成绩", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadAllScore")
    public ResponseEntity<byte[]> downloadAllScore(Long matchId) {
        List<JwSignRecord> jwSignRecordList = jwHaiScoreService.listAllGameItemGradeDes(matchId, null);
        try {
            // 调用服务生成Word文档并返回字节数组
            byte[] wordBytes = PdfGenerator.generateGradeWord(jwSignRecordList, jwMatchService.selectJwMatchById(matchId).getMatchName());
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "schedule.docx");
            // 返回带有字节数据和响应头的ResponseEntity
            return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // 处理异常情况
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:export')")
    @Log(title = "组别成绩", businessType = BusinessType.EXPORT)
    @PostMapping("/downloadGameItemScore")
    public ResponseEntity<byte[]> downloadGameItemScore(Long matchId, Long gameItemId) {
        List<JwSignRecord> jwSignRecordList = jwHaiScoreService.listAllGameItemGradeDes(matchId, gameItemId);
        try {
            // 调用服务生成Word文档并返回字节数组
            byte[] wordBytes = PdfGenerator.generateGradeWord(jwSignRecordList, jwMatchService.selectJwMatchById(matchId).getMatchName());
            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "schedule.docx");
            // 返回带有字节数据和响应头的ResponseEntity
            return new ResponseEntity<>(wordBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            // 处理异常情况
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwHaiScoreService.selectJwHaiScoreById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:add')")
    @Log(title = "海选打分", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwHaiScore jwHaiScore) {
        return toAjax(jwHaiScoreService.saveJudgeScore(jwHaiScore.getJudgeId(), jwHaiScore.getScore(), jwHaiScore.getSportId()));
    }


    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:add')")
    @Log(title = "计算成绩", businessType = BusinessType.INSERT)
    @PostMapping("/jiSuanGameItem")
    @ResponseBody
    public AjaxResult jiSuanGameItem(Long gameItemId, String type) {
        return toAjax(jwHaiScoreService.jiSuanGameItem(gameItemId, type));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:add')")
    @Log(title = "修改排名", businessType = BusinessType.INSERT)
    @PostMapping("/saveCustomOrder")
    @ResponseBody
    public AjaxResult saveCustomOrder(Long id, Long rankOrder) {
        return toAjax(jwSignRecordService.saveCustomOrder(id, rankOrder));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:add')")
    @Log(title = "海选完成", businessType = BusinessType.INSERT)
    @PostMapping("/haiXuanComplete")
    @ResponseBody
    public AjaxResult haiXuanComplete(Long id) {
        return jwHaiScoreService.haiXuanComplete(id);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:add')")
    @Log(title = "获取项目成绩描述", businessType = BusinessType.INSERT)
    @PostMapping("/listGameItemGradeDes")
    @ResponseBody
    public AjaxResult listGameItemGradeDes(JwSignRecord jwSignRecord) {
        return AjaxResult.success(jwHaiScoreService.listGameItemGradeDes(jwSignRecord));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:add')")
    @Log(title = "获取比赛全部项目成绩描述", businessType = BusinessType.INSERT)
    @PostMapping("/listAllGameItemGradeDes")
    @ResponseBody
    public AjaxResult listAllGameItemGradeDes(Long matchId) {
        return AjaxResult.success(jwHaiScoreService.listAllGameItemGradeDes(matchId, null));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:add')")
    @Log(title = "获取代表队成绩统计", businessType = BusinessType.INSERT)
    @PostMapping("/listTeamGradeDes")
    @ResponseBody
    public AjaxResult listTeamGradeDes(JwSignRecord jwSignRecord) {
        return AjaxResult.success(jwHaiScoreService.listTeamGradeDes(jwSignRecord));
    }


    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:edit')")
    @Log(title = "海选打分", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwHaiScore jwHaiScore) {
        return toAjax(jwHaiScoreService.updateJwHaiScore(jwHaiScore));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwHaiScore:remove')")
    @Log(title = "海选打分", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(jwHaiScoreService.deleteJwHaiScoreByIds(ids));
    }
}
