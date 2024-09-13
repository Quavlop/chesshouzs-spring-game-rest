package com.chesshouzs.server.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.chesshouzs.server.constants.GameConstants;

public class GameHelper {

    public static String GameDurationToString(int duration, int increment) {
        String minutes = Integer.toString(duration / 60);
        if (increment != 0){
            minutes += " | " + Integer.toString(increment);
        } else {
            minutes += " min";
        }
    
        return minutes;
    }

    public static char[][] convertNotationToArray(String input) {
        String[] rows = input.split("\\|");
        char[][] result = new char[rows.length][];

        for (int i = 0; i < rows.length; i++) {
            result[i] = rows[i].toCharArray();
        }

        return result;
    }

    public static String convertArrayToNotation(char[][] array) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : array) {
            sb.append(new String(row)).append("|");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    public static String getPieceColor(char character){
        if (Character.toUpperCase(character) == character){
            return GameConstants.WHITE_COLOR;
        }
        return GameConstants.BLACK_COLOR;
    }

    public static char[][] transformBoard(char[][] array){
        int len = array.length;

        char[][] newState = new char[len][len];

        for (int row = 0; row < len; row++) {
            System.arraycopy(array[row], 0, newState[row], 0, len);
        }

        for (int row = 0; row < len / 2; row++) {
            for (int col = 0; col < len; col++) {
                char temp = newState[row][col];
                newState[row][col] = newState[len - row - 1][len - col - 1];
                newState[len - row - 1][len - col - 1] = temp;
            }
        }

        return newState;
    }

    public static com.chesshouzs.server.service.lib.interfaces.Character map(char ch){
        Map<String, Object> pawn = pawnCheck(ch);
        if ((Boolean)pawn.get(GameConstants.KEY_VALID)){
            return new com.chesshouzs.server.service.lib.game.Pawn();
        }

        Map<String, Object> knight = knightCheck(ch);
        if ((Boolean)knight.get(GameConstants.KEY_VALID)){
            return new com.chesshouzs.server.service.lib.game.Knight();
        }

        Map<String, Object> king = kingCheck(ch);
        if ((Boolean)king.get(GameConstants.KEY_VALID)){
            return new com.chesshouzs.server.service.lib.game.King();
        }

        Map<String, Object> queen = queenCheck(ch);
        if ((Boolean)queen.get(GameConstants.KEY_VALID)){
            return new com.chesshouzs.server.service.lib.game.Queen();
        }

        Map<String, Object> bishop = bishopCheck(ch);
        if ((Boolean)bishop.get(GameConstants.KEY_VALID)){
            return new com.chesshouzs.server.service.lib.game.Bishop();
        }

        Map<String, Object> rook = rookCheck(ch);
        if ((Boolean)rook.get(GameConstants.KEY_VALID)){
            return new com.chesshouzs.server.service.lib.game.Rook();
        }

        Map<String, Object> evolvedPawn = evolvedPawnCheck(ch);
        if ((Boolean)evolvedPawn.get(GameConstants.KEY_VALID)){
            return new com.chesshouzs.server.service.lib.game.EvolvedPawn();
        }

        return null;
    }

    /*
        returns {
            color : [String], 
            valid : [Boolean]
        }
     */
    public static Map<String, Object> pawnCheck(char ch){
        Map<String, Object> data = new HashMap<String, Object>();

        data.put(GameConstants.KEY_VALID, ch == GameConstants.WHITE_CHARACTER_PAWN || ch == GameConstants.BLACK_CHARACTER_PAWN);
        data.put(GameConstants.KEY_COLOR, getPieceColor(ch));

        return data;
    }

    /*
        returns {
            color : [String], 
            valid : [Boolean]
        }
     */
    public static Map<String, Object> kingCheck(char ch){
        Map<String, Object> data = new HashMap<String, Object>();

        data.put(GameConstants.KEY_VALID, ch == GameConstants.WHITE_CHARACTER_KING || ch == GameConstants.BLACK_CHARACTER_KING);
        data.put(GameConstants.KEY_COLOR, getPieceColor(ch));

        return data;
    }

    /*
        returns {
            color : [String], 
            valid : [Boolean]
        }
     */
    public static Map<String, Object> evolvedPawnCheck(char ch){
        Map<String, Object> data = new HashMap<String, Object>();

        data.put(GameConstants.KEY_VALID, ch == GameConstants.WHITE_CHARACTER_EVOLVED_PAWN || ch == GameConstants.BLACK_CHARACTER_EVOLVED_PAWN);
        data.put(GameConstants.KEY_COLOR, getPieceColor(ch));

        return data;
    }    

    /*
        returns {
            color : [String], 
            valid : [Boolean]
        }
     */
    public static Map<String, Object> knightCheck(char ch){
        Map<String, Object> data = new HashMap<String, Object>();

        data.put(GameConstants.KEY_VALID, ch == GameConstants.WHITE_CHARACTER_KNIGHT || ch == GameConstants.BLACK_CHARACTER_KNIGHT);
        data.put(GameConstants.KEY_COLOR, getPieceColor(ch));

        return data;
    }

    /*
        returns {
            color : [String], 
            valid : [Boolean]
        }
     */
    public static Map<String, Object> queenCheck(char ch){
        Map<String, Object> data = new HashMap<String, Object>();

        data.put(GameConstants.KEY_VALID, ch == GameConstants.WHITE_CHARACTER_QUEEN || ch == GameConstants.BLACK_CHARACTER_QUEEN);
        data.put(GameConstants.KEY_COLOR, getPieceColor(ch));

        return data;
    }
    
    /*
        returns {
            color : [String], 
            valid : [Boolean]
        }
     */
    public static Map<String, Object> bishopCheck(char ch){
        Map<String, Object> data = new HashMap<String, Object>();

        data.put(GameConstants.KEY_VALID, ch == GameConstants.WHITE_CHARACTER_BISHOP || ch == GameConstants.BLACK_CHARACTER_BISHOP);
        data.put(GameConstants.KEY_COLOR, getPieceColor(ch));

        return data;
    }    

    /*
        returns {
            color : [String], 
            valid : [Boolean]
        }
     */
    public static Map<String, Object> rookCheck(char ch){
        Map<String, Object> data = new HashMap<String, Object>();

        data.put(GameConstants.KEY_VALID, ch == GameConstants.WHITE_CHARACTER_ROOK || ch == GameConstants.BLACK_CHARACTER_ROOK);
        data.put(GameConstants.KEY_COLOR, getPieceColor(ch));

        return data;
    }    


    public static String getRedisGameMoveKey(String ref){
        return "game_move:" + ref;
    }

    public static String getRedisPlayerMatchSkillKey(UUID playerId){
        return "player_match_skill:" + playerId.toString();
    }
    
}
