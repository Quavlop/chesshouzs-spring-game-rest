package com.chesshouzs.server.service.http;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chesshouzs.server.dto.custom.match.PositionDto;
import com.chesshouzs.server.dto.kafka.ExecuteSkillMessage;
import com.chesshouzs.server.dto.request.ExecuteSkillReqDto;
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

    public ExecuteSkillMessage executeEnlightenedApprentice(UUID userId, ExecuteSkillReqDto params){
        PositionDto position = params.getPosition();
        char[][] state = GameHelper.convertNotationToArray(params.getState());

        state[position.getRow()][position.getCol()] = 'E';

        String newNotation = GameHelper.convertArrayToNotation(state);

        return new ExecuteSkillMessage(newNotation, params.getGameId(), userId);
    }

    public ExecuteSkillMessage executeTheGreatWall(UUID userId, ExecuteSkillReqDto params){
        return new ExecuteSkillMessage(null, userId, userId, null);
    }

    public ExecuteSkillMessage executeFogMaster(UUID userId, ExecuteSkillReqDto params){
        return new ExecuteSkillMessage(null, userId, userId, null);
    }

    public ExecuteSkillMessage executeFreezingWand(UUID userId, ExecuteSkillReqDto params){
        return new ExecuteSkillMessage(null, userId, userId, null);
    }

    public ExecuteSkillMessage executeParalyzer(UUID userId, ExecuteSkillReqDto params){
        return new ExecuteSkillMessage(null, userId, userId, null);
    }
    

}
