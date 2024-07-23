package com.chesshouzs.server.util.exceptions.http;

public class DataNotFoundExceptionHandler extends RuntimeException{
    public DataNotFoundExceptionHandler(String message){
        super(message);
    }
}

