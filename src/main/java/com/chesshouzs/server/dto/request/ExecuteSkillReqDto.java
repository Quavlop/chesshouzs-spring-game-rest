package com.chesshouzs.server.dto.request;

import java.util.UUID;

import com.chesshouzs.server.dto.custom.match.PositionDto;

public class ExecuteSkillReqDto {
    
    private UUID skillId;
    private String state;
    private PositionDto position;
    private UUID playerId; 
    private UUID gameId;


    public ExecuteSkillReqDto(UUID skillId, String state, PositionDto position, UUID playerId, UUID gameId) {
        this.skillId = skillId;
        this.state = state;
        this.position = position;
        this.playerId = playerId;
        this.gameId = gameId;
    }


    public UUID getSkillId() {
        return this.skillId;
    }

    public void setSkillId(UUID skillId) {
        this.skillId = skillId;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public PositionDto getPosition() {
        return this.position;
    }

    public void setPosition(PositionDto position) {
        this.position = position;
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getGameId() {
        return this.gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }
    
}
