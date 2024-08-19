package com.chesshouzs.server.constants;

public class RedisConstants {
    public static String GAME_MOVE_KEY_IDENTIFIER = "game_move:";
    public static String GAME_SKILL_KEY_IDENTIFIER = "player_match_skill:";

    public static String getGameMoveKey(String id){
        return GAME_MOVE_KEY_IDENTIFIER + id;
    }

    public static String getPlayerMatchSkillStatsKey(String id ){
        return GAME_SKILL_KEY_IDENTIFIER + id;
    }
}
