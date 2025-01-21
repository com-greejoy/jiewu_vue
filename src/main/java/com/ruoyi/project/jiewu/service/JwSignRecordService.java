package com.ruoyi.project.jiewu.service;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.exception.GlobalException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.jiewu.domain.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwSignRecordMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.Joinable;

@Service
public class JwSignRecordService {

    @Autowired
    private JwSignRecordMapper jwSignRecordMapper;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @Autowired
    private JwMatchTeamService jwMatchTeamService;

    @Autowired
    private JwMatchService jwMatchService;

    @Autowired
    private JwSportService jwSportService;

    @Autowired
    private JwSignRecordSportService jwSignRecordSportService;

    @Autowired
    private JwScheduleItemService jwScheduleItemService;

    public static String T = "000";

    @Transactional
    public int changeGameItem(Long id, Long changeGameItemId) {
        // 单人组别 判断下 改的组是不是已经存在 该选手
        JwSignRecord jwSignRecord = jwSignRecordService.selectJwSignRecordById(id);
        Long oldGameItemId = jwSignRecord.getGameItemId();
        if ("1".equals(jwSignRecord.getSportLimit())) {

            List<JwSignRecordSport> jwSignRecordSportList = jwSignRecordSportService.selectJwSignRecordSportListById(id);
            JwSignRecordSport jwSignRecordSport = jwSignRecordSportList.get(0);

            JwSignRecordSport query = new JwSignRecordSport();
            query.setTeamId(jwSignRecord.getTeamId());
            query.setGameItemId(changeGameItemId);
            query.setMatchId(jwSignRecord.getMatchId());
            query.setSportId(jwSignRecordSport.getSportId());

            List<JwSignRecordSport> jwSignRecordSportListOld = jwSignRecordSportService.selectJwSignRecordSportList(query);
            if (jwSignRecordSportListOld != null && jwSignRecordSportListOld.size() > 0) {
                throw new GlobalException("新的组别已经有该选手");
            }
        }
        int re = jwSignRecordMapper.changeGameItem(id, changeGameItemId);

        // 改完组之后, 如果已经分组，还要把小项一起改了
        addNewSignToScheduleItem(jwSignRecord, jwGameItemService.selectJwGameItemById(changeGameItemId));

        // 改完组之后，如果已经分组, 把组别的排序重新整理
//        if ("1".equals(jwSignRecord.getSportLimit())) {
            // 单人的直接排序 齐舞的手动排
//            arrangeOrder(oldGameItemId);
//        }

        return re;
    }

    // 重新整理组别里面的选手排序
    public void arrangeOrder(Long gameItemId){
        List<JwScheduleItem> jwScheduleItemList = jwScheduleItemService.selectJwScheduleItemByGameItemId(gameItemId);
        if(jwScheduleItemList != null && jwScheduleItemList.size() > 0){
            jwScheduleItemList.forEach(jwScheduleItem -> {
                List<JwSignRecord> jwSignRecordList = selectJwSignRecordListByScheduleItem(jwScheduleItem.getId());
                jwSignRecordList.sort(Comparator.comparing(JwSignRecord::getIndexOrder));
                for(int i = 1; i <= jwSignRecordList.size(); i++){
                    JwSignRecord updateD = new JwSignRecord();
                    updateD.setId(jwSignRecordList.get(i-1).getId());
                    updateD.setIndexOrder(Long.valueOf(i));
                    updateJwSignRecord(updateD);
                }
            });
        }
    }

    // 修改分组小项
    public int changeJwScheduleItem(Long id, Long changeScheduleItemId) {

        List<JwSignRecord> jwSignRecordList = selectJwSignRecordListByScheduleItem(changeScheduleItemId);
        JwSignRecord jwSignRecord = jwSignRecordList.stream().max(Comparator.comparingLong(JwSignRecord::getIndexOrder)).get();
        JwSignRecord updateD = new JwSignRecord();
        updateD.setId(id);
        updateD.setScheduleItemId(changeScheduleItemId);
        updateD.setIndexOrder(jwSignRecord.getIndexOrder() + 1l);
        updateJwSignRecord(updateD);

        // 如果已经分组, 把组别的排序重新整理
//        arrangeOrder(jwSignRecord.getGameItemId());

        return 1;
    }

