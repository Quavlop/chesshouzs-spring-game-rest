package com.chesshouzs.server.dto.response;

public class ExecuteSkillResDto {
    private String message;


    public ExecuteSkillResDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
