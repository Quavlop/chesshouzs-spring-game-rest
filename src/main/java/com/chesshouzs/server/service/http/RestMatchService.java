package com.chesshouzs.server.service.http;

import java.util.Optional;
import java.util.UUID;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.chesshouzs.server.constants.RedisConstants;
import com.chesshouzs.server.dto.GameActiveDto;
import com.chesshouzs.server.model.GameActive;
import com.chesshouzs.server.model.redis.GameMove;
import com.chesshouzs.server.repository.GameActiveRepository;
import com.chesshouzs.server.repository.RedisBaseRepository;
import com.chesshouzs.server.constants.RedisConstants;

@Service
public class RestMatchService {
    
    @Autowired
    private GameActiveRepository gameActiveRepository;    

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
        return result;
    }
}