    public JwSignRecord selectJwSignRecordById(Long id) {
        return jwSignRecordMapper.selectJwSignRecordById(id);
    }

    public List<JwSignRecord> selectJwSignRecordList(JwSignRecord jwSignRecord) {
        return jwSignRecordMapper.selectJwSignRecordList(jwSignRecord);
    }

    // 获取代表队 某个组别的报名信息
    public List<JwSignRecord> selectJwSignRecordListWithUserGameItem(Long teamId, Long gameItemId, String backNumber) {
        return jwSignRecordMapper.selectJwSignRecordListWithUserGameItem(teamId, gameItemId, backNumber);
    }

    // 获取比赛项目的报名数据  代表队  赛程小项  选手
    public List<JwSignRecord> selectJwSignRecordListWithAllInfo(Long gameItemId, Long scheduleItemId) {
        return jwSignRecordMapper.selectJwSignRecordListWithAllInfo(gameItemId, scheduleItemId);
    }

    // 获取赛程小项的名单
    public List<JwSignRecord> selectJwSignRecordListByScheduleItem(Long scheduleItemId) {
        return jwSignRecordMapper.selectJwSignRecordListByScheduleItem(scheduleItemId);
    }

    // 获取比赛项目的选手名单
    public List<JwSignRecord> selectJwSignRecordListByGameItem(Long gameItemId) {
        return jwSignRecordMapper.selectJwSignRecordListByGameItem(gameItemId);
    }

    //  获取代表队 比赛的所有报名信息
    public List<JwSignRecord> selectJwSignRecordListWithUserMatch(Long teamId, Long matchId) {
        return jwSignRecordMapper.selectJwSignRecordListWithUserMatch(teamId, matchId);
    }

    public List<JwSignRecord> selectJwSignRecordListByMatchId(Long matchId) {
        return jwSignRecordMapper.selectJwSignRecordListByMatchId(matchId);
    }

    // 获取 单人 运动员 得所有记录
    public List<JwSignRecord> selectJwSignRecordBySportId(Long matchId, Long sportId) {
        return jwSignRecordMapper.selectJwSignRecordBySportId(matchId, sportId);
    }

    // 查询比赛最大背号
    public String selectMaxBackNum(Long matchId) {
        return jwSignRecordMapper.selectMaxBackNum(matchId);
    }

    // 获取代表队全部赛程
    public List<JwSignRecord> getTeamScheduleInfoList(Long matchId, Long teamId) {
        return jwSignRecordMapper.getTeamScheduleInfoList(matchId, teamId);
    }

    // 获取代表队所有背号
    public List<String> selectSignBackNumList(Long matchId, Long teamId) {
        return jwSignRecordMapper.selectSignBackNumList(matchId, teamId);
    }

    // 根据背号获取赛程
    public List<JwSignRecord> selectScheduleByBackNum(Long matchId, String backNumber) {
        return jwSignRecordMapper.selectScheduleByBackNum(matchId, backNumber);
    }

    //获取齐舞的报名记录
    public List<JwSignRecord> listMatchQiWuSignRecord(Long teamId, Long matchId) {
        return jwSignRecordMapper.listMatchQiWuSignRecord(teamId, matchId);
    }

    public Long selectJwSignRecordSportNum(Long teamId, Long matchId) {
        return jwSignRecordMapper.selectJwSignRecordSportNum(teamId, matchId);
    }

    public Long selectJwSignRecordSportTime(Long teamId, Long matchId) {
        return jwSignRecordMapper.selectJwSignRecordSportTime(teamId, matchId);
    }

