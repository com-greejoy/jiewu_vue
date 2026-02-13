package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwHaiScore extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "比赛ID")
    private Long matchId;

    @Excel(name = "组别")
    private Long gameItemId;

    @Excel(name = "赛程小项")
    private Long scheduleItemId;

    private Long sportId;

    private Long judgeId;

    private String score;

    private String scoreType;

    private String judgeName;
    private String backNumber;
    private String judgeImg;


    public JwHaiScore(Long matchId, Long gameItemId, Long scheduleItemId, Long sportId, Long judgeId, String score, String scoreType) {
        this.matchId = matchId;
        this.gameItemId = gameItemId;
        this.scheduleItemId = scheduleItemId;
        this.sportId = sportId;
        this.judgeId = judgeId;
        this.score = score;
        this.scoreType = scoreType;
    }

    public JwHaiScore() {
    }

    public String getJudgeImg() {
        return judgeImg;
    }

    public void setJudgeImg(String judgeImg) {
        this.judgeImg = judgeImg;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

    public String getBackNumber() {
        return backNumber;
    }

    public void setBackNumber(String backNumber) {
        this.backNumber = backNumber;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMatchId(Long matchId) 
    {
        this.matchId = matchId;
    }

    public Long getMatchId() 
    {
        return matchId;
    }
    public void setGameItemId(Long gameItemId) 
    {
        this.gameItemId = gameItemId;
    }

    public Long getGameItemId() 
    {
        return gameItemId;
    }
    public void setScheduleItemId(Long scheduleItemId) 
    {
        this.scheduleItemId = scheduleItemId;
    }

    public Long getScheduleItemId() 
    {
        return scheduleItemId;
    }
    public void setSportId(Long sportId) 
    {
        this.sportId = sportId;
    }

    public Long getSportId() 
    {
        return sportId;
    }
    public void setJudgeId(Long judgeId) 
    {
        this.judgeId = judgeId;
    }

    public Long getJudgeId() 
    {
        return judgeId;
    }
    public void setScore(String score) 
    {
        this.score = score;
    }

    public String getScore() 
    {
        return score;
    }
    public void setScoreType(String scoreType) 
    {
        this.scoreType = scoreType;
    }

    public String getScoreType() 
    {
        return scoreType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("matchId", getMatchId())
            .append("gameItemId", getGameItemId())
            .append("scheduleItemId", getScheduleItemId())
            .append("sportId", getSportId())
            .append("judgeId", getJudgeId())
            .append("score", getScore())
            .append("scoreType", getScoreType())
            .append("createTime", getCreateTime())
            .toString();
    }
}