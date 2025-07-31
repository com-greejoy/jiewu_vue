package com.ruoyi.project.jiewu.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.List;

public class JwGameItemCount extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "编号")
    private String code;

    @Excel(name = "项目名")
    private String name;

    @Excel(name = "报名数")
    private Long signCount;


    @Excel(name = "报名人数")
    private int signSportCount;

    public JwGameItemCount(String code, String name, Long signCount, int signSportCount) {
        this.code = code;
        this.name = name;
        this.signCount = signCount;
        this.signSportCount = signSportCount;
    }

    public int getSignSportCount() {
        return signSportCount;
    }

    public void setSignSportCount(int signSportCount) {
        this.signSportCount = signSportCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSignCount() {
        return signCount;
    }

    public void setSignCount(Long signCount) {
        this.signCount = signCount;
    }
}
