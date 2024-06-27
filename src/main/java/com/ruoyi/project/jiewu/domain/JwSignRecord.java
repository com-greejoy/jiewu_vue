package com.ruoyi.project.jiewu.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class JwSignRecord extends BaseEntity
{

    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "代表队")
    private Long teamId;

    /** 背号 */
    @Excel(name = "背号")
    private String backNumber;

    /** 比赛ID */
    @Excel(name = "比赛ID")
    private Long matchId;

    /** 组别 */
    @Excel(name = "组别")
    private Long gameItemId;

    /** 人数限制 */
    @Excel(name = "人数限制")
    private String sportLimit;

    /** 作品名称 */
    @Excel(name = "作品名称")
    private String worksName;

    /** 作品音乐 */
    @Excel(name = "作品音乐")
    private String worksMusic;

    private String worksMusicName;

    /** 作品视频 */
    @Excel(name = "作品视频")
    private String worksVideo;

    private Long indexOrder;

    private JwGameItem jwGameItem;

    private JwScheduleItem jwScheduleItem;

    private JwTeam jwTeam;

    private BigDecimal fee;

    private BigDecimal avgFee;

    private Long[] sportIds;

    private Long scheduleItemId;

    private String scheduleName;
    private String itemName;
    private Long placeOrder;
    private String area;
    @JsonFormat(pattern = "MM月dd日 HH:mm")
    private Date placeTime;

    public Date getPlaceTime() {
        return placeTime;
    }

    public void setPlaceTime(Date placeTime) {
        this.placeTime = placeTime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(Long placeOrder) {
        this.placeOrder = placeOrder;
    }

    public BigDecimal getAvgFee() {
        return avgFee;
    }

    public void setAvgFee(BigDecimal avgFee) {
        this.avgFee = avgFee;
    }

    public JwTeam getJwTeam() {
        return jwTeam;
    }

    public void setJwTeam(JwTeam jwTeam) {
        this.jwTeam = jwTeam;
    }

    public JwScheduleItem getJwScheduleItem() {
        return jwScheduleItem;
    }

    public void setJwScheduleItem(JwScheduleItem jwScheduleItem) {
        this.jwScheduleItem = jwScheduleItem;
    }

    public Long getIndexOrder() {
        return indexOrder;
    }

    public void setIndexOrder(Long indexOrder) {
        this.indexOrder = indexOrder;
    }

    public Long getScheduleItemId() {
        return scheduleItemId;
    }

    public void setScheduleItemId(Long scheduleItemId) {
        this.scheduleItemId = scheduleItemId;
    }

    public Long[] getSportIds() {
        return sportIds;
    }

    public void setSportIds(Long[] sportIds) {
        this.sportIds = sportIds;
    }

    public String getWorksMusicName() {
        return worksMusicName;
    }

    public void setWorksMusicName(String worksMusicName) {
        this.worksMusicName = worksMusicName;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public JwGameItem getJwGameItem() {
        return jwGameItem;
    }

    public void setJwGameItem(JwGameItem jwGameItem) {
        this.jwGameItem = jwGameItem;
    }

    private List<JwSignRecordSport> jwSignRecordSportList;

    public List<JwSignRecordSport> getJwSignRecordSportList() {
        return jwSignRecordSportList;
    }

    public void setJwSignRecordSportList(List<JwSignRecordSport> jwSignRecordSportList) {
        this.jwSignRecordSportList = jwSignRecordSportList;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setBackNumber(String backNumber) 
    {
        this.backNumber = backNumber;
    }

    public String getBackNumber() 
    {
        return backNumber;
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
    public void setSportLimit(String sportLimit) 
    {
        this.sportLimit = sportLimit;
    }

    public String getSportLimit() 
    {
        return sportLimit;
    }
    public void setWorksName(String worksName) 
    {
        this.worksName = worksName;
    }

    public String getWorksName() 
    {
        return worksName;
    }
    public void setWorksMusic(String worksMusic) 
    {
        this.worksMusic = worksMusic;
    }

    public String getWorksMusic() 
    {
        return worksMusic;
    }
    public void setWorksVideo(String worksVideo) 
    {
        this.worksVideo = worksVideo;
    }

    public String getWorksVideo() 
    {
        return worksVideo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("backNumber", getBackNumber())
            .append("matchId", getMatchId())
            .append("gameItemId", getGameItemId())
            .append("sportLimit", getSportLimit())
            .append("worksName", getWorksName())
            .append("worksMusic", getWorksMusic())
            .append("worksVideo", getWorksVideo())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
