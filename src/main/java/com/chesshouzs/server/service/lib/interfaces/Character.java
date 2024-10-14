package com.chesshouzs.server.service.lib.interfaces;

import java.util.Map;

import com.chesshouzs.server.dto.custom.match.PositionDto;
import com.datastax.oss.driver.shaded.guava.common.collect.Table;

public abstract class Character {

    // used for new position of the moving character OR external pieces position
    public PositionDto position;

    public String color;

    public Character(String color){
        this.color = color;
    }

    public Character(String color, PositionDto position){
        this.color = color;
        this.position = position;
    }

    // key : (row, col) | value : (boolean)
    public abstract Table<Integer, Integer, Boolean> getEligibleMoves(char[][] state);

    /*
        validate if :
            - [IF KING] the newPosition is guarded by enemy
            - rook, queen, bishop movement should not overflow (ex : went pass enemy)
            - if pawn is killing, then the same old state position must contain enemy
            - if pawn is moving forward, then the same old state position must NOT contain enemy
            - new movement must not cause king to be ATTACKED
     */
    public abstract Boolean isValidMove(PositionDto oldPosition, char[][] oldState, char[][] newState);

    // this applies for blocking attacks OR eliminating the attacker
    public abstract Boolean isAbleToEliminateCheckThreat(Character attacker, PositionDto kingPosition, char[][] oldState);


    public PositionDto getPosition() {
        return this.position;
    }

    public void setPosition(PositionDto position) {
        this.position = position;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    

}