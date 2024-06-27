package com.ruoyi.project.jiewu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwMatchTeamMapper;
import com.ruoyi.project.jiewu.domain.JwMatchTeam;

@Service
public class JwMatchTeamService {

    @Autowired
    private JwMatchTeamMapper jwMatchTeamMapper;

    public JwMatchTeam getJwMatchTeam(Long matchId, Long teamId) {
        return jwMatchTeamMapper.getJwMatchTeam(matchId, teamId);
    }

    public List<JwMatchTeam> selectJwMatchTeamByTeamId(Long teamId) {
        return jwMatchTeamMapper.selectJwMatchTeamByTeamId(teamId);
    }

    public List<JwMatchTeam> selectJwMatchTeamByMatchId(Long matchId) {
        return jwMatchTeamMapper.selectJwMatchTeamByMatchId(matchId);
    }

    public List<JwMatchTeam> selectJwMatchTeamList(JwMatchTeam jwMatchTeam) {
        return jwMatchTeamMapper.selectJwMatchTeamList(jwMatchTeam);
    }

    public int insertJwMatchTeam(JwMatchTeam jwMatchTeam) {
        return jwMatchTeamMapper.insertJwMatchTeam(jwMatchTeam);
    }

    public Long getLastOrder(Long matchId){
        return jwMatchTeamMapper.getLastOrder(matchId);
    }

    public int updateJwMatchTeam(JwMatchTeam jwMatchTeam) {
        return jwMatchTeamMapper.updateJwMatchTeam(jwMatchTeam);
    }

    public int deleteJwMatchTeamByTeamIds(Long[] teamIds) {
        return jwMatchTeamMapper.deleteJwMatchTeamByTeamIds(teamIds);
    }

    public int deleteJwMatchTeamByTeamId(Long teamId) {
        return jwMatchTeamMapper.deleteJwMatchTeamByTeamId(teamId);
    }
}
