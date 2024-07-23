package com.chesshouzs.server.model;

import java.sql.Date;
import java.util.UUID;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import com.chesshouzs.server.dto.GameTypeVariantDto;
import com.chesshouzs.server.dto.GameTypesDto;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
public class GameType implements ModelInterface<GameType, GameTypesDto>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private UUID id;

    @OneToMany(mappedBy = "gameType", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JsonManagedReference
    private List<GameTypeVariant> gameTypeVariants;

    private String name;
    private Date createdAt;
    private Date updatedAt;


    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<GameTypeVariant> getGameTypeVariants() {
        return this.gameTypeVariants;
    }

    public void setGameTypeVariants(List<GameTypeVariant> gameTypeVariants) {
        this.gameTypeVariants = gameTypeVariants;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // dto methods    
    public GameTypesDto convertToDto(GameType data){
        List<GameTypeVariantDto> variantDto = new ArrayList<GameTypeVariantDto>();
        for (GameTypeVariant variant : data.gameTypeVariants){
            variantDto.add(variant.convertToDto(variant));
        }
        return new GameTypesDto(data.id, data.name, variantDto);
    }


}
