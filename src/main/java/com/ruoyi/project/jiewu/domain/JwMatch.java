package com.ruoyi.project.jiewu.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class JwMatch extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "比赛名称")
    private String matchName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报名开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date signBeginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报名结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date signEndTime;

    @Excel(name = "地址")
    private String addr;

    @Excel(name = "海报")
    private String posterImg;

    @Excel(name = "状态")
    private String state;

    @Excel(name = "赛事详情")
    private String matchDetails;

    @Excel(name = "竞赛规程")
    private String matchRegulations;

    @Excel(name = "盖章单位")
    private String sealUnit;

    @Excel(name = "邀请码")
    private String invitationCode;

    @Excel(name = "邀请码列表")
    private String invitationList;

    private String isShow;

    private String isShowGrade;

    private String mainImg;

    private String battleImg;

    private Long startBackNum;

    @Excel(name = "管理密码")
    private String manageCode;

    private String matchConfig;

    private Long lastBackNum;

    public Long getLastBackNum() {
        return lastBackNum;
    }

    public void setLastBackNum(Long lastBackNum) {
        this.lastBackNum = lastBackNum;
    }

    public String getMatchConfig() {
        return matchConfig;
    }

    public void setMatchConfig(String matchConfig) {
        this.matchConfig = matchConfig;
    }

    public String getManageCode() {
        return manageCode;
    }

    public void setManageCode(String manageCode) {
        this.manageCode = manageCode;
    }

    public String getIsShowGrade() {
        return isShowGrade;
    }

    public void setIsShowGrade(String isShowGrade) {
        this.isShowGrade = isShowGrade;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getInvitationList() {
        return invitationList;
    }

    public void setInvitationList(String invitationList) {
        this.invitationList = invitationList;
    }

    public Long getStartBackNum() {
        return startBackNum;
    }

    public void setStartBackNum(Long startBackNum) {
        this.startBackNum = startBackNum;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getBattleImg() {
        return battleImg;
    }

    public void setBattleImg(String battleImg) {
        this.battleImg = battleImg;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getSealUnit() {
        return sealUnit;
    }

    public void setSealUnit(String sealUnit) {
        this.sealUnit = sealUnit;
    }

    public String getMatchDetails() {
        return matchDetails;
    }

    public void setMatchDetails(String matchDetails) {
        this.matchDetails = matchDetails;
    }

    public String getMatchRegulations() {
        return matchRegulations;
    }

    public void setMatchRegulations(String matchRegulations) {
        this.matchRegulations = matchRegulations;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMatchName(String matchName) 
    {
        this.matchName = matchName;
    }

    public String getMatchName() 
    {
        return matchName;
    }
    public void setBeginTime(Date beginTime) 
    {
        this.beginTime = beginTime;
    }

    public Date getBeginTime() 
    {
        return beginTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setSignBeginTime(Date signBeginTime) 
    {
        this.signBeginTime = signBeginTime;
    }

    public Date getSignBeginTime() 
    {
        return signBeginTime;
    }
    public void setSignEndTime(Date signEndTime) 
    {
        this.signEndTime = signEndTime;
    }

    public Date getSignEndTime() 
    {
        return signEndTime;
    }
    public void setAddr(String addr) 
    {
        this.addr = addr;
    }

    public String getAddr() 
    {
        return addr;
    }
    public void setPosterImg(String posterImg) 
    {
        this.posterImg = posterImg;
    }

    public String getPosterImg() 
    {
        return posterImg;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("matchName", getMatchName())
            .append("beginTime", getBeginTime())
            .append("endTime", getEndTime())
            .append("signBeginTime", getSignBeginTime())
            .append("signEndTime", getSignEndTime())
            .append("addr", getAddr())
            .append("posterImg", getPosterImg())
            .append("state", getState())
            .toString();
    }
}
