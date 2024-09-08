package com.chesshouzs.server.util.utils.game;

public interface Elo {
    double CalculatePostMatchElo(Integer elo, Integer enemyElo, boolean win);
} 
