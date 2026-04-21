package com.ruoyi.project.jiewu.mapper;

import java.util.List;

import com.ruoyi.project.jiewu.domain.JwSignRecord;
import org.apache.ibatis.annotations.Param;


public interface JwSignRecordMapper {

    // 改组
    public int changeGameItem(@Param("id") Long id, @Param("changeGameItemId") Long changeGameItemId);

    public JwSignRecord selectJwSignRecordById(Long id);

    public List<JwSignRecord> selectJwSignRecordList(JwSignRecord jwSignRecord);

    public List<JwSignRecord> selectQiWuJwSignRecordList(JwSignRecord jwSignRecord);

    public List<JwSignRecord> selectJwSignRecordListWithUserGameItem(Long teamId, Long gameItemId, String backNumber);

    public List<JwSignRecord> selectJwSignRecordListWithAllInfo(@Param("gameItemId") Long gameItemId, @Param("scheduleItemId") Long scheduleItemId);

    public List<JwSignRecord> selectJwSignRecordListByScheduleItem(@Param("scheduleItemId") Long scheduleItemId);

    // 获取比赛项目的选手名单
    public List<JwSignRecord> selectJwSignRecordListByGameItem(@Param("gameItemId") Long gameItemId);

    public List<JwSignRecord> selectJwSignRecordListWithUserMatch(Long teamId, Long matchId);

    public List<JwSignRecord> selectJwSignRecordListByMatchId(@Param("matchId") Long matchId);

    public List<JwSignRecord> selectJwSignRecordBySportId(@Param("matchId") Long matchId, @Param("sportId") Long sportId);

    // 获取代表队全部赛程
    public List<JwSignRecord> getTeamScheduleInfoList(@Param("matchId") Long matchId, @Param("teamId") Long teamId);

    // 获取代表队所有背号
    public List<String> selectSignBackNumList(@Param("matchId") Long matchId, @Param("teamId") Long teamId);

    // 根据背号获取赛程
    public List<JwSignRecord> selectScheduleByBackNum(@Param("matchId") Long matchId, @Param("backNumber") String backNumber);

    public String selectMaxBackNum(@Param("matchId") Long matchId);

    //获取齐舞的报名记录
    public List<JwSignRecord> listMatchQiWuSignRecord(Long teamId, Long matchId);

    //获取代表队比赛报名人数
    public Long selectJwSignRecordSportNum(Long teamId, Long matchId);

    //获取代表队比赛报名人次
    public Long selectJwSignRecordSportTime(Long teamId, Long matchId);

    public int insertJwSignRecord(JwSignRecord jwSignRecord);

    public int updateJwSignRecord(JwSignRecord jwSignRecord);

    public int updateJwSignRecordUpScore(JwSignRecord jwSignRecord);

    public int saveAward(JwSignRecord jwSignRecord);

    public int clearAllBackNum(@Param("matchId") Long matchId);

    public int deleteJwSignRecordById(Long id);

    public int deleteJwSignRecordByIds(Long[] ids);

    // 获取选手的打分明细
    public List<JwSignRecord> selectJwSignRecordHaiScore(JwSignRecord jwSignRecord);

    // 清除一个组别的打分排名
    public int clearScoreByGameItem(Long gameItemId);

    // 修改排名
    public int saveCustomOrder(@Param("id") Long id, @Param("rankOrder") Long rankOrder);

    // 修改选手队伍
    public int changeTeam(@Param("ids") Long[] ids, @Param("teamId") Long teamId);

    // 获取海选晋级选手
    public List<JwSignRecord> selectHaiXuanJinJiSportList(@Param("gameItemId") Long gameItemId);

    // 获取裁判打分列表
    public List<JwSignRecord> selectJwSignRecordListWithJudgeScore(@Param("judgeId") Long judgeId, @Param("scheduleItemId") Long scheduleItemId);

}
