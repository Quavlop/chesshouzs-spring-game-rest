package com.chesshouzs.server.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Response {
    private int code; 
    private String message; 

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object error;

    public Response(int code, String message, Object data){
        this.code = code; 
        this.message = message; 
        this.data = data;
    }

    public Response(int code, String message, Object data, Object error){
        this.code = code; 
        this.message = message; 
        this.data = data;
        this.error = error;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getError(){
        return this.error;
    }

    public void setError(Object error){
        this.error = error;
    }
}
