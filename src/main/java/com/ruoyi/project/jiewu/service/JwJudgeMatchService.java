package com.ruoyi.project.jiewu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwJudgeMatchMapper;
import com.ruoyi.project.jiewu.domain.JwJudgeMatch;

@Service
public class JwJudgeMatchService {

    @Autowired
    private JwJudgeMatchMapper jwJudgeMatchMapper;

    public JwJudgeMatch selectJwJudgeMatchByJudgeId(Long matchId, Long judgeId) {
        return jwJudgeMatchMapper.selectJwJudgeMatchByJudgeId(matchId, judgeId);
    }

    public List<JwJudgeMatch> selectJwJudgeMatchList(JwJudgeMatch jwJudgeMatch) {
        return jwJudgeMatchMapper.selectJwJudgeMatchList(jwJudgeMatch);
    }

    public int insertJwJudgeMatch(JwJudgeMatch jwJudgeMatch) {
        return jwJudgeMatchMapper.insertJwJudgeMatch(jwJudgeMatch);
    }

    public int updateJwJudgeMatch(JwJudgeMatch jwJudgeMatch) {
        return jwJudgeMatchMapper.updateJwJudgeMatch(jwJudgeMatch);
    }

    public int deleteJwJudgeMatchByJudgeIds(Long[] judgeIds) {
        return jwJudgeMatchMapper.deleteJwJudgeMatchByJudgeIds(judgeIds);
    }

    public int deleteJwJudgeMatch(Long matchId, Long judgeId) {
        return jwJudgeMatchMapper.deleteJwJudgeMatch(matchId, judgeId);
    }

    public int deleteJwJudgeMatchByJudgeId(Long judgeId) {
        return jwJudgeMatchMapper.deleteJwJudgeMatchByJudgeId(judgeId);
    }
}
