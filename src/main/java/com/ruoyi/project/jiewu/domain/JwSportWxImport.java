package com.ruoyi.project.jiewu.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;

import javax.validation.constraints.NotEmpty;

public class JwSportWxImport   {

    @NotEmpty(message = "姓名不能为空")
    @Excel(name = "姓名")
    private String playerName;

    @NotEmpty(message = "身份证不能为空")
    @Excel(name = "身份证")
    private String idCard;

    @NotEmpty(message = "手机号不能为空")
    @Excel(name = "手机号")
    private String playerPhone;

    public String getPlayerPhone() {
        return playerPhone;
    }

    public void setPlayerPhone(String playerPhone) {
        this.playerPhone = playerPhone;
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
