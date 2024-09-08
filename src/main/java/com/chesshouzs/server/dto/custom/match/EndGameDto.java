package com.chesshouzs.server.dto.custom.match;

import java.util.UUID;

public class EndGameDto {
    private UUID winnerId; 
    private UUID gameId;
    private String type;  

    public EndGameDto(UUID winnerId, UUID gameId, String type) {
        this.winnerId = winnerId;
        this.gameId = gameId;
        this.type = type;
    }

    public UUID getWinnerId() {
        return this.winnerId;
    }

    public void setWinnerId(UUID winnerId) {
        this.winnerId = winnerId;
    }

    public UUID getGameId() {
        return this.gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
