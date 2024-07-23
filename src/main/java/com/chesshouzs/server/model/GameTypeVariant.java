package com.chesshouzs.server.model;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.chesshouzs.server.dto.GameTypeVariantDto;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class GameTypeVariant implements ModelInterface<GameTypeVariant, GameTypeVariantDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "game_type_id", nullable = false)
    @JsonBackReference
    private GameType gameType;

    private int duration; 
    private int increment; 
    private Date createdAt; 
    private Date updatedAt;
 

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIncrement() {
        return this.increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // dto methods 
    public GameTypeVariantDto convertToDto(GameTypeVariant data){
        return new GameTypeVariantDto(
            data.id, 
            data.gameType.getName(), 
            data.duration, 
            data.increment
        );
    }
}
