package com.ruoyi.project.jiewu.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwMatchTeamMapper;
import com.ruoyi.project.jiewu.domain.JwMatchTeam;
import org.springframework.transaction.annotation.Transactional;

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

    public Long getLastOrder(Long matchId) {
        return jwMatchTeamMapper.getLastOrder(matchId);
    }

    public int updateJwMatchTeam(JwMatchTeam jwMatchTeam) {
        return jwMatchTeamMapper.updateJwMatchTeam(jwMatchTeam);
    }

    public int deleteJwMatchTeamByTeamIds(Long[] teamIds) {
        return jwMatchTeamMapper.deleteJwMatchTeamByTeamIds(teamIds);
    }

    public int deleteJwMatchTeamByTeamMatch(Long indexOrder, Long matchId) {
        return jwMatchTeamMapper.deleteJwMatchTeamByTeamMatch(indexOrder, matchId);
    }

    @Transactional
    public int reOrderMatchTeam(Long matchId) {
        List<JwMatchTeam> jwMatchTeamList = selectJwMatchTeamByMatchId(matchId);
        if (jwMatchTeamList != null && jwMatchTeamList.size() > 0) {
            jwMatchTeamList.sort(Comparator.comparingLong(JwMatchTeam::getIndexOrder));
            for (int i = 0; i < jwMatchTeamList.size(); i++) {
                JwMatchTeam up = jwMatchTeamList.get(i);
                up.setIndexOrder((long) (i + 1));
                updateJwMatchTeam(up);
            }
        }
        return 1;
    }


    public int deleteJwMatchTeamByTeamId(Long teamId) {
        return jwMatchTeamMapper.deleteJwMatchTeamByTeamId(teamId);
    }
}
