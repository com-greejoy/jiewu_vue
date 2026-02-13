package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwTeamLeader;

public interface JwTeamLeaderMapper {

    public JwTeamLeader selectJwTeamLeaderById(Long id);

    public List<JwTeamLeader> selectJwTeamLeaderList(JwTeamLeader jwTeamLeader);

    public List<JwTeamLeader> selectJwTeamLeaderListByNamePhone(JwTeamLeader jwTeamLeader);

    public List<JwTeamLeader> selectJwTeamLeaderListByWxUser(Long createUserId, String searchName, Long matchId);

    public int insertJwTeamLeader(JwTeamLeader jwTeamLeader);

    public int updateJwTeamLeader(JwTeamLeader jwTeamLeader);

    public int deleteJwTeamLeaderById(Long id);

    public int deleteJwTeamLeaderByIds(Long[] ids);
}
