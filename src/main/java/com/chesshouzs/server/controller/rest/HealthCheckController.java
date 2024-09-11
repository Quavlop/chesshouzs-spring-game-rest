package com.chesshouzs.server.controller.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.chesshouzs.server.util.response.Response;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<Response> HealthCheck(){
        return new ResponseEntity<>(new Response(HttpServletResponse.SC_OK, "Service is healthy!.", true), HttpStatus.OK);
    }
}
