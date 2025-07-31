package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

public class JwSignRecordSport extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "比赛ID")
    private Long matchId;

    private Long gameItemId;

    private Long teamId;

    @Excel(name = "运动员")
    private Long sportId;

    @Excel(name = "报名记录")
    private Long signRecordId;

    private String playerName;
    private String idCard;
    private String sex;
    private String playerPhone;

    private JwSport jwSport;

    private JwGameItem jwGameItem;

    private Long[] sportIds;

    private String showImg;

    private String gameNameVar;

    public String getGameNameVar() {
        return gameNameVar;
    }

    public void setGameNameVar(String gameNameVar) {
        this.gameNameVar = gameNameVar;
    }

    public String getPlayerPhone() {
        return playerPhone;
    }

    public void setPlayerPhone(String playerPhone) {
        this.playerPhone = playerPhone;
    }

    public String getShowImg() {
        return showImg;
    }

    public void setShowImg(String showImg) {
        this.showImg = showImg;
    }

    public Long[] getSportIds() {
        return sportIds;
    }

    public void setSportIds(Long[] sportIds) {
        this.sportIds = sportIds;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public JwSport getJwSport() {
        return jwSport;
    }

    public void setJwSport(JwSport jwSport) {
        this.jwSport = jwSport;
    }

    public JwGameItem getJwGameItem() {
        return jwGameItem;
    }

    public void setJwGameItem(JwGameItem jwGameItem) {
        this.jwGameItem = jwGameItem;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getGameItemId() {
        return gameItemId;
    }

    public void setGameItemId(Long gameItemId) {
        this.gameItemId = gameItemId;
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

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMatchId(Long matchId) 
    {
        this.matchId = matchId;
    }

    public Long getMatchId() 
    {
        return matchId;
    }
    public void setSportId(Long sportId) 
    {
        this.sportId = sportId;
    }

    public Long getSportId() 
    {
        return sportId;
    }
    public void setSignRecordId(Long signRecordId) 
    {
        this.signRecordId = signRecordId;
    }

    public Long getSignRecordId() 
    {
        return signRecordId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("matchId", getMatchId())
            .append("sportId", getSportId())
            .append("signRecordId", getSignRecordId())
            .toString();
    }
}
