package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwJudgeMatch extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private Long judgeId;

    private Long matchId;

    public void setJudgeId(Long judgeId) 
    {
        this.judgeId = judgeId;
    }

    public Long getJudgeId() 
    {
        return judgeId;
    }
    public void setMatchId(Long matchId) 
    {
        this.matchId = matchId;
    }

    public Long getMatchId() 
    {
        return matchId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("judgeId", getJudgeId())
            .append("matchId", getMatchId())
            .toString();
    }
}
