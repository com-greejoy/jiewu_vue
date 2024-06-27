package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import javax.validation.constraints.NotEmpty;

/**
 * 代表队对象 jw_team
 * 
 * @author ruoyi
 * @date 2024-05-29
 */
public class JwTeam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 代表队名字 */
    @NotEmpty(message = "代表队名字不能为空")
    @Excel(name = "代表队名字")
    private String teamName;

    /** 联系人 */
    @NotEmpty(message = "联系人不能为空")
    @Excel(name = "联系人")
    private String userName;

    /** 联系电话 */
    @NotEmpty(message = "联系电话不能为空")
    @Excel(name = "联系电话")
    private String userPhone;

    /** 邮寄地址 */
    @Excel(name = "邮寄地址")
    private String addr;

    /** 创建者 */
    @Excel(name = "创建者")
    private Long createUserId;

    /** 比赛ID */
    @Excel(name = "比赛ID")
    private Long matchId;

    private Long indexOrder;

    public Long getIndexOrder() {
        return indexOrder;
    }

    public void setIndexOrder(Long indexOrder) {
        this.indexOrder = indexOrder;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTeamName(String teamName) 
    {
        this.teamName = teamName;
    }

    public String getTeamName() 
    {
        return teamName;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setUserPhone(String userPhone) 
    {
        this.userPhone = userPhone;
    }

    public String getUserPhone() 
    {
        return userPhone;
    }
    public void setAddr(String addr) 
    {
        this.addr = addr;
    }

    public String getAddr() 
    {
        return addr;
    }
    public void setCreateUserId(Long createUserId) 
    {
        this.createUserId = createUserId;
    }

    public Long getCreateUserId() 
    {
        return createUserId;
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
            .append("teamName", getTeamName())
            .append("userName", getUserName())
            .append("userPhone", getUserPhone())
            .append("addr", getAddr())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("createUserId", getCreateUserId())
            .append("matchId", getMatchId())
            .toString();
    }
}
