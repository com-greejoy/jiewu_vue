package com.ruoyi.project.jiewu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwJudgeMapper;
import com.ruoyi.project.jiewu.domain.JwJudge;

@Service
public class JwJudgeService {

    @Autowired
    private JwJudgeMapper jwJudgeMapper;

    public JwJudge selectJwJudgeById(Long id) {
        return jwJudgeMapper.selectJwJudgeById(id);
    }

    public List<JwJudge> selectJwJudgeList(JwJudge jwJudge) {
        return jwJudgeMapper.selectJwJudgeList(jwJudge);
    }

    public int insertJwJudge(JwJudge jwJudge) {
        return jwJudgeMapper.insertJwJudge(jwJudge);
    }

    public int updateJwJudge(JwJudge jwJudge) {
        return jwJudgeMapper.updateJwJudge(jwJudge);
    }

    public int deleteJwJudgeByIds(Long[] ids) {
        return jwJudgeMapper.deleteJwJudgeByIds(ids);
    }

    public int deleteJwJudgeById(Long id) {
        return jwJudgeMapper.deleteJwJudgeById(id);
    }
}
