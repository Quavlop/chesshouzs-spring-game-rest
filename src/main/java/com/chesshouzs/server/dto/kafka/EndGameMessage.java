package com.chesshouzs.server.dto.kafka;

import java.util.UUID;

public class EndGameMessage {
    private UUID winnerId; 
    private UUID loserId;
    private double winnerNewElo; 
    private double loserNewElo;
    private String type;


    public EndGameMessage(UUID winnerId, UUID loserId, double winnerNewElo, double loserNewElo, String type) {
        this.winnerId = winnerId;
        this.loserId = loserId;
        this.winnerNewElo = winnerNewElo;
        this.loserNewElo = loserNewElo;
        this.type = type;
    }


    public UUID getWinnerId() {
        return this.winnerId;
    }

    public void setWinnerId(UUID winnerId) {
        this.winnerId = winnerId;
    }

    public UUID getLoserId() {
        return this.loserId;
    }

    public void setLoserId(UUID loserId) {
        this.loserId = loserId;
    }

    public double getWinnerNewElo() {
        return this.winnerNewElo;
    }

    public void setWinnerNewElo(double winnerNewElo) {
        this.winnerNewElo = winnerNewElo;
    }

    public double getLoserNewElo() {
        return this.loserNewElo;
    }

    public void setLoserNewElo(double loserNewElo) {
        this.loserNewElo = loserNewElo;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
