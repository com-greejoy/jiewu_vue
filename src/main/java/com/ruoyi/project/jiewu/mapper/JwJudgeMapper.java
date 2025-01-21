package com.ruoyi.project.jiewu.mapper;

import java.util.List;
import com.ruoyi.project.jiewu.domain.JwJudge;

public interface JwJudgeMapper {

    public JwJudge selectJwJudgeById(Long id);

    public List<JwJudge> selectJwJudgeList(JwJudge jwJudge);

    public List<JwJudge> selectJwJudgeByIds(String[] ids);

    public int insertJwJudge(JwJudge jwJudge);

    public int updateJwJudge(JwJudge jwJudge);

    public int deleteJwJudgeById(Long id);

    public int deleteJwJudgeByIds(Long[] ids);
}
