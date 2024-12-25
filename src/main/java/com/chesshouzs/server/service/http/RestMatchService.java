package com.chesshouzs.server.service.http;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Map;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.chesshouzs.server.config.queue.KafkaMessageProducer;
import com.chesshouzs.server.config.queue.KafkaMessageWrapper;
import com.chesshouzs.server.constants.GameConstants;
import com.chesshouzs.server.constants.KafkaConstants;
import com.chesshouzs.server.constants.RedisConstants;
import com.chesshouzs.server.dto.GameActiveDto;
import com.chesshouzs.server.dto.custom.match.EndGameDto;
import com.chesshouzs.server.dto.custom.match.PlayerSkillDataCountDto;
import com.chesshouzs.server.dto.kafka.EndGameMessage;
import com.chesshouzs.server.dto.kafka.ExecuteSkillMessage;
import com.chesshouzs.server.dto.request.ExecuteSkillReqDto;
import com.chesshouzs.server.dto.response.ExecuteSkillResDto;
import com.chesshouzs.server.model.GameActive;
import com.chesshouzs.server.model.GameSkill;
import com.chesshouzs.server.model.Users;
import com.chesshouzs.server.model.cassandra.keys.PlayerGameStatePrimaryKeys;
import com.chesshouzs.server.model.cassandra.tables.PlayerGameState;
import com.chesshouzs.server.model.redis.GameMove;
import com.chesshouzs.server.repository.GameActiveRepository;
import com.chesshouzs.server.repository.GameSkillRepository;
import com.chesshouzs.server.repository.RedisBaseRepository;
import com.chesshouzs.server.repository.UsersRepository;
import com.chesshouzs.server.repository.cassandra.PlayerGameStatesRepository;
import com.chesshouzs.server.service.rpc.module.RpcGameModule;
import com.chesshouzs.server.util.GameHelper;
import com.chesshouzs.server.util.exceptions.http.DataNotFoundExceptionHandler;
import com.chesshouzs.server.constants.SkillConstants;
import com.chesshouzs.server.util.utils.game.*;


@Service
public class RestMatchService {
    
    @Autowired
    private GameActiveRepository gameActiveRepository;    

    @Autowired 
    private GameSkillRepository gameSkillRepository;

    @Autowired 
    private UsersRepository usersRepository;

    @Autowired 
    private RedisBaseRepository redis;

    @Autowired
    private SkillService skillService;

    @Autowired 
    private PlayerGameStatesRepository playerGameStateCassandraRepository;

    @Autowired 
    private KafkaMessageProducer messageProducer;

    @Autowired 
    private RpcGameModule rpcGameModule;

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

        result.getWhitePlayer().setDuration(Integer.parseInt(notation.get("white_total_duration")));
        result.getBlackPlayer().setDuration(Integer.parseInt(notation.get("black_total_duration")));

