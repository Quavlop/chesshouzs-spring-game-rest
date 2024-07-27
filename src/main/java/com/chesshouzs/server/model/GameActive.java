package com.chesshouzs.server.model;


import java.time.LocalDateTime;
import java.util.UUID;

import com.chesshouzs.server.dto.GameActiveDto;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class GameActive implements ModelInterface<GameActive, GameActiveDto>{

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    
    private String movesCacheRef; 
    private String moves; 
    private boolean isDone; 
    private LocalDateTime startTime; 
    private LocalDateTime endTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="game_type_variant_id", nullable = false)
    @JsonBackReference
    private GameTypeVariant gameTypeVariant; 


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="white_player_id", nullable = false)
    @JsonBackReference
    private Users whitePlayer; 

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="black_player_id", nullable = false)
    @JsonBackReference
    private Users blackPlayer; 

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="winner_player_id", nullable = false)
    @JsonBackReference
    private Users winnerPlayer; 

    public GameActive(){}

    public GameActive(UUID id, String movesCacheRef, String moves, boolean isDone, LocalDateTime startTime, LocalDateTime endTime, GameTypeVariant gameTypeVariant, Users whitePlayer, Users blackPlayer, Users winnerPlayer) {
        this.id = id;
        this.movesCacheRef = movesCacheRef;
        this.moves = moves;
        this.isDone = isDone;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gameTypeVariant = gameTypeVariant;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.winnerPlayer = winnerPlayer;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMovesCacheRef() {
        return this.movesCacheRef;
    }

    public void setMovesCacheRef(String movesCacheRef) {
        this.movesCacheRef = movesCacheRef;
    }

    public String getMoves() {
        return this.moves;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

    public boolean isIsDone() {
        return this.isDone;
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
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

    public GameTypeVariant getGameTypeVariant() {
        return this.gameTypeVariant;
    }

    public void setGameTypeVariant(GameTypeVariant gameTypeVariant) {
        this.gameTypeVariant = gameTypeVariant;
    }

    public Users getWhitePlayer() {
        return this.whitePlayer;
    }

    public void setWhitePlayer(Users whitePlayer) {
        this.whitePlayer = whitePlayer;
    }

    public Users getBlackPlayer() {
        return this.blackPlayer;
    }

    public void setBlackPlayer(Users blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public Users getWinnerPlayer() {
        return this.winnerPlayer;
    }

    public void setWinnerPlayer(Users winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }

    public GameActiveDto convertToDto(GameActive data){
        return new GameActiveDto(
            this.id,
            this.whitePlayer.convertToDto(this.whitePlayer), 
            this.blackPlayer.convertToDto(this.blackPlayer),  
            this.gameTypeVariant.convertToDto(this.gameTypeVariant), 
            this.startTime, 
            this.endTime
        );
    }
}
