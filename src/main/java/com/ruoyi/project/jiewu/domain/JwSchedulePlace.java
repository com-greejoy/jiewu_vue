package com.ruoyi.project.jiewu.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwSchedulePlace extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "比赛")
    private Long matchId;

    @Excel(name = "阶段")
    private Long scheduleInfoId;

    @Excel(name = "场次")
    private Long placeOrder;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date placeTime;

    private List<JwScheduleItem> jwScheduleItemList;

    public List<JwScheduleItem> getJwScheduleItemList() {
        return jwScheduleItemList;
    }

    public void setJwScheduleItemList(List<JwScheduleItem> jwScheduleItemList) {
        this.jwScheduleItemList = jwScheduleItemList;
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
    public void setScheduleInfoId(Long scheduleInfoId) 
    {
        this.scheduleInfoId = scheduleInfoId;
    }

    public Long getScheduleInfoId() 
    {
        return scheduleInfoId;
    }
    public void setPlaceOrder(Long placeOrder) 
    {
        this.placeOrder = placeOrder;
    }

    public Long getPlaceOrder() 
    {
        return placeOrder;
    }
    public void setPlaceTime(Date placeTime) 
    {
        this.placeTime = placeTime;
    }

    public Date getPlaceTime() 
    {
        return placeTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("matchId", getMatchId())
            .append("scheduleInfoId", getScheduleInfoId())
            .append("placeOrder", getPlaceOrder())
            .append("placeTime", getPlaceTime())
            .toString();
    }
}
