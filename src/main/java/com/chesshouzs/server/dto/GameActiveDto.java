package com.chesshouzs.server.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class GameActiveDto {

    private UUID id;
    private UserDto whitePlayer; 
    private UserDto blackPlayer;
    private GameTypeVariantDto gameTypeVariant;
    private LocalDateTime startTime; 
    private LocalDateTime endTime;
    private String gameNotation;


    public GameActiveDto(UUID id, UserDto whitePlayer, UserDto blackPlayer, GameTypeVariantDto gameTypeVariant, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.gameTypeVariant = gameTypeVariant;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UUID getId(){
        return this.id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public UserDto getWhitePlayer() {
        return this.whitePlayer;
    }

    public void setWhitePlayer(UserDto whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public UserDto getBlackPlayer() {
        return this.blackPlayer;
    }

    public void setBlackPlayer(UserDto blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public GameTypeVariantDto getGameTypeVariant() {
        return this.gameTypeVariant;
    }

    public void setGameTypeVariant(GameTypeVariantDto gameTypeVariant) {
        this.gameTypeVariant = gameTypeVariant;
    }

    public String getGameNotation(){
        return this.gameNotation;
    }

    public void setGameNotation(String gameNotation){
        this.gameNotation = gameNotation;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
}
