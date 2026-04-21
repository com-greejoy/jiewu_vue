package com.ruoyi.project.jiewu.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.utils.IDCardUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.jiewu.service.*;
import org.apache.commons.io.FilenameUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/jiewu/JwSignRecord")
public class JwSignRecordController extends BaseController {

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwSignRecordSportService jwSignRecordSportService;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwTeamService jwTeamService;

    @Autowired
    private JwSportService jwSportService;

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(JwSignRecord jwSignRecord) {
        if (!StringUtils.isLongNotNull(jwSignRecord.getMatchId())) {
            return getDataTable(new ArrayList<>());
        }

        // 以组别的形式显示
        List<JwGameItem> list = jwGameItemService.selectJwGameItemListWithTeamSignRecordByMatchId(jwSignRecord.getMatchId(), jwSignRecord.getGameItemId(), jwSignRecord.getTeamId(), jwSignRecord.getBackNumber());
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:list')")
    @GetMapping("/listQiWuMusic")
    public TableDataInfo listQiWuMusic(JwSignRecord jwSignRecord) {
        if (!StringUtils.isLongNotNull(jwSignRecord.getMatchId())) {
            return getDataTable(new ArrayList<>());
        }
        startOrderBy();
        // 以组别的形式显示
        List<JwSignRecord> list = jwSignRecordService.selectQiWuJwSignRecordList(jwSignRecord);
        if (list != null && list.size() > 0) {

            list.forEach(jwSignRecord1 -> jwSignRecord1.setJwSignRecordSportList(jwSignRecordSportService.selectJwSignRecordSportListById(jwSignRecord1.getId())));
        }
        return getDataTable(list);
    }

    // 获取比赛项目的报名数据
    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:list')")
    @PostMapping("/listJwSignRecordByGameItem")
    @ResponseBody
    public AjaxResult listJwSignRecordByGameItem(Long gameItemId, Long scheduleItemId) {
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.selectJwSignRecordListWithAllInfo(gameItemId, scheduleItemId);
        return AjaxResult.success(jwSignRecordList);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:export')")
    @Log(title = "报名记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, JwSignRecord jwSignRecord) {
        List<JwSignRecord> list = jwSignRecordService.selectJwSignRecordList(jwSignRecord);
        List<JwSignRecordExport> res = new ArrayList<>();

        ExcelUtil<JwSignRecordExport> util = new ExcelUtil<JwSignRecordExport>(JwSignRecordExport.class);
        if (list != null && list.size() > 0) {
            for (JwSignRecord jwSignRecord1 : list) {
                JwSignRecordExport jwSignRecordExport = new JwSignRecordExport();
                jwSignRecordExport.setGameItemName(jwGameItemService.selectJwGameItemById(jwSignRecord1.getGameItemId()).getName());
                JwTeam jwTeam = jwTeamService.selectJwTeamById(jwSignRecord1.getTeamId());
                jwSignRecordExport.setTeamName(jwTeam.getTeamName());
                jwSignRecordExport.setBackNumber(jwSignRecord1.getBackNumber());
                jwSignRecordExport.setUserName(jwTeam.getUserName());
                jwSignRecordExport.setUserPhone(jwTeam.getUserPhone());
                List<JwSignRecordSport> jwSignRecordSportList = jwSignRecordSportService.selectJwSignRecordSportListById(jwSignRecord1.getId());

                if (jwSignRecord1.getSportLimit().equals("1")) {
                    jwSignRecordExport.setPlayerNameS(jwSignRecordSportList.get(0).getPlayerName());
                    jwSignRecordExport.setPlayerIdCard(jwSignRecordSportList.get(0).getIdCard());
                    jwSignRecordExport.setPlayerPhone(jwSignRecordSportList.get(0).getPlayerPhone());
                } else {
                    if (jwSignRecordSportList != null && jwSignRecordSportList.size() > 0) {
//                        jwSignRecordExport.setPlayerName(jwSignRecordSportList.stream().map(jwSignRecordSport -> jwSignRecordSport.getPlayerName() + ",身份证," + jwSignRecordSport.getIdCard()).collect(Collectors.joining(";")));
                        jwSignRecordExport.setPlayerName(jwSignRecordSportList.stream().map(JwSignRecordSport::getPlayerName).collect(Collectors.joining(";")));

                    }
                }
                res.add(jwSignRecordExport);
            }
        }
        util.exportExcel(response, res, "报名记录数据");
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(jwSignRecordService.selectJwSignRecordById(id));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:add')")
    @Log(title = "报名记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JwSignRecord jwSignRecord) {
        JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(jwSignRecord.getGameItemId());
        return jwSignRecordService.saveSign(jwGameItem, jwSignRecord.getSportIds(), jwSignRecord.getTeamId(), null, null, null);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:add')")
    @Log(title = "改组", businessType = BusinessType.INSERT)
    @PostMapping("/changeGameItem")
    @ResponseBody
    public AjaxResult changeGameItem(Long id, Long changeGameItemId) {
        jwSignRecordService.changeGameItem(id, changeGameItemId);
        return AjaxResult.success(1);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:add')")
    @Log(title = "改小项", businessType = BusinessType.UPDATE)
    @PostMapping("/changeJwScheduleItem")
    @ResponseBody
    public AjaxResult changeJwScheduleItem(Long id, Long changeScheduleItemId) {
        jwSignRecordService.changeJwScheduleItem(id, changeScheduleItemId);
        return AjaxResult.success(1);
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:edit')")
    @Log(title = "报名记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JwSignRecord jwSignRecord) {
        return toAjax(jwSignRecordService.updateJwSignRecord(jwSignRecord));
    }

    @PreAuthorize("@ss.hasPermi('jiewu:JwSignRecord:remove')")
    @Log(title = "报名记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(jwSignRecordService.deleteJwSignRecordById(id));
    }

    // 报名导入
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, Long matchId) throws Exception {
        if (matchId != null && matchId != 0) {

            ExcelUtil<JwSportImport> util = new ExcelUtil<>(JwSportImport.class);
            List<JwSportImport> userList = util.importExcel(file.getInputStream());

            for (JwSportImport jwSportImport : userList) {

//                jwSportImport.setPlayerGroup(new DecimalFormat("000").format(Long.valueOf(jwSportImport.getPlayerGroup())));
//                String code = jwSportImport.getPlayerGroup().split(":")[0];
//                JwGameItem jwGameItem = jwGameItemService.selectJwGameItemByCode(code, matchId);
                JwGameItem jwGameItem = jwGameItemService.selectJwGameItemByName(jwSportImport.getPlayerGroup(), matchId);
                JwTeam jwTeam = jwTeamService.selectJwTeamByName(jwSportImport.getPlayerTeam());
                List<Long> sportIds = new ArrayList<>();
                String[] playerNames = jwSportImport.getPlayerName().replaceAll(" ", "").split("&");
//                String[] playerNames = jwSportImport.getPlayerName().split(" ");
                if(jwTeam == null){
                    throw new GlobalException(jwSportImport.getPlayerTeam());
                }
                for (String playerName : playerNames) {
                    System.out.println(playerName + "----" + jwSportImport.getPlayerTeam());
                    JwSport jwSport = jwSportService.selectJwSportByName(playerName, null, jwTeam.getCreateUserId());
                    if (jwSport != null && StringUtils.isNotEmpty(jwSport.getPlayerName())) {
                        sportIds.add(jwSport.getId());
                    } else {
                        jwSport = new JwSport();
                        jwSport.setPlayerName(playerName);
                        jwSport.setIdCard(IDCardUtils.RandomIdCard());
                        jwSport.setCreateUserId(jwTeam.getCreateUserId());
                        jwSportService.insertJwSport(jwSport);
                        sportIds.add(jwSport.getId());
                    }
                }
                if (jwGameItem == null) {
                    throw new GlobalException(jwSportImport.getPlayerGroup());
                }
                jwSignRecordService.saveSign(jwGameItem, sportIds.toArray(new Long[0]), jwTeam.getId(), null, jwSportImport.getBackNum(), null);
            }
        }
        return success();
    }

    @PostMapping("/upLoadMusic")
    public AjaxResult upLoadMusic(MultipartFile file, Long jwSignRecordId) {
        String avatar = "";
        try {
            avatar = FileUploadUtils.uploadMatch(RuoYiConfig.getMatchPath() + jwSignRecordService.selectJwSignRecordById(jwSignRecordId).getMatchId(), file, MimeTypeUtils.MEDIA_EXTENSION);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("错误");
        }
        JwSignRecord jwSignRecord = new JwSignRecord();
        jwSignRecord.setId(jwSignRecordId);
        jwSignRecord.setWorksMusic(avatar);
//        jwSignRecord.setWorksName(FilenameUtils.getBaseName(file.getOriginalFilename()));
        jwSignRecord.setWorksMusicName(FilenameUtils.getBaseName(file.getOriginalFilename()) + "." + FilenameUtils.getExtension(file.getOriginalFilename()));
        jwSignRecordService.updateJwSignRecord(jwSignRecord);
        return AjaxResult.success(avatar);
    }
}