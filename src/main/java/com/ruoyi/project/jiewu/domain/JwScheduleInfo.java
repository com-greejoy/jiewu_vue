package com.ruoyi.project.jiewu.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwScheduleInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "比赛")
    private Long matchId;

    @Excel(name = "阶段")
    private String scheduleName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @Excel(name = "排序")
    private Long indexOrder;

    private String matchName;

    private List<JwSchedulePlace> jwSchedulePlaceList;

    public List<JwSchedulePlace> getJwSchedulePlaceList() {
        return jwSchedulePlaceList;
    }

    public void setJwSchedulePlaceList(List<JwSchedulePlace> jwSchedulePlaceList) {
        this.jwSchedulePlaceList = jwSchedulePlaceList;
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
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
    public void setScheduleName(String scheduleName) 
    {
        this.scheduleName = scheduleName;
    }

    public String getScheduleName() 
    {
        return scheduleName;
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
            .append("id", getId())
            .append("matchId", getMatchId())
            .append("scheduleName", getScheduleName())
            .append("beginTime", getBeginTime())
            .append("endTime", getEndTime())
            .append("indexOrder", getIndexOrder())
            .toString();
    }
}
