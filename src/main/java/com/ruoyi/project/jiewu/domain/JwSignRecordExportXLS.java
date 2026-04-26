package com.ruoyi.project.jiewu.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;

import java.util.Date;

public class JwSignRecordExportXLS {
    @Excel(name = "场地")
    private String scheduleName;
    @Excel(name = "场次")
    private String schedulePlace;
    @Excel(name = "上场序号")
    private Long indexOrder;
    @Excel(name = "时间", dateFormat = "MM-dd HH:mm")
    private Date indexTime;
    @Excel(name = "选手编号")
    private String backNumber;
    @Excel(name = "选手")
    private String playerName;
    @Excel(name = "代码")
    private String gameItemCode;
    @Excel(name = "组别")
    private String gameItemName;
    @Excel(name = "代表队")
    private String teamName;



    private Long placeOrder;
    private Long scheduleIndex;


    public Long getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(Long placeOrder) {
        this.placeOrder = placeOrder;
    }

    public Long getScheduleIndex() {
        return scheduleIndex;
    }

    public void setScheduleIndex(Long scheduleIndex) {
        this.scheduleIndex = scheduleIndex;
    }

    public Long getIndexOrder() {
        return indexOrder;
    }

    public void setIndexOrder(Long indexOrder) {
        this.indexOrder = indexOrder;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getSchedulePlace() {
        return schedulePlace;
    }

    public void setSchedulePlace(String schedulePlace) {
        this.schedulePlace = schedulePlace;
    }

    public Date getIndexTime() {
        return indexTime;
    }

    public void setIndexTime(Date indexTime) {
        this.indexTime = indexTime;
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

    public String getGameItemCode() {
        return gameItemCode;
    }

    public void setGameItemCode(String gameItemCode) {
        this.gameItemCode = gameItemCode;
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
}