package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwMatchTeam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 代表队 */
    private Long teamId;

    /** 比赛 */
    @Excel(name = "比赛")
    private Long matchId;

    /** 序号 */
    @Excel(name = "序号")
    private Long indexOrder;

    private String erCode;

    public String getErCode() {
        return erCode;
    }

    public void setErCode(String erCode) {
        this.erCode = erCode;
    }

    public void setTeamId(Long teamId)
    {
        this.teamId = teamId;
    }

    public Long getTeamId() 
    {
        return teamId;
    }
    public void setMatchId(Long matchId) 
    {
        this.matchId = matchId;
    }

    public Long getMatchId() 
    {
        return matchId;
    }
    public void setIndexOrder(Long indexOrder) 
    {
        this.indexOrder = indexOrder;
    }

    public Long getIndexOrder() 
    {
        return indexOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("teamId", getTeamId())
            .append("matchId", getMatchId())
            .append("indexOrder", getIndexOrder())
            .toString();
    }
}
