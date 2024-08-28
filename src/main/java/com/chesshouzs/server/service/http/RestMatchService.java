package com.chesshouzs.server.service.http;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.chesshouzs.server.config.queue.KafkaMessageProducer;
import com.chesshouzs.server.config.queue.KafkaMessageWrapper;
import com.chesshouzs.server.constants.GameConstants;
import com.chesshouzs.server.constants.KafkaConstants;
import com.chesshouzs.server.constants.RedisConstants;
import com.chesshouzs.server.dto.GameActiveDto;
import com.chesshouzs.server.dto.custom.match.PlayerSkillDataCountDto;
import com.chesshouzs.server.dto.kafka.ExecuteSkillMessage;
import com.chesshouzs.server.dto.request.ExecuteSkillReqDto;
import com.chesshouzs.server.dto.response.ExecuteSkillResDto;
import com.chesshouzs.server.model.GameActive;
import com.chesshouzs.server.model.GameSkill;
import com.chesshouzs.server.model.cassandra.keys.PlayerGameStatePrimaryKeys;
import com.chesshouzs.server.model.cassandra.tables.PlayerGameState;
import com.chesshouzs.server.model.redis.GameMove;
import com.chesshouzs.server.repository.GameActiveRepository;
import com.chesshouzs.server.repository.GameSkillRepository;
import com.chesshouzs.server.repository.RedisBaseRepository;
import com.chesshouzs.server.repository.cassandra.PlayerGameStatesRepository;
import com.chesshouzs.server.util.exceptions.http.DataNotFoundExceptionHandler;
import com.chesshouzs.server.constants.SkillConstants;


@Service
public class RestMatchService {
    
    @Autowired
    private GameActiveRepository gameActiveRepository;    

    @Autowired 
    private GameSkillRepository gameSkillRepository;

    @Autowired 
    private RedisBaseRepository redis;

    @Autowired
    private SkillService skillService;

    @Autowired 
    private PlayerGameStatesRepository playerGameStateCassandraRepository;

    @Autowired 
    private KafkaMessageProducer messageProducer;

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
        if (turn.equals("1")){
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
        String key = RedisConstants.getPlayerMatchSkillStatsKey(userId.toString()); 
        Map <String, String> skillStats = redis.hgetall(key);
        if (skillStats == null){
            return null;
        }

        for (GameSkill skill : data){
            PlayerSkillDataCountDto skillDto = skill.convertToDto(skill);
            String skillRedisData = skillStats.get(skill.getId().toString());
            if (skillRedisData == null){
                continue;
            }
            Integer remainingCount = Integer.parseInt(skillRedisData);
            skillDto.setCurrentUsageCount(remainingCount);
            result.add(skillDto);
        }

        return result;
    }

    public ExecuteSkillResDto ExecuteSkill(UUID userId, ExecuteSkillReqDto params) throws Exception {
    
        Optional<GameSkill> data = gameSkillRepository.findById(params.getSkillId());
        if (!data.isPresent()){
            return null;
        }

        GameSkill skill = data.get();

        // produce message to be published 
        // check which skill is being executed
        ExecuteSkillMessage action = new ExecuteSkillMessage(); 
        if (SkillConstants.SKILL_ENLIGHTENED_APPRENTICE.equals(skill.getName())) {
            action = skillService.executeEnlightenedApprentice(userId, params, skill);
        } else if (SkillConstants.SKILL_THE_GREAT_WALL.equals(skill.getName())) {
            action = skillService.executeTheGreatWall(userId, params, skill);
        } else if (SkillConstants.SKILL_FOG_MASTER.equals(skill.getName())) {
            action = skillService.executeFogMaster(userId, params, skill);
        } else if (SkillConstants.SKILL_FREEZING_WAND.equals(skill.getName())) {
            action = skillService.executeFreezingWand(userId, params, skill);
        } else if (SkillConstants.SKILL_PARALYZER.equals(skill.getName())) {
            action = skillService.executeParalyzer(userId, params, skill);
        } else {
            throw new DataNotFoundExceptionHandler("Skill data not found");
        }

        if (action == null){
            throw new Exception("Failed to execute skill");
        }

        // publish message to kafka        
        messageProducer.publish(KafkaConstants.TOPIC_EXECUTE_SKILL, params.getGameId().toString(), action);

        return new ExecuteSkillResDto("success");
    }

    public PlayerGameState GetPlayerStatus(UUID userId) throws Exception {
        GameActive matchData = gameActiveRepository.findPlayerActiveMatch(userId);
        if (matchData == null){
            return null;
        }

        PlayerGameStatePrimaryKeys keys = new PlayerGameStatePrimaryKeys(userId.toString(), matchData.getId().toString());
        Optional<PlayerGameState> res = playerGameStateCassandraRepository.findById(keys);
        if (res == null){
            return null;
        }
        
        return res.get();
    }

    public PlayerGameState GetOpponentStatus(UUID userId) throws Exception {
        GameActive matchData = gameActiveRepository.findPlayerActiveMatch(userId);
        if (matchData == null){
            return null;
        }

        UUID opponentId;
        if (userId.equals(matchData.getWhitePlayer().getId())){
            opponentId = matchData.getBlackPlayer().getId();
        } else {
            opponentId = matchData.getWhitePlayer().getId();
        }

        PlayerGameStatePrimaryKeys keys = new PlayerGameStatePrimaryKeys(opponentId.toString(), matchData.getId().toString());
        Optional<PlayerGameState> res = playerGameStateCassandraRepository.findById(keys);
        if (res == null){
            return null;
        }
        
        return res.get();
    }
}
