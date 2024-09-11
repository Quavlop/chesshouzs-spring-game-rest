package com.chesshouzs.server.controller.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chesshouzs.server.config.queue.KafkaMessageProducer;
import com.chesshouzs.server.constants.KafkaConstants;
import com.chesshouzs.server.dto.GameActiveDto;
import com.chesshouzs.server.dto.custom.match.EndGameDto;
import com.chesshouzs.server.dto.custom.match.PlayerSkillDataCountDto;
import com.chesshouzs.server.dto.request.ExecuteSkillReqDto;
import com.chesshouzs.server.dto.response.ExecuteSkillResDto;
import com.chesshouzs.server.model.Users;
import com.chesshouzs.server.model.cassandra.tables.PlayerGameState;
import com.chesshouzs.server.service.http.RestMatchService;
import com.chesshouzs.server.util.exceptions.http.DataNotFoundExceptionHandler;
import com.chesshouzs.server.util.response.Response;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("v1/match")
public class MatchController {
    
    @Autowired
    private RestMatchService restMatchService;


    @GetMapping("/player/check")
    public ResponseEntity<Response> CheckInGamePlayerStatus(@AuthenticationPrincipal Users user){
        String res =  restMatchService.IsPlayerInActiveGame(user.getId());
        return new ResponseEntity<>(new Response(HttpServletResponse.SC_OK, "Successfully check player active game status.", res), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Response> GetMatchData(@AuthenticationPrincipal Users user){
        GameActiveDto res = restMatchService.GetMatchData(user.getId()); 
        if (res == null){
            throw new DataNotFoundExceptionHandler("Data not found"); 
        }
        return new ResponseEntity<>(new Response(HttpServletResponse.SC_OK, "Successfully retrieved game type data.", res), HttpStatus.OK);
    } 

    @GetMapping("/skills")
    public ResponseEntity<Response> GetPlayerMatchSkillStats(@AuthenticationPrincipal Users user){
        List<PlayerSkillDataCountDto> res = restMatchService.GetMatchSkillData(user.getId()); 
        if (res == null){
            throw new DataNotFoundExceptionHandler("Data not found");    
        }
        return new ResponseEntity<>(new Response(HttpServletResponse.SC_OK, "Successfully retrieved player match skill stats.", res), HttpStatus.OK);
    }

    @PostMapping("/skills/execute/{id}")
    public ResponseEntity<Response> ExecuteSkill(@AuthenticationPrincipal Users user, @PathVariable("id") String id, @RequestBody ExecuteSkillReqDto params) throws Exception{
        params.setSkillId(UUID.fromString(id));
        ExecuteSkillResDto res = restMatchService.ExecuteSkill(user.getId(), params);
        if (res == null) {
            throw new Exception("Failed to execute skill"); 
        }
        return new ResponseEntity<>(new Response(HttpServletResponse.SC_OK, "Successfully execute skill.", res), HttpStatus.OK);
    }

    @GetMapping("/player/status")
    public ResponseEntity<Response> GetPlayerStatus(@AuthenticationPrincipal Users user, @RequestParam(name = "isOpponent", required = true) Integer isOpponent) throws Exception {
        PlayerGameState res;
        if (isOpponent == 1){
            res = restMatchService.GetOpponentStatus(user.getId()); 
        } else {
            res = restMatchService.GetPlayerStatus(user.getId()); 
        }

        if (res == null) {
            throw new Exception("Failed to get player skill status");
        }
        return new ResponseEntity<>(new Response(HttpServletResponse.SC_OK, "Successfully retrieved game type data.", res), HttpStatus.OK);
    }

    @PostMapping("/end/{game_id}")
    public ResponseEntity<Response> EndMatch(@AuthenticationPrincipal Users user, @PathVariable("game_id") UUID gameId, @RequestBody EndGameDto params) throws Exception{
        params.setGameId(gameId);
        boolean res = restMatchService.EndGame(user.getId(), params);
        if (!res){
            throw new Exception("Failed to fulfill end game request");
        }
        return new ResponseEntity<>(new Response(HttpServletResponse.SC_OK, "End game request fulfilled successfully.", null), HttpStatus.OK);
    }
}
