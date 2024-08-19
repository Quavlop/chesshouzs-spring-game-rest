package com.chesshouzs.server.service.http;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chesshouzs.server.constants.GameConstants;
import com.chesshouzs.server.constants.RedisConstants;
import com.chesshouzs.server.constants.SkillConstants;
import com.chesshouzs.server.dto.custom.match.PositionDto;
import com.chesshouzs.server.dto.kafka.ExecuteSkillMessage;
import com.chesshouzs.server.dto.request.ExecuteSkillReqDto;
import com.chesshouzs.server.model.GameActive;
import com.chesshouzs.server.model.GameSkill;
import com.chesshouzs.server.repository.GameActiveRepository;
import com.chesshouzs.server.repository.GameSkillRepository;
import com.chesshouzs.server.repository.RedisBaseRepository;
import com.chesshouzs.server.util.GameHelper;

@Service
public class SkillService {
    
    @Autowired
    private GameActiveRepository gameActiveRepository;    

    @Autowired 
    private GameSkillRepository gameSkillRepository;

    @Autowired 
    private RedisBaseRepository redis;


    /*
     * the return values of this function will be published to kafka and consumed by the websocket service
     * data {
     *      state : ***
     *      executorUserId : ***
     *      gameId : *** 
     *      skill : *** (returns the updated stats of remaining skill usage count)
     * }
     */

    public ExecuteSkillMessage executeEnlightenedApprentice(UUID userId, ExecuteSkillReqDto params, GameSkill skill) throws Exception {

        GameActive matchData = gameActiveRepository.findPlayerActiveMatch(userId);
        if (matchData == null){
            throw new Exception("Match data not found");
        }

        String key = RedisConstants.getGameMoveKey(matchData.getMovesCacheRef());
        Map<String, String> notation = redis.hgetall(key);
        if (notation == null){
            throw new Exception("Match state not found");
        }

        String turn = notation.get("turn");

        PositionDto position = params.getPosition();
        char[][] state = GameHelper.convertNotationToArray(params.getState());

        if (turn == "1") {
            if (state[position.getRow()][position.getCol()] != GameConstants.WHITE_CHARACTER_PAWN){
                throw new Exception("Invalid character");
            }
        } else {
            if (state[position.getRow()][position.getCol()] != GameConstants.BLACK_CHARACTER_PAWN){
                throw new Exception("Invalid character");
            }
        }

        if (turn == "1"){
            state[position.getRow()][position.getCol()] = GameConstants.WHITE_CHARACTER_EVOLVED_PAWN;
        } else {
            state[position.getRow()][position.getCol()] = GameConstants.BLACK_CHARACTER_EVOLVED_PAWN;
        }

        String newNotation = GameHelper.convertArrayToNotation(state);

        return new ExecuteSkillMessage(newNotation, params.getGameId(), userId, params.getSkillId());
    }

    public ExecuteSkillMessage executeTheGreatWall(UUID userId, ExecuteSkillReqDto params, GameSkill skill) throws Exception{

        GameActive matchData = gameActiveRepository.findPlayerActiveMatch(userId);
        if (matchData == null){
            throw new Exception("Match data not found");
        }

        String key = RedisConstants.getGameMoveKey(matchData.getMovesCacheRef());
        Map<String, String> notation = redis.hgetall(key);
        if (notation == null){
            throw new Exception("Match state not found");
        }

        String turn = notation.get("turn");

        PositionDto position = params.getPosition();
        char[][] state = GameHelper.convertNotationToArray(params.getState());

        if (state[position.getRow()][position.getCol()] != GameConstants.NONCHARACTER_EMPTY) {
            throw new Exception("Skill cannot be used on non-empty square");
        }

        if (position.getRow() < skill.getRowLimit() || position.getRow() > state.length - skill.getRowLimit() - 1){
            throw new Exception("Invalid wall position");
        }

        if (position.getCol() < skill.getColLimit() || position.getCol() > state.length - skill.getColLimit() - 1){
            throw new Exception("Invalid wall position");
        }

        state[position.getRow()][position.getCol()] = GameConstants.NONCHARACTER_WALL;

        String newNotation = GameHelper.convertArrayToNotation(state);

        return new ExecuteSkillMessage(newNotation, params.getGameId(), userId, params.getSkillId());
    }

    public ExecuteSkillMessage executeFogMaster(UUID userId, ExecuteSkillReqDto params, GameSkill skill){
        return new ExecuteSkillMessage(null, userId, userId, null);
    }

    public ExecuteSkillMessage executeFreezingWand(UUID userId, ExecuteSkillReqDto params, GameSkill skill){
        return new ExecuteSkillMessage(null, userId, userId, null);
    }

    public ExecuteSkillMessage executeParalyzer(UUID userId, ExecuteSkillReqDto params, GameSkill skill){
        return new ExecuteSkillMessage(null, userId, userId, null);
    }
    

}
