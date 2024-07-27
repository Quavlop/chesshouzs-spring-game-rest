package com.chesshouzs.server.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import com.chesshouzs.server.service.http.RestGameService;
import com.chesshouzs.server.dto.GameTypesDto;
import com.chesshouzs.server.model.Users;
import com.chesshouzs.server.util.exceptions.http.DataNotFoundExceptionHandler;
import com.chesshouzs.server.util.response.Response;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/v1/game")
public class GameController {

    @Autowired
    private RestGameService restGameService;

    @GetMapping 
    public ResponseEntity<Response> GetGameTypes(@AuthenticationPrincipal Users user){
        List<GameTypesDto> res =  restGameService.GetGameTypes();
        if (res.isEmpty()){
            throw new DataNotFoundExceptionHandler("Data not found");
        }
        return new ResponseEntity<>(new Response(HttpServletResponse.SC_OK, "Successfully retrieved game type data.", res), HttpStatus.OK);
    }
}   
