package com.ruoyi.project.jiewu.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwAwardsItem extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "描述")
    private String awardName;

    @Excel(name = "开始名次")
    private Long rankStart;

    @Excel(name = "结束名次")
    private Long rankEnd;

    @Excel(name = "百分比开始")
    private BigDecimal proportionStart;

    @Excel(name = "百分比结束")
    private BigDecimal proportionEnd;

    @Excel(name = "奖项")
    private String rankText;

    private Long jiangBei;

    private Long jiangPai;

    private Long zhengShu;

    private Long countNum;

    public Long getCountNum() {
        return countNum;
    }

    public void setCountNum(Long countNum) {
        this.countNum = countNum;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setAwardName(String awardName) 
    {
        this.awardName = awardName;
    }

    public String getAwardName() 
    {
        return awardName;
    }
    public void setRankStart(Long rankStart) 
    {
        this.rankStart = rankStart;
    }

    public Long getRankStart() 
    {
        return rankStart;
    }
    public void setRankEnd(Long rankEnd) 
    {
        this.rankEnd = rankEnd;
    }

    public Long getRankEnd() 
    {
        return rankEnd;
    }
    public void setProportionStart(BigDecimal proportionStart) 
    {
        this.proportionStart = proportionStart;
    }

    public BigDecimal getProportionStart() 
    {
        return proportionStart;
    }
    public void setProportionEnd(BigDecimal proportionEnd) 
    {
        this.proportionEnd = proportionEnd;
    }

    public BigDecimal getProportionEnd() 
    {
        return proportionEnd;
    }
    public void setRankText(String rankText) 
    {
        this.rankText = rankText;
    }

    public String getRankText() 
    {
        return rankText;
    }
    public void setJiangBei(Long jiangBei) 
    {
        this.jiangBei = jiangBei;
    }

    public Long getJiangBei() 
    {
        return jiangBei;
    }
    public void setJiangPai(Long jiangPai) 
    {
        this.jiangPai = jiangPai;
    }

    public Long getJiangPai() 
    {
        return jiangPai;
    }
    public void setZhengShu(Long zhengShu) 
    {
        this.zhengShu = zhengShu;
    }

    public Long getZhengShu() 
    {
        return zhengShu;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("awardName", getAwardName())
            .append("rankStart", getRankStart())
            .append("rankEnd", getRankEnd())
            .append("proportionStart", getProportionStart())
            .append("proportionEnd", getProportionEnd())
            .append("rankText", getRankText())
            .append("jiangBei", getJiangBei())
            .append("jiangPai", getJiangPai())
            .append("zhengShu", getZhengShu())
            .toString();
    }
}
