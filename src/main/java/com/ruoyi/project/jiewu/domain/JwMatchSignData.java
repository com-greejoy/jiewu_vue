package com.ruoyi.project.jiewu.domain;

import java.math.BigDecimal;

public class JwMatchSignData {
    Long matchId;
    Long signCount;
    Long signSportCount;
    int teamCount;
    BigDecimal allFee;

    public BigDecimal getAllFee() {
        return allFee;
    }

    public void setAllFee(BigDecimal allFee) {
        this.allFee = allFee;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getSignCount() {
        return signCount;
    }

    public void setSignCount(Long signCount) {
        this.signCount = signCount;
    }

    public Long getSignSportCount() {
        return signSportCount;
    }

    public void setSignSportCount(Long signSportCount) {
        this.signSportCount = signSportCount;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }
}
