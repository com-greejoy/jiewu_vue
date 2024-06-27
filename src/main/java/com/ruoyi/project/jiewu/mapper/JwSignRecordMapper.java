package com.ruoyi.project.jiewu.mapper;

import java.util.List;

import com.ruoyi.project.jiewu.domain.JwSignRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 报名记录Mapper接口
 *
 * @author ruoyi
 * @date 2024-06-05
 */
public interface JwSignRecordMapper {

    // 改组
    public int changeGameItem(@Param("id") Long id, @Param("changeGameItemId") Long changeGameItemId);

    public JwSignRecord selectJwSignRecordById(Long id);

    public List<JwSignRecord> selectJwSignRecordList(JwSignRecord jwSignRecord);

    public List<JwSignRecord> selectJwSignRecordListWithUserGameItem(Long teamId, Long gameItemId, String backNumber);

    public List<JwSignRecord> selectJwSignRecordListWithAllInfo(@Param("gameItemId") Long gameItemId,@Param("scheduleItemId") Long scheduleItemId);

    public List<JwSignRecord> selectJwSignRecordListByScheduleItem(@Param("scheduleItemId") Long scheduleItemId);

    public List<JwSignRecord> selectJwSignRecordListWithUserMatch(Long teamId, Long matchId);

    public List<JwSignRecord> selectJwSignRecordListByMatchId(@Param("matchId") Long matchId);

    public List<JwSignRecord> selectJwSignRecordBySportId(@Param("matchId") Long matchId, @Param("sportId") Long sportId);

    // 获取代表队全部赛程
    public List<JwSignRecord> getTeamScheduleInfoList(@Param("matchId") Long matchId, @Param("teamId") Long teamId);

    public String selectMaxBackNum(@Param("matchId") Long matchId);

    //获取齐舞的报名记录
    public List<JwSignRecord> listMatchQiWuSignRecord(Long teamId,Long matchId);

    //获取代表队比赛报名人数
    public Long selectJwSignRecordSportNum(Long teamId, Long matchId);

    //获取代表队比赛报名人次
    public Long selectJwSignRecordSportTime(Long teamId, Long matchId);

    public int insertJwSignRecord(JwSignRecord jwSignRecord);

    public int updateJwSignRecord(JwSignRecord jwSignRecord);

    public int clearAllBackNum(@Param("matchId") Long matchId);

    public int deleteJwSignRecordById(Long id);

    public int deleteJwSignRecordByIds(Long[] ids);
}
