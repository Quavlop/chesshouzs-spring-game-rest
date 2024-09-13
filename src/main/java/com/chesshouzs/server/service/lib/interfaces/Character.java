package com.chesshouzs.server.service.lib.interfaces;

import java.util.Map;

import com.datastax.oss.driver.shaded.guava.common.collect.Table;

public interface Character {
    // key : (row, col) | value : (boolean)
    Table<Integer, Integer, Boolean> getEligibleMoves(int row, int col, char[][] state);

    // 0 -> row
    // 1 -> col
    Boolean isValidMove(int[] oldPosition, int[] newPosition);
    
}