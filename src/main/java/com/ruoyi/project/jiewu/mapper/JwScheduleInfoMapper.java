package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwScheduleInfo;

public interface JwScheduleInfoMapper {

    public JwScheduleInfo selectJwScheduleInfoById(Long id);

    public List<JwScheduleInfo> selectJwScheduleInfoList(JwScheduleInfo jwScheduleInfo);


    public List<JwScheduleInfo> selectJwScheduleInfoByMatchId(Long matchId);

    public int insertJwScheduleInfo(JwScheduleInfo jwScheduleInfo);

    public int updateJwScheduleInfo(JwScheduleInfo jwScheduleInfo);

    public int deleteJwScheduleInfoById(Long id);

    public int deleteJwScheduleInfoByIds(Long[] ids);
}
