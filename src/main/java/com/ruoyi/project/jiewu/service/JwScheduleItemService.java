package com.ruoyi.project.jiewu.service;

import java.text.DecimalFormat;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.jiewu.domain.*;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysDictData;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwScheduleItemMapper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwScheduleItemService {

    @Autowired
    private JwScheduleItemMapper jwScheduleItemMapper;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwScheduleInfoService jwScheduleInfoService;

    @Autowired
    private JwSchedulePlaceService jwSchedulePlaceService;

    @Autowired
    private JwMatchService jwMatchService;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    // 获取比赛项目的小项
    public List<JwScheduleItem> selectJwScheduleItemByGameItemId(Long gameItemId) {
        return jwScheduleItemMapper.selectJwScheduleItemByGameItemId(gameItemId);
    }

    public JwScheduleItem selectJwScheduleItemById(Long id) {
        return jwScheduleItemMapper.selectJwScheduleItemById(id);
    }

    public List<JwScheduleItem> selectJwScheduleItemList(JwScheduleItem jwScheduleItem) {
        return jwScheduleItemMapper.selectJwScheduleItemList(jwScheduleItem);
    }

    // 获取没有分配场次的小项
    public List<JwScheduleItem> listNoPlaceJwScheduleItem(Long matchId) {
        return jwScheduleItemMapper.listNoPlaceJwScheduleItem(matchId);
    }

    // 计算时间
    @Transactional
    public AjaxResult calculateTime(Long matchId) {

        List<JwScheduleInfo> jwScheduleInfoList = jwScheduleInfoService.selectJwScheduleInfoByMatchId(matchId);

        if (jwScheduleInfoList != null && jwScheduleInfoList.size() > 0) {
            jwScheduleInfoList.forEach(jwScheduleInfo -> {
                JwSchedulePlace query = new JwSchedulePlace();
                query.setMatchId(matchId);
                query.setScheduleInfoId(jwScheduleInfo.getId());
                List<JwSchedulePlace> jwSchedulePlaceList = jwSchedulePlaceService.listJwSchedulePlaceWithScheduleItem(query);

                Date beginTime = jwScheduleInfo.getBeginTime();

                // 计算每场的时间
                if (jwSchedulePlaceList != null && jwSchedulePlaceList.size() > 0) {
                    for (JwSchedulePlace jwSchedulePlace : jwSchedulePlaceList) {
                        JwSchedulePlace update = new JwSchedulePlace();
                        update.setId(jwSchedulePlace.getId());
                        update.setPlaceTime(beginTime);

                        // 计算场次里面的最长时间
                        List<JwScheduleItem> jwScheduleItemList = jwSchedulePlace.getJwScheduleItemList();
                        int placeTime = 0;
                        if (jwScheduleItemList != null && jwScheduleItemList.size() > 0) {
                            for (JwScheduleItem jwScheduleItem : jwScheduleItemList) {
                                JwGameItem jwGameItem = jwGameItemService.selectJwGameItemById(jwScheduleItem.getGameItemId());
                                List<JwSignRecord> jwSignRecordList = jwSignRecordService.selectJwSignRecordListByScheduleItem(jwScheduleItem.getId());
                                if ("1".equals(jwGameItem.getSportLimit())) {
                                    // 单人
                                    placeTime = jwSignRecordList.size() * 60;
                                } else if ("3".equals(jwGameItem.getSportLimit())) {
                                    // 齐舞
                                    placeTime = jwSignRecordList.size() * 180;
                                }
                            }
                        }

                        placeTime += 120;

                        jwSchedulePlaceService.updateJwSchedulePlace(update);

                        beginTime = DateUtils.addSeconds(beginTime, placeTime);
                    }
                }
            });
        }

        return AjaxResult.success(1);
    }

    // 初始化赛程小项
    @Transactional
    public AjaxResult initScheduleItem(Long matchId) {

        JwMatch jwMatch = jwMatchService.selectJwMatchById(matchId);
        if (jwMatch == null) {
            return AjaxResult.error("请选择比赛");
        }
        // 1. 判断可不可以初始化小项  有没有打分数据

        // 2.清除该比赛的小项数据
        jwScheduleItemMapper.clearMatchScheduleItem(matchId);

        // 3.获取该比赛的所有比赛项目
        JwGameItem queryItem = new JwGameItem();
        queryItem.setMatchId(matchId);
        List<JwGameItem> jwGameItemList = jwGameItemService.selectJwGameItemListWithCount(queryItem);

        // 分组 分组的时候根据分组数 先自动分ABCD场，然后再手动调整
        List<SysDictData> sysDictDataList = DictUtils.getDictCache("jw_area");
        // 4.根据比赛项目生成小项
        if (jwGameItemList != null && jwGameItemList.size() > 0) {
            for (JwGameItem jwGameItem : jwGameItemList) {

                generateScheduleItem(jwGameItem);
            }
        }

        return AjaxResult.success();
    }

    // 初始化背号
    public AjaxResult initBackNum(Long matchId) {
        Long startBackNum = 1l;

        // 清空所有背号
        jwSignRecordService.clearAllBackNum(matchId);

        // 获取所有报名记录  根据代表队排序
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.selectJwSignRecordListByMatchId(matchId);

        for (JwSignRecord jwSignRecord : jwSignRecordList) {
            JwSignRecord jwSignRecord1 = new JwSignRecord();
            jwSignRecord1.setId(jwSignRecord.getId());
            if("1".equals(jwSignRecord.getSportLimit())){
                // 单人的背号，先判断他有没有背号，没有就加一  有就用原来的
                if(jwSignRecord.getJwSignRecordSportList() != null && jwSignRecord.getJwSignRecordSportList().size() > 0){
                    JwSignRecordSport jwSignRecordSport = jwSignRecord.getJwSignRecordSportList().get(0);
                    List<JwSignRecord> hasBackNumList = jwSignRecordService.selectJwSignRecordBySportId(matchId, jwSignRecordSport.getSportId());

                    if(hasBackNumList != null && hasBackNumList.size() > 0){
                        JwSignRecord b = hasBackNumList.get(0);
                        if(StringUtils.isNotEmpty(b.getBackNumber())){
                            jwSignRecord1.setBackNumber(b.getBackNumber());
//                            jwSignRecord1.setBackNumber(new DecimalFormat("0000").format(startBackNum));
//                            startBackNum++;
                        }else{
                            jwSignRecord1.setBackNumber(new DecimalFormat("0000").format(startBackNum));
                            startBackNum++;
                        }
                    }else{
                        jwSignRecord1.setBackNumber(new DecimalFormat("0000").format(startBackNum));
                        startBackNum++;
                    }
                }else{
                    jwSignRecord1.setBackNumber(new DecimalFormat("0000").format(startBackNum));
                    startBackNum++;
                }
//                jwSignRecordService.selectbackNumBySport();
            }else if("3".equals(jwSignRecord.getSportLimit())){
                // 齐舞的背号 直接加一
                jwSignRecord1.setBackNumber(new DecimalFormat("0000").format(startBackNum));
                startBackNum++;
            }
            jwSignRecordService.updateJwSignRecord(jwSignRecord1);
        }

        return AjaxResult.success();
    }

    private void generateScheduleItem(JwGameItem jwGameItem) {
        Long signCount = jwGameItem.getSignCount();
        // 组别的报名数据
        List<JwSignRecord> jwSignRecordList = jwSignRecordService.selectJwSignRecordListWithUserGameItem(null, jwGameItem.getId(), null);

        if (StringUtils.isLongNotNull(signCount) && jwSignRecordList != null && jwSignRecordList.size() > 0) {

            // 顺序打乱
            if ("1".equals(jwGameItem.getGroupMode())) {
                //全随机
                Collections.shuffle(jwSignRecordList);
            } else if ("2".equals(jwGameItem.getGroupMode())) {
                // 代表队一起, 按照代表队排序
                jwSignRecordList.sort(Comparator.comparing(JwSignRecord::getTeamId));
            } else if ("3".equals(jwGameItem.getGroupMode())) {
                // 随机代表队分开
                // 1.先随机份
                Collections.shuffle(jwSignRecordList);
                // 2.整理代表队连起的
                List<JwSignRecord> newList = new ArrayList<>();
                // 添加第一个
                int size = jwSignRecordList.size();
                newList.add(jwSignRecordList.get(0));
                jwSignRecordList.remove(0);
                if(jwSignRecordList.size() > 0){
                    for(int i = 1; i < size; i++){
                        int ii = i - 1;
                        newList.add(jwSignRecordList.stream().filter(jwSignRecord -> !jwSignRecord.getTeamId().equals(newList.get(ii).getTeamId())).findFirst().orElse(jwSignRecordList.get(0)));
                        jwSignRecordList.remove(newList.get(i));
                    }
                }
                jwSignRecordList.addAll(newList);
            }
            // 每一组的人数
            Long groupNum = Math.floorDiv(jwSignRecordList.size(), jwGameItem.getGroupLimit());
            Long yuShu = jwSignRecordList.size() % jwGameItem.getGroupLimit();
            int start = 0;
            // 单海选 根据 报名数量 和 分组数量 分组就行了
            for (int i = 1; i <= jwGameItem.getGroupLimit(); i++) {
                JwScheduleItem jwScheduleItem = new JwScheduleItem();
                //  matchId 比赛; gameItemId 项目; itemName 小项;
                //  schedulePlaceId 场次; scheduleInfoId 阶段; area 场地; shceduleTime 开始时间;
                jwScheduleItem.setMatchId(jwGameItem.getMatchId());
                jwScheduleItem.setGameItemId(jwGameItem.getId());
                jwScheduleItem.setArea("" + i); // 默认场地
                jwScheduleItem.setItemProcess("1");
                String groupStr = jwGameItem.getGroupLimit() > 1 ? "第" + i + "组" : "";
                if ("2".equals(jwGameItem.getMatchType())) {
                    jwScheduleItem.setItemName(jwGameItem.getCode() + ":" + jwGameItem.getName() + "海选" + groupStr);
                } else {
                    jwScheduleItem.setItemName(jwGameItem.getCode() + ":" + jwGameItem.getName() + "决赛" + groupStr);
                }
                // 插入小项
                insertJwScheduleItem(jwScheduleItem);
                // 分配选手
                List<JwSignRecord> groupSignRecordList = null;

                if (i == jwGameItem.getGroupLimit()) {
                    // 最后一组
                    groupSignRecordList = jwSignRecordList.subList(0 , jwSignRecordList.size());
                } else {
                    groupSignRecordList = jwSignRecordList.subList(0, groupNum.intValue() + (yuShu > 0 ? 1 : 0));
                    yuShu --;
                }
                if (groupSignRecordList.size() >= 0) {
                    for (int indexOrder = 0; indexOrder < groupSignRecordList.size(); indexOrder++) {
                        JwSignRecord groupSign = groupSignRecordList.get(indexOrder);
                        JwSignRecord updateD = new JwSignRecord();
                        updateD.setId(groupSign.getId());
                        updateD.setScheduleItemId(jwScheduleItem.getId());
                        updateD.setIndexOrder(Long.valueOf(indexOrder) + 1);
                        jwSignRecordService.updateJwSignRecord(updateD);
                    }
                }
                jwSignRecordList.removeAll(groupSignRecordList);
            }
            // 如果有决赛 生成决赛小项
            if ("2".equals(jwGameItem.getMatchType())) {
                JwScheduleItem jwScheduleItemFinal = new JwScheduleItem();
                jwScheduleItemFinal.setMatchId(jwGameItem.getMatchId());
                jwScheduleItemFinal.setGameItemId(jwGameItem.getId());
                jwScheduleItemFinal.setItemName(jwGameItem.getCode() + ":" + jwGameItem.getName() + "决赛");
                jwScheduleItemFinal.setArea("1"); // 默认到A场地
                jwScheduleItemFinal.setItemProcess("2");
                insertJwScheduleItem(jwScheduleItemFinal);
            }
        }
    }

    public int reGroupJwGameItem(JwGameItem jwGameItem) {
        // 清除之前的小项
        deleteJwScheduleItemByGameItem(jwGameItem.getId());

        // 生成新的小项
        generateScheduleItem(jwGameItem);

        return 1;
    }

    public int insertJwScheduleItem(JwScheduleItem jwScheduleItem) {
        return jwScheduleItemMapper.insertJwScheduleItem(jwScheduleItem);
    }

    // 更新小项 的 场地 单元
    @Transactional
    public int updateJwScheduleItemPlace(Long[] ids, Long scheduleInfoId, Long schedulePlaceId) {
        return jwScheduleItemMapper.updateJwScheduleItemPlace(ids, scheduleInfoId, schedulePlaceId);
    }

    // 清空一个场次的小项
    public int clearSchedulePlace(Long schedulePlaceId) {
        return jwScheduleItemMapper.clearSchedulePlace(schedulePlaceId);
    }

    // 从场地中删除小项
    public int clearSchedulePlaceById(Long id) {
        return jwScheduleItemMapper.clearSchedulePlaceById(id);
    }

    // 清空一个项目的小项，准备重新分组
    public int deleteJwScheduleItemByGameItem(Long gameItemId) {
        return jwScheduleItemMapper.deleteJwScheduleItemByGameItem(gameItemId);
    }

    public int updateJwScheduleItem(JwScheduleItem jwScheduleItem) {
        return jwScheduleItemMapper.updateJwScheduleItem(jwScheduleItem);
    }

    public int deleteJwScheduleItemByIds(Long[] ids) {
        return jwScheduleItemMapper.deleteJwScheduleItemByIds(ids);
    }

    public int deleteJwScheduleItemById(Long id) {
        return jwScheduleItemMapper.deleteJwScheduleItemById(id);
    }
}