    @Transactional
    public AjaxResult saveSign(JwGameItem jwGameItem, Long[] sportIds, Long teamId, Long editId, String backNum) {

        if (jwGameItem != null && sportIds != null && sportIds.length > 0) {

            // 增加比赛报名队伍记录
            JwMatchTeam jwMatchTeam = jwMatchTeamService.getJwMatchTeam(jwGameItem.getMatchId(), teamId);
            if (jwMatchTeam == null) {
                jwMatchTeam = new JwMatchTeam();
                jwMatchTeam.setMatchId(jwGameItem.getMatchId());
                jwMatchTeam.setTeamId(teamId);
                Long lastOrder = jwMatchTeamService.getLastOrder(jwGameItem.getMatchId());
                if (!StringUtils.isLongNotNull(lastOrder)) {
                    jwMatchTeam.setIndexOrder(1l);
                } else {
                    jwMatchTeam.setIndexOrder(lastOrder + 1);
                }
                jwMatchTeamService.insertJwMatchTeam(jwMatchTeam);
            }

            //  单人报名
            if ("1".equals(jwGameItem.getSportLimit())) {
                for (Long sportId : sportIds) {
                    // 判断有没有报过该组别
                    JwSignRecordSport query = new JwSignRecordSport();
                    query.setTeamId(teamId);
                    query.setGameItemId(jwGameItem.getId());
                    query.setMatchId(jwGameItem.getMatchId());
                    query.setSportId(sportId);
                    List<JwSignRecordSport> jwSignRecordSportList = jwSignRecordSportService.selectJwSignRecordSportList(query);

                    if (jwSignRecordSportList == null || jwSignRecordSportList.size() <= 0) {
                        JwSignRecord jwSignRecord = new JwSignRecord();
                        jwSignRecord.setMatchId(jwGameItem.getMatchId());
                        jwSignRecord.setSportLimit(jwGameItem.getSportLimit());
                        jwSignRecord.setGameItemId(jwGameItem.getId());
                        jwSignRecord.setTeamId(teamId);
                        jwSignRecord.setBackNumber(backNum);
                        insertJwSignRecord(jwSignRecord);

                        JwSignRecordSport jwSignRecordSport = new JwSignRecordSport();
                        jwSignRecordSport.setMatchId(jwGameItem.getMatchId());
                        jwSignRecordSport.setSignRecordId(jwSignRecord.getId());
                        jwSignRecordSport.setSportId(sportId);
                        jwSignRecordSport.setGameItemId(jwGameItem.getId());
                        jwSignRecordSport.setTeamId(teamId);
                        jwSignRecordSportService.insertJwSignRecordSport(jwSignRecordSport);

                        // 如果该项目已经分组 则新加的报名也要分组
                        addNewSignToScheduleItem(jwSignRecord, jwGameItem);

                        // 判断需不需加背号
                        if(StringUtils.isEmpty(backNum)){
                            addNewBackNum(jwSignRecord);
                        }
                    } else {
                        JwSport jwSport = jwSportService.selectJwSportById(sportId);
                        throw new GlobalException(jwSport.getPlayerName() + " 已经报名 " + jwGameItem.getCode() + " 组别");
                    }
                }
            } else if ("2".equals(jwGameItem.getSportLimit())) {

                if (StringUtils.isLongNotNull(editId)) {
                    // 修改多人报名
                    JwSignRecord jwSignRecord = jwSignRecordService.selectJwSignRecordById(editId);
                    if (jwSignRecord != null) {
                        // 删除之前的
                        jwSignRecordSportService.deleteJwSignRecordSportBySignRecordId(editId);
                        // 插入新的
                        for (Long sportId : sportIds) {
                            JwSignRecordSport jwSignRecordSport = new JwSignRecordSport();
                            jwSignRecordSport.setMatchId(jwGameItem.getMatchId());
                            jwSignRecordSport.setSignRecordId(editId);
                            jwSignRecordSport.setSportId(sportId);
                            jwSignRecordSport.setTeamId(teamId);
                            jwSignRecordSport.setGameItemId(jwGameItem.getId());
                            jwSignRecordSportService.insertJwSignRecordSport(jwSignRecordSport);
                        }
                    }
                } else {
                    // 新增多人报名
                    JwSignRecord jwSignRecord = new JwSignRecord();
                    jwSignRecord.setMatchId(jwGameItem.getMatchId());
                    jwSignRecord.setSportLimit(jwGameItem.getSportLimit());
                    jwSignRecord.setGameItemId(jwGameItem.getId());
                    jwSignRecord.setTeamId(teamId);
                    jwSignRecord.setBackNumber(backNum);
                    insertJwSignRecord(jwSignRecord);

                    for (Long sportId : sportIds) {
                        JwSignRecordSport jwSignRecordSport = new JwSignRecordSport();
                        jwSignRecordSport.setMatchId(jwGameItem.getMatchId());
                        jwSignRecordSport.setSignRecordId(jwSignRecord.getId());
                        jwSignRecordSport.setSportId(sportId);
                        jwSignRecordSport.setTeamId(teamId);
                        jwSignRecordSport.setGameItemId(jwGameItem.getId());
                        jwSignRecordSportService.insertJwSignRecordSport(jwSignRecordSport);
                    }

                    // 如果该项目已经分组 则新加的报名也要分组
                    addNewSignToScheduleItem(jwSignRecord, jwGameItem);

                    // 判断需不需加背号

                    if(StringUtils.isEmpty(backNum)){
                        addNewBackNum(jwSignRecord);
                    }
                }
            } else if ("3".equals(jwGameItem.getSportLimit())) {

                if (StringUtils.isLongNotNull(editId)) {
                    // 修改多人报名
                    JwSignRecord jwSignRecord = jwSignRecordService.selectJwSignRecordById(editId);
                    if (jwSignRecord != null) {
                        // 删除之前的
                        jwSignRecordSportService.deleteJwSignRecordSportBySignRecordId(editId);
                        // 插入新的
                        for (Long sportId : sportIds) {
                            JwSignRecordSport jwSignRecordSport = new JwSignRecordSport();
                            jwSignRecordSport.setMatchId(jwGameItem.getMatchId());
                            jwSignRecordSport.setSignRecordId(editId);
                            jwSignRecordSport.setSportId(sportId);
                            jwSignRecordSport.setTeamId(teamId);
                            jwSignRecordSport.setGameItemId(jwGameItem.getId());
                            jwSignRecordSportService.insertJwSignRecordSport(jwSignRecordSport);
                        }
                    }
                } else {
                    // 新增多人报名
                    JwSignRecord jwSignRecord = new JwSignRecord();
                    jwSignRecord.setMatchId(jwGameItem.getMatchId());
                    jwSignRecord.setSportLimit(jwGameItem.getSportLimit());
                    jwSignRecord.setGameItemId(jwGameItem.getId());
                    jwSignRecord.setTeamId(teamId);
                    jwSignRecord.setBackNumber(backNum);
                    insertJwSignRecord(jwSignRecord);

                    for (Long sportId : sportIds) {
                        JwSignRecordSport jwSignRecordSport = new JwSignRecordSport();
                        jwSignRecordSport.setMatchId(jwGameItem.getMatchId());
                        jwSignRecordSport.setSignRecordId(jwSignRecord.getId());
                        jwSignRecordSport.setSportId(sportId);
                        jwSignRecordSport.setTeamId(teamId);
                        jwSignRecordSport.setGameItemId(jwGameItem.getId());
                        jwSignRecordSportService.insertJwSignRecordSport(jwSignRecordSport);
                    }

                    // 如果该项目已经分组 则心加的报名也要分组
                    addNewSignToScheduleItem(jwSignRecord, jwGameItem);

                    // 判断需不需加背号
                    if(StringUtils.isEmpty(backNum)){
                        addNewBackNum(jwSignRecord);
                    }

                }
            }
        }
        return AjaxResult.success();
    }

