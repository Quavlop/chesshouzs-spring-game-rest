package com.chesshouzs.server.util;

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
    
}
