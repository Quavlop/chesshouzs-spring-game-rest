package com.chesshouzs.server.dto;

import java.util.UUID;
import java.util.List;

public class GameTypesDto { 

    public UUID id;
    public String name; 
    public List<GameTypeVariantDto> variants;

    public GameTypesDto(UUID id, String name, List<GameTypeVariantDto> variants) {
        this.id = id;
        this.name = name;
        this.variants = variants;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GameTypeVariantDto> getVariants() {
        return this.variants;
    }

    public void setVariants(List<GameTypeVariantDto> variants) {
        this.variants = variants;
    }

}

