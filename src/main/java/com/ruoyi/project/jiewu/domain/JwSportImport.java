package com.ruoyi.project.jiewu.domain;


import com.ruoyi.framework.aspectj.lang.annotation.Excel;

public class JwSportImport   {

    @Excel(name = "组别")
    private String playerGroup;

    @Excel(name = "姓名")
    private String playerName;

    @Excel(name = "背号")
    private String backNum;

    @Excel(name = "单位")
    private String playerTeam;

    public String getPlayerGroup() {
        return playerGroup;
    }

    public void setPlayerGroup(String playerGroup) {
        this.playerGroup = playerGroup;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getBackNum() {
        return backNum;
    }

    public void setBackNum(String backNum) {
        this.backNum = backNum;
    }

    public String getPlayerTeam() {
        return playerTeam;
    }

    public void setPlayerTeam(String playerTeam) {
        this.playerTeam = playerTeam;
    }
}
