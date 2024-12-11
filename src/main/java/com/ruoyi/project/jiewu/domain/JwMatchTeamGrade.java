package com.ruoyi.project.jiewu.domain;

import java.util.List;

public class JwMatchTeamGrade {

    private Long teamId;

    private Long matchId;

    private Long indexOrder;

    private List<JwSignRecord> jwSignRecordList;

    private List<JwAwardsItem> awardsItemList;


    public JwMatchTeamGrade() {
    }

    public JwMatchTeamGrade(Long teamId, Long matchId, Long indexOrder, List<JwSignRecord> jwSignRecordList, List<JwAwardsItem> awardsItemList) {
        this.teamId = teamId;
        this.matchId = matchId;
        this.indexOrder = indexOrder;
        this.jwSignRecordList = jwSignRecordList;
        this.awardsItemList = awardsItemList;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getIndexOrder() {
        return indexOrder;
    }

    public void setIndexOrder(Long indexOrder) {
        this.indexOrder = indexOrder;
    }

    public List<JwSignRecord> getJwSignRecordList() {
        return jwSignRecordList;
    }

    public void setJwSignRecordList(List<JwSignRecord> jwSignRecordList) {
        this.jwSignRecordList = jwSignRecordList;
    }

    public List<JwAwardsItem> getAwardsItemList() {
        return awardsItemList;
    }

    public void setAwardsItemList(List<JwAwardsItem> awardsItemList) {
        this.awardsItemList = awardsItemList;
    }
}
