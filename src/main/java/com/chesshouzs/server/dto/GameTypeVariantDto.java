package com.chesshouzs.server.dto;

import java.util.UUID;

public class GameTypeVariantDto {


    private UUID id;

    // belongs to GameType entity
    private String name; 

    private int duration;
    private int increment;

    public GameTypeVariantDto(UUID id, String name, int duration, int increment) {
        this.name = name;
        this.id = id;
        this.duration = duration;
        this.increment = increment;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getIncrement() {
        return this.increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

}