    // 新增报名增加到分组里面
    private void addNewSignToScheduleItem(JwSignRecord jwSignRecord, JwGameItem jwGameItem) {
        // 如果该项目已经分组 则新加的报名也要分组
        List<JwScheduleItem> jwScheduleItemList = jwScheduleItemService.selectJwScheduleItemByGameItemId(jwGameItem.getId());
        jwScheduleItemList = jwScheduleItemList.stream().filter(jwScheduleItem -> "1".equals(jwScheduleItem.getItemProcess())).collect(Collectors.toList());
        if (jwScheduleItemList != null && jwScheduleItemList.size() > 0) {
            // 找到最少的一个组加进去
            JwScheduleItem jwScheduleItem = jwScheduleItemList.stream().min(Comparator.comparingLong(JwScheduleItem::getSportCount)).get();
            if (jwScheduleItem.getSportCount() < 0) jwScheduleItem.setSportCount(0l);
            JwSignRecord updateD = new JwSignRecord();
            updateD.setId(jwSignRecord.getId());
            updateD.setScheduleItemId(jwScheduleItem.getId());
            updateD.setIndexOrder(jwScheduleItem.getSportCount() + 1);
            jwSignRecordService.updateJwSignRecord(updateD);
        }
    }

    //  新增加报报名 判断需不需加背号
    private void addNewBackNum(JwSignRecord jwSignRecord) {
        JwSignRecord updateJ = new JwSignRecord();
        updateJ.setId(jwSignRecord.getId());
        String backNum = selectMaxBackNum(jwSignRecord.getMatchId());
        if (StringUtils.isNotEmpty(backNum) && Long.valueOf(backNum) > 0) {
            if ("1".equals(jwSignRecord.getSportLimit())) {
                // 单人的背号，先判断他有没有背号，没有就加一  有就用原来的
                List<JwSignRecordSport> jwSignRecordSportList = jwSignRecordSportService.selectJwSignRecordSportListById(jwSignRecord.getId());
                if (jwSignRecordSportList != null && jwSignRecordSportList.size() > 0) {
                    JwSignRecordSport jwSignRecordSport = jwSignRecordSportList.get(0);
                    List<JwSignRecord> hasBackNumList = jwSignRecordService.selectJwSignRecordBySportId(jwSignRecord.getMatchId(), jwSignRecordSport.getSportId());
                    if (hasBackNumList != null && hasBackNumList.size() > 0) {
                        JwSignRecord b = hasBackNumList.get(0);
                        if (b != null && StringUtils.isNotEmpty(b.getBackNumber())) {
                            updateJ.setBackNumber(b.getBackNumber());
                        } else {
                            updateJ.setBackNumber(new DecimalFormat(T).format(Long.valueOf(backNum) + 1));
                        }
                    } else {
                        updateJ.setBackNumber(new DecimalFormat(T).format(Long.valueOf(backNum) + 1));
                    }
                } else {
                    updateJ.setBackNumber(new DecimalFormat(T).format(Long.valueOf(backNum) + 1));
                }
            } else if ("3".equals(jwSignRecord.getSportLimit()) || "2".equals(jwSignRecord.getSportLimit())) {
                // 齐舞的背号 直接加一
                updateJ.setBackNumber(new DecimalFormat(T).format(Long.valueOf(backNum) + 1));
            }
            jwSignRecordService.updateJwSignRecord(updateJ);
        }
    }

