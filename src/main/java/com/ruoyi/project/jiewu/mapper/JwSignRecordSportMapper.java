package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwSignRecordSport;
import com.ruoyi.project.jiewu.domain.JwSport;

public interface JwSignRecordSportMapper {

    public JwSignRecordSport selectJwSignRecordSportById(Long id);

    public List<JwSignRecordSport> selectJwSignRecordSportList(JwSignRecordSport jwSignRecordSport);

    // 获取报名记录的人员名单
    public List<JwSignRecordSport> selectJwSignRecordSportListById(Long signRecordId);

    public List<JwSignRecordSport> selectJwSignRecordSportBySportId(Long sportId);

    public int insertJwSignRecordSport(JwSignRecordSport jwSignRecordSport);

    public int updateJwSignRecordSport(JwSignRecordSport jwSignRecordSport);

    public int deleteJwSignRecordSportById(Long id);

    public int deleteJwSignRecordSportByIds(Long[] ids);

    public int deleteJwSignRecordSportBySignRecordId(Long signRecordId);
}
