package com.chesshouzs.server.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chesshouzs.server.dto.GameActiveDto;
import com.chesshouzs.server.model.Users;
import com.chesshouzs.server.service.http.RestMatchService;
import com.chesshouzs.server.util.exceptions.http.DataNotFoundExceptionHandler;
import com.chesshouzs.server.util.response.Response;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("v1/match")
public class MatchController {
    
    @Autowired
    private RestMatchService restMatchService;

    @GetMapping
    public ResponseEntity<Response> GetMatchData(@AuthenticationPrincipal Users user){
        GameActiveDto res = restMatchService.GetMatchData(user.getId()); 
        if (res == null){
            throw new DataNotFoundExceptionHandler("Data not found"); 
        }
        return new ResponseEntity<>(new Response(HttpServletResponse.SC_OK, "Successfully retrieved game type data.", res), HttpStatus.OK);
    }
}
