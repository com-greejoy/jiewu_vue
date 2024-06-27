package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwSport;

public interface JwSportMapper {

    public JwSport selectJwSportById(Long id);

    public List<JwSport> selectJwSportList(JwSport jwSport);

    public List<JwSport> selectJwSportByUserId(Long createUserId, String searchName);

    public List<JwSport> selectTodayBirthSport(String today);

    public JwSport selectJwSportByIdCard(String idCard, Long id);

    public List<JwSport> getSportListWithsignRecord(Long signRecordId);

    // 获取运动员 没有报名该项目的
    public List<JwSport> getWxSportListWithGameItem(Long createUserId, Long gameItemId, Long minAge, Long maxAge, String sex);

    public List<JwSport> getWxSportListByMatchTeam(JwSport jwSport);

    // 获取所有运动员的报名项目
    public List<JwSport> selectJwSignRecordSportGameItemList(Long matchId, Long teamId);

    public int insertJwSport(JwSport jwSport);

    public int updateJwSport(JwSport jwSport);

    public int deleteJwSportById(Long id);

    public int deleteJwSportByIds(Long[] ids);
}
