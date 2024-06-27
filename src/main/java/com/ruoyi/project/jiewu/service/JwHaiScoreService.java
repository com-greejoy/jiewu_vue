package com.ruoyi.project.jiewu.service;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.jiewu.mapper.JwHaiScoreMapper;
import com.ruoyi.project.jiewu.domain.JwHaiScore;

@Service
public class JwHaiScoreService {

    @Autowired
    private JwHaiScoreMapper jwHaiScoreMapper;

    public JwHaiScore selectJwHaiScoreById(Long id) {
        return jwHaiScoreMapper.selectJwHaiScoreById(id);
    }

    public List<JwHaiScore> selectJwHaiScoreList(JwHaiScore jwHaiScore) {
        return jwHaiScoreMapper.selectJwHaiScoreList(jwHaiScore);
    }

    public int insertJwHaiScore(JwHaiScore jwHaiScore) {
        jwHaiScore.setCreateTime(DateUtils.getNowDate());
        return jwHaiScoreMapper.insertJwHaiScore(jwHaiScore);
    }

    public int updateJwHaiScore(JwHaiScore jwHaiScore) {
        return jwHaiScoreMapper.updateJwHaiScore(jwHaiScore);
    }

    public int deleteJwHaiScoreByIds(Long[] ids) {
        return jwHaiScoreMapper.deleteJwHaiScoreByIds(ids);
    }

    public int deleteJwHaiScoreById(Long id) {
        return jwHaiScoreMapper.deleteJwHaiScoreById(id);
    }
}