    private int insertJwSignRecord(JwSignRecord jwSignRecord) {
        jwSignRecord.setCreateTime(DateUtils.getNowDate());
        return jwSignRecordMapper.insertJwSignRecord(jwSignRecord);
    }

    public int updateJwSignRecord(JwSignRecord jwSignRecord) {
        jwSignRecord.setUpdateTime(DateUtils.getNowDate());
        return jwSignRecordMapper.updateJwSignRecord(jwSignRecord);
    }

    public int clearAllBackNum(Long matchId) {
        return jwSignRecordMapper.clearAllBackNum(matchId);
    }

    public int deleteJwSignRecordByIds(Long[] ids) {
        return jwSignRecordMapper.deleteJwSignRecordByIds(ids);
    }

    public int deleteJwSignRecordById(Long id) {
        Long oldGameItemId = selectJwSignRecordById(id).getGameItemId();
        int re = jwSignRecordMapper.deleteJwSignRecordById(id);
        // 如果已经分组, 把组别的排序重新整理
        arrangeOrder(oldGameItemId);
        return re;
    }

    // 获取选手的打分明细
    public List<JwSignRecord> selectJwSignRecordHaiScore(JwSignRecord jwSignRecord) {
        return jwSignRecordMapper.selectJwSignRecordHaiScore(jwSignRecord);
    }

    // 清除一个组别的打分排名
    public int clearScoreByGameItem(Long gameItemId) {
        return jwSignRecordMapper.clearScoreByGameItem(gameItemId);
    }

    // 修改海选排名
    public int saveCustomOrder(Long id, Long rankOrder) {
        return jwSignRecordMapper.saveCustomOrder(id, rankOrder);
    }

    // 获取海选晋级选手
    public List<JwSignRecord> selectHaiXuanJinJiSportList(Long gameItemId) {
        return jwSignRecordMapper.selectHaiXuanJinJiSportList(gameItemId);
    }

    // 获取裁判打分列表
    public List<JwSignRecord> selectJwSignRecordListWithJudgeScore(Long judgeId, Long scheduleItemId) {
        return jwSignRecordMapper.selectJwSignRecordListWithJudgeScore(judgeId, scheduleItemId);
    }

}
