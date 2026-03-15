package com.ruoyi.project.jiewu.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwScheduleItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 比赛 */
    @Excel(name = "比赛")
    private Long matchId;

    /** 项目 */
    @Excel(name = "项目")
    private Long gameItemId;

    /** 阶段 */
    @Excel(name = "阶段")
    private Long scheduleInfoId;

    /** 场次 */
    @Excel(name = "场次")
    private Long schedulePlaceId;

    /** 小项 */
    @Excel(name = "小项")
    private String itemName;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date shceduleTime;

    /** 场地 */
    @Excel(name = "场地")
    private String area;

    @Excel(name = "进程")
    private String itemProcess;

    private String lockScore;

    private Long sportCount;

    private Long scheduleIndex;

    public Long getScheduleIndex() {
        return scheduleIndex;
    }

    public void setScheduleIndex(Long scheduleIndex) {
        this.scheduleIndex = scheduleIndex;
    }

    public String getLockScore() {
        return lockScore;
    }

    public void setLockScore(String lockScore) {
        this.lockScore = lockScore;
    }

    public String getItemProcess() {
        return itemProcess;
    }

    public void setItemProcess(String itemProcess) {
        this.itemProcess = itemProcess;
    }

    public Long getSportCount() {
        return sportCount;
    }

    public void setSportCount(Long sportCount) {
        this.sportCount = sportCount;
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
    public void setScheduleInfoId(Long scheduleInfoId) 
    {
        this.scheduleInfoId = scheduleInfoId;
    }

    public Long getScheduleInfoId() 
    {
        return scheduleInfoId;
    }
    public void setSchedulePlaceId(Long schedulePlaceId) 
    {
        this.schedulePlaceId = schedulePlaceId;
    }

    public Long getSchedulePlaceId() 
    {
        return schedulePlaceId;
    }
    public void setItemName(String itemName) 
    {
        this.itemName = itemName;
    }

    public String getItemName() 
    {
        return itemName;
    }
    public void setShceduleTime(Date shceduleTime) 
    {
        this.shceduleTime = shceduleTime;
    }

    public Date getShceduleTime() 
    {
        return shceduleTime;
    }
    public void setArea(String area) 
    {
        this.area = area;
    }

    public String getArea() 
    {
        return area;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("matchId", getMatchId())
            .append("gameItemId", getGameItemId())
            .append("scheduleInfoId", getScheduleInfoId())
            .append("schedulePlaceId", getSchedulePlaceId())
            .append("itemName", getItemName())
            .append("shceduleTime", getShceduleTime())
            .append("area", getArea())
            .toString();
    }
}
