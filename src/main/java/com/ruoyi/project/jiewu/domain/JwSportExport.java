package com.ruoyi.project.jiewu.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class JwSportExport  {

    @Excel(name = "代表队")
    private String teamName;

    @Excel(name = "姓名")
    private String playerName;

    @Excel(name = "身份证", cellType = Excel.ColumnType.TEXT)
    private String idCard;

    public JwSportExport() {
    }

    public JwSportExport(String teamName, String playerName, String idCard) {
        this.teamName = teamName;
        this.playerName = playerName;
        this.idCard = idCard;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
