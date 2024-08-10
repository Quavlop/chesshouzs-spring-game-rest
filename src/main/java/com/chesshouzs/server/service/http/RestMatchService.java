package com.chesshouzs.server.service.http;

import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.chesshouzs.server.constants.GameConstants;
import com.chesshouzs.server.constants.RedisConstants;
import com.chesshouzs.server.dto.GameActiveDto;
import com.chesshouzs.server.dto.custom.match.PlayerSkillDataCountDto;
import com.chesshouzs.server.model.GameActive;
import com.chesshouzs.server.model.GameSkill;
import com.chesshouzs.server.model.redis.GameMove;
import com.chesshouzs.server.repository.GameActiveRepository;
import com.chesshouzs.server.repository.GameSkillRepository;
import com.chesshouzs.server.repository.RedisBaseRepository;
import com.chesshouzs.server.constants.RedisConstants;

@Service
public class RestMatchService {
    
    @Autowired
    private GameActiveRepository gameActiveRepository;    

    @Autowired 
    private GameSkillRepository gameSkillRepository;

    @Autowired 
    private RedisBaseRepository redis;

    public GameActiveDto GetMatchData(UUID userId){
        GameActive matchData = gameActiveRepository.findPlayerActiveMatch(userId);
        if (matchData == null){
            return null;
        }

        GameActiveDto result = matchData.convertToDto(matchData);

        String key = RedisConstants.getGameMoveKey(matchData.getMovesCacheRef());
        Map<String, String> notation = redis.hgetall(key);
        if (notation == null){
            return null;
        }

        result.setGameNotation(notation.get("move"));
        String turn = notation.get("turn");
        if (turn == "1"){
            result.setTurn(GameConstants.WHITE_COLOR);
        } else {
            result.setTurn(GameConstants.BLACK_COLOR);
        }

        return result;
    }

    public List<PlayerSkillDataCountDto> GetMatchSkillData(UUID userId){
        List<PlayerSkillDataCountDto> result = new ArrayList<PlayerSkillDataCountDto>();

        List<GameSkill> data = gameSkillRepository.findAll();
        if (data == null){
            return null;
        }

        // TODO : get redis key for player data count and initialize the data on handle matchmaking
        
        for (GameSkill skill : data){
            result.add(skill.convertToDto(skill));
        }

        return result;
    }
}
