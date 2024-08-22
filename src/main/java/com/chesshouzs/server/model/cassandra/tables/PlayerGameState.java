package com.chesshouzs.server.model.cassandra.tables;

import org.springframework.data.cassandra.core.mapping.Table;

import com.chesshouzs.server.model.cassandra.keys.PlayerGameStatePrimaryKeys;
import com.chesshouzs.server.model.cassandra.types.State;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import java.util.Map;

@Table("player_game_states")
public class PlayerGameState {

    @PrimaryKey
    private PlayerGameStatePrimaryKeys id;

    @Column("buff_state")
    private Map<String, State> buffState;

    @Column("debuff_state")
    private Map<String, State> debuffState;

    public PlayerGameState() {}

    public PlayerGameState(PlayerGameStatePrimaryKeys id, Map<String, State> buffState, Map<String, State> debuffState) {
        this.id = id;
        this.buffState = buffState;
        this.debuffState = debuffState;
    }

    public PlayerGameStatePrimaryKeys getId() {
        return id;
    }

    public void setId(PlayerGameStatePrimaryKeys id) {
        this.id = id;
    }

    public Map<String, State> getBuffState() {
        return buffState;
    }

    public void setBuffState(Map<String, State> buffState) {
        this.buffState = buffState;
    }

    public Map<String, State> getDebuffState() {
        return debuffState;
    }

    public void setDebuffState(Map<String, State> debuffState) {
        this.debuffState = debuffState;
    }
}