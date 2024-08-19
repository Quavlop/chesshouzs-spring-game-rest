package com.chesshouzs.server.dto.kafka;

import java.util.UUID;

public class ExecuteSkillMessage {
    private String state; 
    private UUID gameId; 
    private UUID executorUserId;
    private UUID skillId; 

    public ExecuteSkillMessage(){}

    public ExecuteSkillMessage(String state, UUID gameId, UUID executorUserId, UUID skillId) {
        this.state = state;
        this.gameId = gameId;
        this.executorUserId = executorUserId;
        this.skillId = skillId;
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

    public UUID getSkillId() {
        return this.skillId;
    }

    public void setSkillData(UUID skillId) {
        this.skillId = skillId;
    }

}
