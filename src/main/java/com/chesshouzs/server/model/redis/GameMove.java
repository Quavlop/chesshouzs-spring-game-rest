package com.chesshouzs.server.model.redis;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Id;

@RedisHash("GameMove")
public class GameMove implements Serializable {

    @Id 
    private String id;
    
    private String move;
    private String turn;

    public GameMove(String move, String turn) {
        this.move = move;
        this.turn = turn;
    }

    public String getMove() {
        return this.move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getTurn() {
        return this.turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }
}


