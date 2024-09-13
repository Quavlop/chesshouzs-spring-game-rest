package com.chesshouzs.server.service.lib.game;

import com.chesshouzs.server.service.lib.interfaces.Character;
import com.datastax.oss.driver.shaded.guava.common.collect.Table;

public class Bishop implements Character{

    // key : (row, col) | value : (boolean)
    public Table<Integer, Integer, Boolean> getEligibleMoves(int row, int col, char[][] state){
        return null;
    }

    // 0 -> row
    // 1 -> col
    public Boolean isValidMove(int[] oldPosition, int[] newPosition){
        return true;
    }

    
}
