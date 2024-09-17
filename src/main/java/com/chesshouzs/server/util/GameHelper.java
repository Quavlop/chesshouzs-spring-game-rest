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
        if (character == GameConstants.NONCHARACTER_EMPTY || character == GameConstants.NONCHARACTER_WALL){
            return null;
        }
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
        String pawnColor = getPawnColor(ch);
        if (pawnColor != null){
            return new com.chesshouzs.server.service.lib.game.Pawn(pawnColor);
        }

        String knightColor = getKnightColor(ch);
        if (knightColor != null){
            return new com.chesshouzs.server.service.lib.game.Knight(knightColor);
        }

        String kingColor = getKingColor(ch);
        if (kingColor != null){
            return new com.chesshouzs.server.service.lib.game.King(kingColor);
        }

        String queenColor = getQueenColor(ch);
        if (queenColor != null){
            return new com.chesshouzs.server.service.lib.game.Queen(queenColor);
        }

        String bishopColor = getBishopColor(ch);
        if (bishopColor != null){
            return new com.chesshouzs.server.service.lib.game.Bishop(bishopColor);
        }

        String rookColor = getRookColor(ch);
        if (rookColor != null){
            return new com.chesshouzs.server.service.lib.game.Rook(rookColor);
        }

        String evolvedPawnColor = getEvolvedPawnColor(ch);
        if (evolvedPawnColor != null){
            return new com.chesshouzs.server.service.lib.game.EvolvedPawn(evolvedPawnColor);
        }

        return null;
    }

    /*
        returns {
            color : [String]
        }
     */
    public static String getPawnColor(char ch){
        if (!isCharPawn(ch)){
            return null;
        }
        return getPieceColor(ch);
    }

    /*
        returns {
            color : [String]
        }
     */
    public static String getKingColor(char ch){
        if (!isCharKing(ch)){
            return null;
        }
        return getPieceColor(ch);
    }

    /*
        returns {
            color : [String]
        }
     */
    public static String getEvolvedPawnColor(char ch){
        if (!isCharEvolvedPawn(ch)){
            return null;
        }
        return getPieceColor(ch);
    }    

    /*
        returns {
            color : [String]
        }
     */
    public static String getKnightColor(char ch){
        if (!isCharKnight(ch)){
            return null;
        }
        return getPieceColor(ch);
    }

    /*
        returns {
            color : [String]
        }
     */
    public static String getQueenColor(char ch){
        if (!isCharQueen(ch)){
            return null;
        }
        return getPieceColor(ch);
    }
    
    /*
        returns {
            color : [String]
        }
     */
    public static String getBishopColor(char ch){
        if (!isCharBishop(ch)){
            return null;
        }
        return getPieceColor(ch);
    }    

    /*
        returns {
            color : [String]
        }
     */
    public static String getRookColor(char ch){
        if (!isCharRook(ch)){
            return null;
        }
        return getPieceColor(ch);
    }    

    public static Boolean isCharKing(char ch){
        return ch == GameConstants.WHITE_CHARACTER_KING || ch == GameConstants.BLACK_CHARACTER_KING;
    }

    public static Boolean isCharQueen(char ch){
        return ch == GameConstants.WHITE_CHARACTER_QUEEN || ch == GameConstants.BLACK_CHARACTER_QUEEN;
    }

    public static Boolean isCharPawn(char ch){
        return ch == GameConstants.WHITE_CHARACTER_PAWN || ch == GameConstants.BLACK_CHARACTER_PAWN;
    }

    public static Boolean isCharKnight(char ch){
        return ch == GameConstants.WHITE_CHARACTER_KNIGHT || ch == GameConstants.BLACK_CHARACTER_KNIGHT;
    }

    public static Boolean isCharBishop(char ch){
        return ch == GameConstants.WHITE_CHARACTER_BISHOP || ch == GameConstants.BLACK_CHARACTER_BISHOP;
    }

    public static Boolean isCharRook(char ch){
        return ch == GameConstants.WHITE_CHARACTER_ROOK || ch == GameConstants.BLACK_CHARACTER_ROOK;
    }

    public static Boolean isCharEvolvedPawn(char ch){
        return ch == GameConstants.WHITE_CHARACTER_EVOLVED_PAWN || ch == GameConstants.BLACK_CHARACTER_EVOLVED_PAWN;
    }

    public static Boolean isWall(char ch){
        return ch == GameConstants.NONCHARACTER_WALL;
    }

    public static Boolean getDiagonalAttackers(char ch, String playerColor){
        return getQueenColor(ch) != playerColor || getBishopColor(ch) != playerColor;
    }

    public static Boolean getFlatDirectionAttackers(char ch, String playerColor){
        return getQueenColor(ch) != playerColor || getRookColor(ch) != playerColor;
    }

    public static String getRedisGameMoveKey(String ref){
        return "game_move:" + ref;
    }

    public static String getRedisPlayerMatchSkillKey(UUID playerId){
        return "player_match_skill:" + playerId.toString();
    }
    
}
