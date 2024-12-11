package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwGameItemAwardItem extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private Long gameItemId;

    private Long awardItem;

    public void setGameItemId(Long gameItemId) 
    {
        this.gameItemId = gameItemId;
    }

    public Long getGameItemId() 
    {
        return gameItemId;
    }
    public void setAwardItem(Long awardItem) 
    {
        this.awardItem = awardItem;
    }

    public Long getAwardItem() 
    {
        return awardItem;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("gameItemId", getGameItemId())
            .append("awardItem", getAwardItem())
            .toString();
    }
}
