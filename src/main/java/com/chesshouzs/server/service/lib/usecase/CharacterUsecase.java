package com.chesshouzs.server.service.lib.usecase;

import com.chesshouzs.server.dto.custom.match.PositionDto;
import com.chesshouzs.server.service.lib.game.King;
import com.chesshouzs.server.util.GameHelper;

public class CharacterUsecase {
    
    public static King findKing(char[][] state, String color){
        int boardSize = state.length;

        // Map

        for (int row = 0; row < boardSize; row++){
            for (int col = 0; col < boardSize; col++){
                if (GameHelper.getKingColor(state[row][col]) == color){
                    return new King(new PositionDto(row, col),color);
                }
            }
        }

        return null;
    }

}
