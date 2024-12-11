package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwHaiScore;

public interface JwHaiScoreMapper {

    public JwHaiScore selectJwHaiScoreById(Long id);

    public JwHaiScore selectJwHaiScoreByJudgeAndSport(Long judgeId, Long sportId);

    public List<JwHaiScore> selectJwHaiScoreList(JwHaiScore jwHaiScore);

    public List<JwHaiScore> selectJwHaiScoreListBySport(Long sportId);

    public List<JwHaiScore> selectJwHaiScoreListByGameItem(Long gameItemId);

    public List<JwHaiScore> selectJwHaiScoreListByScheduleItem(Long scheduleItemId);

    public int insertJwHaiScore(JwHaiScore jwHaiScore);

    public int updateJwHaiScore(JwHaiScore jwHaiScore);

    public int deleteJwHaiScoreById(Long id);

    public int deleteJwHaiScoreByIds(Long[] ids);
}
