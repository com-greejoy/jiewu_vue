package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.List;

public class JwEight extends BaseEntity{

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long playerId;

    private Long playerIndex;

    private String playerPosition;

    private String playerPkGroup;

    private Long gameItemId;

    private String groupIndex;

    private Long matchId;

    private String description;
    private Long eightOrder;
    private String backNumber;

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getBackNumber() {
        return backNumber;
    }

    public void setBackNumber(String backNumber) {
        this.backNumber = backNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEightOrder() {
        return eightOrder;
    }

    public void setEightOrder(Long eightOrder) {
        this.eightOrder = eightOrder;
    }

    private List<JwSignRecordSport> jwSignRecordSportList;

    public List<JwSignRecordSport> getJwSignRecordSportList() {
        return jwSignRecordSportList;
    }

    public void setJwSignRecordSportList(List<JwSignRecordSport> jwSignRecordSportList) {
        this.jwSignRecordSportList = jwSignRecordSportList;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPlayerId(Long playerId)
    {
        this.playerId = playerId;
    }

    public Long getPlayerId()
    {
        return playerId;
    }
    public void setPlayerIndex(Long playerIndex)
    {
        this.playerIndex = playerIndex;
    }

    public Long getPlayerIndex()
    {
        return playerIndex;
    }
    public void setPlayerPosition(String playerPosition) 
    {
        this.playerPosition = playerPosition;
    }

    public String getPlayerPosition() 
    {
        return playerPosition;
    }
    public void setPlayerPkGroup(String playerPkGroup) 
    {
        this.playerPkGroup = playerPkGroup;
    }

    public String getPlayerPkGroup() 
    {
        return playerPkGroup;
    }
    public void setGameItemId(Long gameItemId) 
    {
        this.gameItemId = gameItemId;
    }

    public Long getGameItemId() 
    {
        return gameItemId;
    }
    public void setGroupIndex(String groupIndex) 
    {
        this.groupIndex = groupIndex;
    }

    public String getGroupIndex() 
    {
        return groupIndex;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("playerId", getPlayerId())
            .append("playerIndex", getPlayerIndex())
            .append("playerPosition", getPlayerPosition())
            .append("playerPkGroup", getPlayerPkGroup())
            .append("gameItemId", getGameItemId())
            .append("groupIndex", getGroupIndex())
            .toString();
    }
}
