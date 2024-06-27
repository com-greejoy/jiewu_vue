package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwJudge extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String judgeName;

    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String judgeType;

    @Excel(name = "头像")
    private String img;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setJudgeName(String judgeName) 
    {
        this.judgeName = judgeName;
    }

    public String getJudgeName() 
    {
        return judgeName;
    }
    public void setJudgeType(String judgeType) 
    {
        this.judgeType = judgeType;
    }

    public String getJudgeType() 
    {
        return judgeType;
    }
    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("judgeName", getJudgeName())
            .append("judgeType", getJudgeType())
            .append("img", getImg())
            .toString();
    }
}
