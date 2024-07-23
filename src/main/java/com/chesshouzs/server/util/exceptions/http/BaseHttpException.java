package com.chesshouzs.server.util.exceptions.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.chesshouzs.server.util.response.Response;

@ControllerAdvice
public class BaseHttpException {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response> handleException(Exception ex, WebRequest request){
        Response response = new Response(500, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataNotFoundExceptionHandler.class)
    public final ResponseEntity<Response> handleNotFoundException(DataNotFoundExceptionHandler ex, WebRequest request) {
        Response response = new Response(404, ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}

