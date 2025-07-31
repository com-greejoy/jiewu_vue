package com.ruoyi.project.jiewu.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class JwSignRecordSportExport extends BaseEntity {

    @Excel(name = "代表队")
    private String teamName;

    @Excel(name = "姓名")
    private String playerName;

    @Excel(name = "身份证")
    private String idCard;

    @Excel(name = "电话")
    private String playerPhone;

    @Excel(name = "分数")
    private String avgScore;

    @Excel(name = "名次")
    private Long rankOrder;

    @Excel(name = "项目名")
    private String gameItemName;

    public JwSignRecordSportExport(String teamName, String playerName, String idCard, String playerPhone, String avgScore, Long rankOrder, String gameItemName) {
        this.playerName = playerName;
        this.idCard = idCard;
        this.playerPhone = playerPhone;
        this.avgScore = avgScore;
        this.rankOrder = rankOrder;
        this.gameItemName = gameItemName;
        this.teamName = teamName;
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

    public String getPlayerPhone() {
        return playerPhone;
    }

    public void setPlayerPhone(String playerPhone) {
        this.playerPhone = playerPhone;
    }

    public String getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(String avgScore) {
        this.avgScore = avgScore;
    }

    public Long getRankOrder() {
        return rankOrder;
    }

    public void setRankOrder(Long rankOrder) {
        this.rankOrder = rankOrder;
    }

    public String getGameItemName() {
        return gameItemName;
    }

    public void setGameItemName(String gameItemName) {
        this.gameItemName = gameItemName;
    }
}
