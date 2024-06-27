package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwHaiScore;

public interface JwHaiScoreMapper {

    public JwHaiScore selectJwHaiScoreById(Long id);

    public List<JwHaiScore> selectJwHaiScoreList(JwHaiScore jwHaiScore);

    public int insertJwHaiScore(JwHaiScore jwHaiScore);

    public int updateJwHaiScore(JwHaiScore jwHaiScore);

    public int deleteJwHaiScoreById(Long id);

    public int deleteJwHaiScoreByIds(Long[] ids);
}