        result.setLastMovement(OffsetDateTime.parse(notation.get("last_movement")));

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

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean EndGame(UUID userId, EndGameDto params) throws Exception {

        // if (!userId.equals(params.getWinnerId())){
        //     throw new Exception("Invalid actor");
        // }
        GameActive matchData = gameActiveRepository.findPlayerActiveMatch(userId);
        if (matchData == null){
            throw new Exception("Match data not found");
        }

        String key = RedisConstants.getGameMoveKey(matchData.getMovesCacheRef());
        Map<String, String> notation = redis.hgetall(key);
        if (notation == null){
            throw new Exception("Match state not found");
        }


        ZoneId jakartaZone = ZoneId.of("Asia/Jakarta");

        if (params.getType().equals(GameConstants.END_GAME_TIMEOUT_TYPE)){
            String lastMovement = notation.get("last_movement");
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(lastMovement);            

            LocalTime latestTimestamp = offsetDateTime.toLocalTime();
            LocalTime currentTime = LocalTime.now(jakartaZone);

            Duration duration = Duration.between(latestTimestamp, currentTime);
            long secondsDifference = duration.getSeconds();

            if (Integer.parseInt(notation.get("turn")) == 1 && params.getWinnerId().equals(matchData.getBlackPlayer().getId())){
                Integer currentCumulativeDuration = Integer.parseInt(notation.get("white_total_duration"));
                System.out.println(currentCumulativeDuration);
                System.out.println(secondsDifference);
                System.out.println(matchData.getGameTypeVariant().getDuration());
                if (currentCumulativeDuration + secondsDifference < matchData.getGameTypeVariant().getDuration()){
                    throw new Exception("Invalid action");
                }

            } else if (Integer.parseInt(notation.get("turn")) == 0 && params.getWinnerId().equals(matchData.getWhitePlayer().getId())) {
                Integer currentCumulativeDuration = Integer.parseInt(notation.get("black_total_duration"));
                System.out.println(currentCumulativeDuration);
                System.out.println(secondsDifference);
                System.out.println(matchData.getGameTypeVariant().getDuration());                
                if (currentCumulativeDuration + secondsDifference < matchData.getGameTypeVariant().getDuration()){
                    throw new Exception("Invalid action");
                }

            } else {
                throw new Exception("Invalid turn");
            }

       


        } else if (params.getType().equals(GameConstants.END_GAME_CHECKMATE_TYPE) || params.getType().equals(GameConstants.END_GAME_STALEMATE_TYPE) || params.getType().equals(GameConstants.END_GAME_DRAW_TYPE)) {
            String oldState = notation.get("move");
            char [][] oldStateToArr = GameHelper.convertNotationToArray(oldState);
            if (matchData.getBlackPlayer().getId().equals(userId)){
                oldStateToArr = GameHelper.transformBoard(oldStateToArr);
            }
            oldState = GameHelper.convertArrayToNotation(oldStateToArr);
    
            String newState = params.getState();
            char [][] newStateToArr = GameHelper.convertNotationToArray(newState);
            if (matchData.getBlackPlayer().getId().equals(userId)){
                newStateToArr = GameHelper.transformBoard(oldStateToArr);
            }
            newState = GameHelper.convertArrayToNotation(newStateToArr);
    
    
            Boolean validMove = rpcGameModule.validateMovement(oldState, newState);
            if (!validMove){
                throw new Exception("Invalid move");
            }
        }

        Users winner, loser; 
        if (params.getWinnerId().equals(matchData.getWhitePlayer().getId())){
            winner = matchData.getWhitePlayer();
            loser = matchData.getBlackPlayer();
        } else {
            winner = matchData.getBlackPlayer();
            loser = matchData.getWhitePlayer();
        }
        

        // change game_active status is_done to true 
        matchData.setIsDone(true);
        matchData.setEndTime(LocalDateTime.now(jakartaZone));
        gameActiveRepository.save(matchData);

        // elo calculation & accumulation & save to database
        // TODO : this one and similar errors should be 422
        Elo eloCalculatorInstance = GetCalculationType(params.getType()); 
        if (eloCalculatorInstance == null){
            throw new Exception("Invalid end game type reasoning");
        }


        double newWinnerElo = eloCalculatorInstance.CalculatePostMatchElo(winner.getEloPoints(), loser.getEloPoints(), true);
        double newLoserElo = eloCalculatorInstance.CalculatePostMatchElo(loser.getEloPoints(), winner.getEloPoints(), false);

        winner.setEloPoints((int)newWinnerElo);
        usersRepository.save(winner);

        loser.setEloPoints((int)newLoserElo);
        usersRepository.save(loser);
        
        // delete player_match_skill redis key on enemy and winner
        // delete game_move for the respective match
        List<String> redisKeys = Arrays.asList(
            GameHelper.getRedisGameMoveKey(matchData.getMovesCacheRef()), 
            GameHelper.getRedisPlayerMatchSkillKey(winner.getId()), 
            GameHelper.getRedisPlayerMatchSkillKey(loser.getId())
        );
        try {
            Long res = redis.del(redisKeys);
            if (res == null){
                throw new Exception("Failed to delete redis records");
            }
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }

        // delete player_game_states movement data on cassandra
        PlayerGameStatePrimaryKeys winnerSkillStateKey = new PlayerGameStatePrimaryKeys(winner.getId().toString(), params.getGameId().toString());
        PlayerGameStatePrimaryKeys loserSkillStateKey = new PlayerGameStatePrimaryKeys(loser.getId().toString(), params.getGameId().toString());

        try {
            playerGameStateCassandraRepository.deleteById(winnerSkillStateKey);
            playerGameStateCassandraRepository.deleteById(loserSkillStateKey);

            if (playerGameStateCassandraRepository.existsById(winnerSkillStateKey) || playerGameStateCassandraRepository.existsById(loserSkillStateKey)){
                throw new Exception("Failed to delete records");
            }

        } catch (Exception e){
            throw new Exception(e.getMessage());
        }

        // publish event to websocket server
        EndGameMessage message = new EndGameMessage(winner.getId(), loser.getId(), newWinnerElo, newLoserElo, params.getType());
        messageProducer.publish(KafkaConstants.TOPIC_END_GAME, params.getGameId().toString(), message);

        return true; 
    }

    public Elo GetCalculationType(String type){
        if (type.equals(GameConstants.END_GAME_CHECKMATE_TYPE)){
            return new CheckmateElo();
        } 

        if (type.equals(GameConstants.END_GAME_RESIGN_TYPE)){
            return new ResignElo();
        }

        if (type.equals(GameConstants.END_GAME_STALEMATE_TYPE)){
            return new StalemateElo();
        }

        if (type.equals(GameConstants.END_GAME_DRAW_TYPE)){
            return new DrawElo();
        }

        if (type.equals(GameConstants.END_GAME_TIMEOUT_TYPE)){
            return new TimeoutElo();
        }

        return null;
    }

    public String IsPlayerInActiveGame(UUID userId){
        GameActive matchData = gameActiveRepository.findPlayerActiveMatch(userId);
        if (matchData == null){
            return null;
        }
        return matchData.getId().toString();
    }
}
