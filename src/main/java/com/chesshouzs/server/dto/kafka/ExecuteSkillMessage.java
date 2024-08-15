package com.chesshouzs.server.dto.kafka;

import java.util.UUID;

import com.chesshouzs.server.dto.custom.match.PlayerSkillDataCountDto;

public class ExecuteSkillMessage {
    private String state; 
    private UUID gameId; 
    private UUID executorUserId;
    private PlayerSkillDataCountDto skillData; 

    public ExecuteSkillMessage(){}

    public ExecuteSkillMessage(String state, UUID gameId, UUID executorUserId, PlayerSkillDataCountDto skillData) {
        this.state = state;
        this.gameId = gameId;
        this.executorUserId = executorUserId;
        this.skillData = skillData;
    }

    public ExecuteSkillMessage(String state, UUID gameId, UUID executorUserId) {
        this.state = state;
        this.gameId = gameId;
        this.executorUserId = executorUserId;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public UUID getGameId() {
        return this.gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getExecutorUserId() {
        return this.executorUserId;
    }

    public void setExecutorUserId(UUID executorUserId) {
        this.executorUserId = executorUserId;
    }

    public PlayerSkillDataCountDto getSkillData() {
        return this.skillData;
    }

    public void setSkillData(PlayerSkillDataCountDto skillData) {
        this.skillData = skillData;
    }

}
