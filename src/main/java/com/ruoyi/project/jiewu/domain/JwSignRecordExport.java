package com.ruoyi.project.jiewu.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;

import javax.validation.constraints.NotEmpty;

public class JwSignRecordExport {

    @Excel(name = "组别")
    private String gameItemName;

    @Excel(name = "代表队")
    private String teamName;

    @Excel(name = "背号")
    private String backNumber;

    @Excel(name = "单选手")
    private String playerNameS;
    @Excel(name = "选手身份证")
    private String playerIdCard;
    @Excel(name = "选手电话")
    private String playerPhone;

    @Excel(name = "选手")
    private String playerName;

    @Excel(name = "联系人")
    private String userName;

    @Excel(name = "联系电话")
    private String userPhone;

    public String getPlayerNameS() {
        return playerNameS;
    }

    public void setPlayerNameS(String playerNameS) {
        this.playerNameS = playerNameS;
    }

    public String getPlayerIdCard() {
        return playerIdCard;
    }

    public void setPlayerIdCard(String playerIdCard) {
        this.playerIdCard = playerIdCard;
    }

    public String getPlayerPhone() {
        return playerPhone;
    }

    public void setPlayerPhone(String playerPhone) {
        this.playerPhone = playerPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

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