package com.chesshouzs.server.model.cassandra.keys;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;

@PrimaryKeyClass
public class PlayerGameStatePrimaryKeys {

    @Column("player_id")
    private String playerId;

    @Column("game_id")
    private String gameId;

    public PlayerGameStatePrimaryKeys(String playerId, String gameId) {
        this.playerId = playerId;
        this.gameId = gameId;
    }


    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return this.gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

}

