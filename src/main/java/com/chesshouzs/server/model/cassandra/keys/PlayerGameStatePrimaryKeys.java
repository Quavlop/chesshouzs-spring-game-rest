package com.chesshouzs.server.model.cassandra.keys;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class PlayerGameStatePrimaryKeys {

    @PrimaryKeyColumn(name = "player_id", type = PrimaryKeyType.PARTITIONED)
    private String playerId;

    @PrimaryKeyColumn(name = "game_id", type = PrimaryKeyType.CLUSTERED)
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

