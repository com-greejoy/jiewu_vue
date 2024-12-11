package com.ruoyi.project.jiewu.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;

public class JwSignRecordExport {

    @Excel(name = "组别")
    private String gameItemName;

    @Excel(name = "代表队")
    private String teamName;

    @Excel(name = "背号")
    private String backNumber;

    @Excel(name = "选手")
    private String playerName;

    public String getGameItemName() {
        return gameItemName;
    }

    public void setGameItemName(String gameItemName) {
        this.gameItemName = gameItemName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getBackNumber() {
        return backNumber;
    }

    public void setBackNumber(String backNumber) {
        this.backNumber = backNumber;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}