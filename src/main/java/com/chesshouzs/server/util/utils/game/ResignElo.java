package com.chesshouzs.server.util.utils.game;

import com.chesshouzs.server.constants.GameConstants;

public class ResignElo implements Elo {
    public double CalculatePostMatchElo(Integer elo, Integer enemyElo,boolean win){
        double expectation = 1.0 / (1.0 + Math.pow(10, (enemyElo - elo) / 400.0));
        if (win){
            return Math.max(elo + GameConstants.ELO_CALC_K_FACTOR * (1 + expectation), 0);
        }
        return Math.max(elo + GameConstants.ELO_CALC_K_FACTOR * (1 - expectation), 0);
    }
}