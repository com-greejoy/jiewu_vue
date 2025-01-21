package com.ruoyi.project.jiewu.domain;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwGameItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "比赛ID")
    private Long matchId;

    private String matchName;

    @Excel(name = "编号")
    private String code;

    @Excel(name = "项目名")
    private String name;

//    @Excel(name = "比赛模式")
    private String matchType;

//    @Excel(name = "项目类型")
    private String sportLimit;

    @Excel(name = "报名费")
    private BigDecimal fee;

    @Excel(name = "最多人数")
    private Long feeMaxSport;

//    @Excel(name = "分组模式")
    private String groupMode;


//    @Excel(name = "最小年龄")
    private Long minYear;

//    @Excel(name = "最大年龄")
    private Long maxYear;
    @Excel(name = "显示最小年龄")
    private Long showMinYear;
    @Excel(name = "显示最大年龄")
    private Long showMaxYear;

    @Excel(name = "备注")
    private String remark;

//    @Excel(name = "分组数")
    private Long groupLimit;

//    @Excel(name = "人数限制")
    private Integer maxSport;
    private Integer minSport;
//    @Excel(name = "选手时长(秒)")
    private Long singleDuration;



//    @Excel(name = "晋级人数")
    private Long promotionNum;



//    @Excel(name = "成绩奖项")
    private String resultDesId;

//    @Excel(name = "投屏背景图")
    private String screenImg;

//    @Excel(name = "性别限制")
    private String sexCon;

    private Long signCount;

    private String judgeId;

    public String getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(String judgeId) {
        this.judgeId = judgeId;
    }

    public Long getShowMinYear() {
        return showMinYear;
    }

    public void setShowMinYear(Long showMinYear) {
        this.showMinYear = showMinYear;
    }

    public Long getShowMaxYear() {
        return showMaxYear;
    }

    public void setShowMaxYear(Long showMaxYear) {
        this.showMaxYear = showMaxYear;
    }

    public String getGroupMode() {
        return groupMode;
    }

    public void setGroupMode(String groupMode) {
        this.groupMode = groupMode;
    }

    public Long getSignCount() {
        return signCount;
    }

    public void setSignCount(Long signCount) {
        this.signCount = signCount;
    }

    private List<Long> sportIds;

    private List<JwSignRecord> sportList;

    public Long getFeeMaxSport() {
        return feeMaxSport;
    }

    public void setFeeMaxSport(Long feeMaxSport) {
        this.feeMaxSport = feeMaxSport;
    }

    public Integer getMinSport() {
        return minSport;
    }

    public void setMinSport(Integer minSport) {
        this.minSport = minSport;
    }

    public List<JwSignRecord> getSportList() {
        return sportList;
    }

    public void setSportList(List<JwSignRecord> sportList) {
        this.sportList = sportList;
    }

    public List<Long> getSportIds() {
        return sportIds;
    }

    public void setSportIds(List<Long> sportIds) {
        this.sportIds = sportIds;
    }

    public Integer getMaxSport() {
        return maxSport;
    }

    public void setMaxSport(Integer maxSport) {
        this.maxSport = maxSport;
    }

    public String getSportLimit() {
        return sportLimit;
    }

    public void setSportLimit(String sportLimit) {
        this.sportLimit = sportLimit;
    }

    public String getSexCon() {
        return sexCon;
    }

    public void setSexCon(String sexCon) {
        this.sexCon = sexCon;
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
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setGroupLimit(Long groupLimit) 
    {
        this.groupLimit = groupLimit;
    }

    public Long getGroupLimit() 
    {
        return groupLimit;
    }
    public void setSingleDuration(Long singleDuration) 
    {
        this.singleDuration = singleDuration;
    }

    public Long getSingleDuration() 
    {
        return singleDuration;
    }
    public void setMatchType(String matchType) 
    {
        this.matchType = matchType;
    }

    public String getMatchType() 
    {
        return matchType;
    }
    public void setPromotionNum(Long promotionNum) 
    {
        this.promotionNum = promotionNum;
    }

    public Long getPromotionNum() 
    {
        return promotionNum;
    }
    public void setMinYear(Long minYear) 
    {
        this.minYear = minYear;
    }

    public Long getMinYear() 
    {
        return minYear;
    }
    public void setMaxYear(Long maxYear) 
    {
        this.maxYear = maxYear;
    }

    public Long getMaxYear() 
    {
        return maxYear;
    }
    public void setFee(BigDecimal fee) 
    {
        this.fee = fee;
    }

    public BigDecimal getFee() 
    {
        return fee;
    }
    public void setResultDesId(String resultDesId)
    {
        this.resultDesId = resultDesId;
    }

    public String getResultDesId()
    {
        return resultDesId;
    }
    public void setScreenImg(String screenImg) 
    {
        this.screenImg = screenImg;
    }

    public String getScreenImg() 
    {
        return screenImg;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("matchId", getMatchId())
            .append("code", getCode())
            .append("name", getName())
            .append("groupLimit", getGroupLimit())
            .append("singleDuration", getSingleDuration())
            .append("matchType", getMatchType())
            .append("promotionNum", getPromotionNum())
            .append("minYear", getMinYear())
            .append("maxYear", getMaxYear())
            .append("fee", getFee())
            .append("resultDesId", getResultDesId())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("screenImg", getScreenImg())
            .append("remark", getRemark())
            .toString();
    }
}
