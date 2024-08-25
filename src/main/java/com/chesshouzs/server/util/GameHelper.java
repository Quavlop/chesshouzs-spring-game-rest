package com.chesshouzs.server.util;

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
    
}
