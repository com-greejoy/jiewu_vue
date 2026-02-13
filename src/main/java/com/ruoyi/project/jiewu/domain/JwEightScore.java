package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwEightScore extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long judgeId;

    private Long playerId;

    private String playerPkGroup;

    private Long playerGroup;

    private Long judgeNum;

    private Long equally;

    private String judgeName;

    private Long lun;

    private String subScore;

    private String changDi;

    private Long matchId;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getChangDi() {
        return changDi;
    }

    public void setChangDi(String changDi) {
        this.changDi = changDi;
    }

    public String getSubScore() {
        return subScore;
    }

    public void setSubScore(String subScore) {
        this.subScore = subScore;
    }

    public Long getLun() {
        return lun;
    }

    public void setLun(Long lun) {
        this.lun = lun;
    }

    public String getJudgeName() {
        return judgeName;
    }

    public void setJudgeName(String judgeName) {
        this.judgeName = judgeName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setJudgeId(Long judgeId) 
    {
        this.judgeId = judgeId;
    }

    public Long getJudgeId() 
    {
        return judgeId;
    }
    public void setPlayerId(Long playerId)
    {
        this.playerId = playerId;
    }

    public Long getPlayerId()
    {
        return playerId;
    }
    public void setPlayerPkGroup(String playerPkGroup) 
    {
        this.playerPkGroup = playerPkGroup;
    }

    public String getPlayerPkGroup() 
    {
        return playerPkGroup;
    }
    public void setPlayerGroup(Long playerGroup)
    {
        this.playerGroup = playerGroup;
    }

    public Long getPlayerGroup()
    {
        return playerGroup;
    }
    public void setJudgeNum(Long judgeNum) 
    {
        this.judgeNum = judgeNum;
    }

    public Long getJudgeNum() 
    {
        return judgeNum;
    }
    public void setEqually(Long equally) 
    {
        this.equally = equally;
    }

    public Long getEqually() 
    {
        return equally;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("judgeId", getJudgeId())
            .append("playerId", getPlayerId())
            .append("playerPkGroup", getPlayerPkGroup())
            .append("createTime", getCreateTime())
            .append("playerGroup", getPlayerGroup())
            .append("judgeNum", getJudgeNum())
            .append("equally", getEqually())
            .toString();
    }
}
