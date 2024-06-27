package com.ruoyi.project.jiewu.service;

import com.ruoyi.project.jiewu.domain.JwMatch;
import com.ruoyi.project.jiewu.mapper.JwMatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JwMatchService {

    @Autowired
    private JwMatchMapper jwMatchMapper;

    public JwMatch selectJwMatchById(Long id) {
        return jwMatchMapper.selectJwMatchById(id);
    }

    // 获取没开始的比赛和正在报名的比赛
    public List<JwMatch> selectFutureIngList( ) {
        return jwMatchMapper.selectFutureIngList();
    }

    public List<JwMatch> selectJwMatchList(JwMatch jwMatch) {
        return jwMatchMapper.selectJwMatchList(jwMatch);
    }

    public int insertJwMatch(JwMatch jwMatch) {
        return jwMatchMapper.insertJwMatch(jwMatch);
    }

    public int updateJwMatch(JwMatch jwMatch) {
        return jwMatchMapper.updateJwMatch(jwMatch);
    }

    public int deleteJwMatchByIds(Long[] ids) {
        return jwMatchMapper.deleteJwMatchByIds(ids);
    }

    public int deleteJwMatchById(Long id) {
        return jwMatchMapper.deleteJwMatchById(id);
    }
}
