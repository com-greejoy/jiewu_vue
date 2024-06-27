package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 微信用户对象 jw_wx_user
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
public class JwWxUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 手机号 */
    @Excel(name = "手机号")
    private String mobile;

    /** 性别 */
    @Excel(name = "性别")
    private String sex;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 微信OpenID */
    @Excel(name = "微信OpenID")
    private String openId;

    /** 邀请人 */
    @Excel(name = "邀请人")
    private Long parentUserId;

    /** 邀请的队伍 */
    @Excel(name = "邀请的队伍")
    private Long parentTeamId;

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public Long getParentTeamId() {
        return parentTeamId;
    }

    public void setParentTeamId(Long parentTeamId) {
        this.parentTeamId = parentTeamId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setMobile(String mobile) 
    {
        this.mobile = mobile;
    }

    public String getMobile() 
    {
        return mobile;
    }
    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }
    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }
    public void setOpenId(String openId) 
    {
        this.openId = openId;
    }

    public String getOpenId() 
    {
        return openId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("mobile", getMobile())
            .append("sex", getSex())
            .append("avatar", getAvatar())
            .append("openId", getOpenId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
