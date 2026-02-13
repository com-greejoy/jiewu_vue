package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import javax.validation.constraints.NotEmpty;

public class JwTeamLeader extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private Long id;


    @NotEmpty(message = "姓名不能为空")
    @Excel(name = "姓名")
    private String leaderName;

    private String idCard;

    @Excel(name = "创建者")
    private Long createUserId;

    @NotEmpty(message = "手机号不能为空")
    @Excel(name = "电话")
    private String leaderPhone;

    @Excel(name = "")
    private Long createAddId;

    @Excel(name = "人员类型")
    private String userType;

    @Excel(name = "比赛")
    private Long matchId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setLeaderName(String leaderName)
    {
        this.leaderName = leaderName;
    }

    public String getLeaderName()
    {
        return leaderName;
    }
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getIdCard()
    {
        return idCard;
    }
    public void setCreateUserId(Long createUserId)
    {
        this.createUserId = createUserId;
    }

    public Long getCreateUserId()
    {
        return createUserId;
    }
    public void setLeaderPhone(String leaderPhone)
    {
        this.leaderPhone = leaderPhone;
    }

    public String getLeaderPhone()
    {
        return leaderPhone;
    }
    public void setCreateAddId(Long createAddId)
    {
        this.createAddId = createAddId;
    }

    public Long getCreateAddId()
    {
        return createAddId;
    }
    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getUserType()
    {
        return userType;
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
                .append("id", getId())
                .append("leaderName", getLeaderName())
                .append("idCard", getIdCard())
                .append("createUserId", getCreateUserId())
                .append("leaderPhone", getLeaderPhone())
                .append("createAddId", getCreateAddId())
                .append("userType", getUserType())
                .append("matchId", getMatchId())
                .toString();
    }
}
