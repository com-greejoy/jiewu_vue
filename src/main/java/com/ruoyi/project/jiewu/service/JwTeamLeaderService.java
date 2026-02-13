package com.ruoyi.project.jiewu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwTeamLeaderMapper;
import com.ruoyi.project.jiewu.domain.JwTeamLeader;

@Service
public class JwTeamLeaderService {

    @Autowired
    private JwTeamLeaderMapper jwTeamLeaderMapper;

    public JwTeamLeader selectJwTeamLeaderById(Long id) {
        return jwTeamLeaderMapper.selectJwTeamLeaderById(id);
    }

    public List<JwTeamLeader> selectJwTeamLeaderList(JwTeamLeader jwTeamLeader) {
        return jwTeamLeaderMapper.selectJwTeamLeaderList(jwTeamLeader);
    }
    public List<JwTeamLeader> selectJwTeamLeaderListByWxUser(Long createUserId, String searchName, Long matchId) {
        return jwTeamLeaderMapper.selectJwTeamLeaderListByWxUser(createUserId,  searchName,  matchId);
    }

    public List<JwTeamLeader> selectJwTeamLeaderListByNamePhone(JwTeamLeader jwTeamLeader) {
        return jwTeamLeaderMapper.selectJwTeamLeaderListByNamePhone(jwTeamLeader);
    }

    public int insertJwTeamLeader(JwTeamLeader jwTeamLeader) {
        return jwTeamLeaderMapper.insertJwTeamLeader(jwTeamLeader);
    }

    public int updateJwTeamLeader(JwTeamLeader jwTeamLeader) {
        return jwTeamLeaderMapper.updateJwTeamLeader(jwTeamLeader);
    }

    public int deleteJwTeamLeaderByIds(Long[] ids) {
        return jwTeamLeaderMapper.deleteJwTeamLeaderByIds(ids);
    }

    public int deleteJwTeamLeaderById(Long id) {
        return jwTeamLeaderMapper.deleteJwTeamLeaderById(id);
    }
}
