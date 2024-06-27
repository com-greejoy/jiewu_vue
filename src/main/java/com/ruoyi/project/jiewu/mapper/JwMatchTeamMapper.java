package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwMatchTeam;

public interface JwMatchTeamMapper {

    public JwMatchTeam getJwMatchTeam(Long matchId, Long teamId);

    public List<JwMatchTeam> selectJwMatchTeamByTeamId(Long teamId);

    public List<JwMatchTeam>  selectJwMatchTeamByMatchId(Long matchId);

    public List<JwMatchTeam> selectJwMatchTeamList(JwMatchTeam jwMatchTeam);

    public int insertJwMatchTeam(JwMatchTeam jwMatchTeam);

    public Long getLastOrder(Long matchId);

    public int updateJwMatchTeam(JwMatchTeam jwMatchTeam);

    public int deleteJwMatchTeamByTeamId(Long teamId);

    public int deleteJwMatchTeamByTeamIds(Long[] teamIds);
}
