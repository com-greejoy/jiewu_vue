package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwJudgeMatch;
import org.apache.ibatis.annotations.Param;

public interface JwJudgeMatchMapper {

    public JwJudgeMatch selectJwJudgeMatchByJudgeId(@Param("matchId") Long matchId, @Param("judgeId") Long judgeId);
    public int deleteJwJudgeMatch(@Param("matchId") Long matchId, @Param("judgeId") Long judgeId);
    public List<JwJudgeMatch> selectJwJudgeMatchList(JwJudgeMatch jwJudgeMatch);

    public int insertJwJudgeMatch(JwJudgeMatch jwJudgeMatch);

    public int updateJwJudgeMatch(JwJudgeMatch jwJudgeMatch);

    public int deleteJwJudgeMatchByJudgeId(Long judgeId);

    public int deleteJwJudgeMatchByJudgeIds(Long[] judgeIds);
}