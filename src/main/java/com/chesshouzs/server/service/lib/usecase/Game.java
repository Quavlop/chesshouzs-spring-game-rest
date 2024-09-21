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
        if ((Boolean)doubleMovementScanResult.get(GameConstants.KEY_IS_DOUBLE) || (Boolean)doubleMovementScanResult.get(GameConstants.KEY_INVALID)){
            data.put(GameConstants.KEY_VALID_MOVE, false);
            return data;
        }

        data.put(GameConstants.KEY_VALID_MOVE, true);

        // map to corresponding class object
        char movingChar = (char)doubleMovementScanResult.get(GameConstants.KEY_CHARACTER);
        // System.out.println("SLLS" + movingChar);

        if (movingChar == '\u0000'){
            data.put(GameConstants.KEY_VALID_MOVE, false);
            return data;
        }


        // map and set the position (color has been set from map)
        Character characterObj = GameHelper.map(movingChar);
        characterObj.setPosition((PositionDto)doubleMovementScanResult.get(GameConstants.KEY_NEW_POSITION));

        data.put(GameConstants.KEY_CHARACTER, characterObj);
        data.put(GameConstants.KEY_OLD_POSITION, doubleMovementScanResult.get(GameConstants.KEY_OLD_POSITION));
        data.put(GameConstants.KEY_NEW_POSITION, doubleMovementScanResult.get(GameConstants.KEY_NEW_POSITION));

    
        return data;
    }


    // public static Boolean containsUnknownCharacter(char[][] state){
    //     int boardSize = state.length;

    //     for (int row = 0; row < boardSize; row++){
    //         for (int col = 0; col < boardSize; col++){

    //         }
    //     }

    //     return false;
    // }   

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
        
        // check row dimension
        if (boardSize <= 0 || boardSize != GameConstants.boardSize){
            data.put(GameConstants.KEY_INVALID, true);
            data.put(GameConstants.KEY_IS_DOUBLE, false);
            data.put(GameConstants.KEY_NEW_POSITION, null);
            data.put(GameConstants.KEY_OLD_POSITION, null);
            data.put(GameConstants.KEY_CHARACTER, '\u0000');
            return data;
        }
        
        // check col dimension
        if (oldState[0].length <= 0 || oldState[0].length != GameConstants.boardSize){
            data.put(GameConstants.KEY_INVALID, true);
            data.put(GameConstants.KEY_IS_DOUBLE, false);
            data.put(GameConstants.KEY_NEW_POSITION, null);
            data.put(GameConstants.KEY_OLD_POSITION, null);
            data.put(GameConstants.KEY_CHARACTER, '\u0000');
            return data;
        }
        
        char movingChar = ' ';
        Map<java.lang.Character, Boolean> validCharacters = GameConstants.validCharacters;

        for (int row = 0; row < boardSize; row++){
            for (int col = 0; col < boardSize; col++){
                // check if board contains invalid characters;
                if (!validCharacters.containsKey(java.lang.Character.valueOf(newState[row][col])) || !validCharacters.containsKey(java.lang.Character.valueOf(oldState[row][col]))){
                    data.put(GameConstants.KEY_INVALID, true);
                    data.put(GameConstants.KEY_IS_DOUBLE, false);
                    data.put(GameConstants.KEY_NEW_POSITION, null);
                    data.put(GameConstants.KEY_OLD_POSITION, null);
                    data.put(GameConstants.KEY_CHARACTER, '\u0000');
                    return data;
                }

                if (oldState[row][col] != newState[row][col]){
                    if (newState[row][col] != GameConstants.NONCHARACTER_EMPTY){
                        if (!Helper.isCharEmpty(movingChar)){
                            data.put(GameConstants.KEY_IS_DOUBLE, true);
                            data.put(GameConstants.KEY_INVALID, true);
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
                            data.put(GameConstants.KEY_INVALID, true);
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


        if (movingChar == GameConstants.NONCHARACTER_EMPTY || movingChar == GameConstants.NONCHARACTER_WALL || (PositionDto)data.get(GameConstants.KEY_OLD_POSITION) == null || (PositionDto)data.get(GameConstants.KEY_NEW_POSITION) == null){
            data.put(GameConstants.KEY_INVALID, true);
            data.put(GameConstants.KEY_IS_DOUBLE, false);
            data.put(GameConstants.KEY_NEW_POSITION, null);
            data.put(GameConstants.KEY_OLD_POSITION, null);
            data.put(GameConstants.KEY_CHARACTER, '\u0000');
            return data;
        }

        data.put(GameConstants.KEY_INVALID, false);
        data.put(GameConstants.KEY_IS_DOUBLE, false);
        data.put(GameConstants.KEY_CHARACTER, movingChar);

        return data;
    }

}
