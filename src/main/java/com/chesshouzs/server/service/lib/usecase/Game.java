package com.chesshouzs.server.service.lib.usecase;

import java.util.HashMap;
import java.util.Map;

import com.chesshouzs.server.constants.GameConstants;
import com.chesshouzs.server.dto.custom.match.PositionDto;
import com.chesshouzs.server.service.lib.interfaces.Character;
import com.chesshouzs.server.util.GameHelper;
import com.chesshouzs.server.util.Helper;

public class Game {
    
    public static Boolean isValidMovement(Map<String, Object> data){
        return (Boolean)data.get(GameConstants.KEY_VALID_MOVE);
    }

    /*
        returns {
            valid_move : [Boolean]
            character : [Character.class]
            old_position : [PositionDto.class]
            new_position : [PositionDto.class]
        }
    */
    public static Map<String, Object> getMovementData(char[][] oldState, char[][] newState){
        Map<String, Object> data = new HashMap<String, Object>();

        // compare the old and new state by checking if movement is only on one piece
        Map<String, Object> doubleMovementScanResult = doubleMovementScanResult(oldState, newState);
        if ((Boolean)doubleMovementScanResult.get(GameConstants.KEY_IS_DOUBLE)){
            data.put(GameConstants.KEY_VALID_MOVE, false);
            return data;
        }

        data.put(GameConstants.KEY_VALID_MOVE, true);

        // map to corresponding class object
        char movingChar = (char)doubleMovementScanResult.get(GameConstants.KEY_CHARACTER);

        // map and set the position (color has been set from map)
        Character characterObj = GameHelper.map(movingChar);
        characterObj.setPosition((PositionDto)doubleMovementScanResult.get(GameConstants.KEY_NEW_POSITION));

        data.put(GameConstants.KEY_CHARACTER, characterObj);
        data.put(GameConstants.KEY_OLD_POSITION, doubleMovementScanResult.get(GameConstants.KEY_OLD_POSITION));
        data.put(GameConstants.KEY_NEW_POSITION, doubleMovementScanResult.get(GameConstants.KEY_NEW_POSITION));

    
        return data;
    }

    /* 
        returns {   
            is_double : [Boolean]
            character : [char]
            old_position : [PositionDto.class]
            new_position : [PositionDto.class]

        }
     */
    public static Map<String, Object> doubleMovementScanResult(char[][] oldState, char[][] newState){

        Map<String, Object> data = new HashMap<String, Object>();

        int boardSize = oldState.length;
        
        char movingChar = ' ';
        
        for (int row = 0; row < boardSize; row++){
            for (int col = 0; col < boardSize; col++){
                if (oldState[row][col] != newState[row][col]){
                    if (oldState[row][col] == GameConstants.NONCHARACTER_EMPTY && newState[row][col] != GameConstants.NONCHARACTER_EMPTY){
                        if (!Helper.isCharEmpty(movingChar)){
                            data.put(GameConstants.KEY_IS_DOUBLE, true);
                            data.put(GameConstants.KEY_NEW_POSITION, null);
                            data.put(GameConstants.KEY_OLD_POSITION, null);
                            data.put(GameConstants.KEY_CHARACTER, '\u0000');
                            return data;
                        }
                        movingChar = newState[row][col];
                        data.put(GameConstants.KEY_NEW_POSITION, new PositionDto(row, col));
                    } 

                    if (oldState[row][col] != GameConstants.NONCHARACTER_EMPTY && newState[row][col] == GameConstants.NONCHARACTER_EMPTY){
                        if (data.get(GameConstants.KEY_OLD_POSITION) != null){
                            data.put(GameConstants.KEY_IS_DOUBLE, true);
                            data.put(GameConstants.KEY_NEW_POSITION, null);
                            data.put(GameConstants.KEY_OLD_POSITION, null);
                            data.put(GameConstants.KEY_CHARACTER, '\u0000');                            
                            return data;
                        }
                        data.put(GameConstants.KEY_OLD_POSITION, new PositionDto(row, col));
                    }
                }
            }
        }

        data.put(GameConstants.KEY_IS_DOUBLE, false);
        data.put(GameConstants.KEY_CHARACTER, movingChar);

        return data;
    }

}
