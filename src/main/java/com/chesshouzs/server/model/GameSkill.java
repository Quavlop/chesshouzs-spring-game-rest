package com.chesshouzs.server.model;

import java.io.ObjectInput;
import java.time.LocalDateTime;
import java.util.UUID;

import com.chesshouzs.server.dto.GameActiveDto;
import com.chesshouzs.server.dto.custom.match.PlayerSkillDataCountDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class GameSkill implements ModelInterface<GameSkill, PlayerSkillDataCountDto> {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String name; 
    private String description; 
    private Boolean forSelf; 
    private Boolean forEnemy; 
    private Integer radiusX; 
    private Integer radiusY; 
    private Boolean autoTrigger; 
    private Integer duration; 
    private Integer usageCount;
    private Integer rowLimit; 
    private Integer colLimit;
    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt;

    public GameSkill(){}


    public GameSkill(UUID id, String name, String description, Boolean forSelf, Boolean forEnemy, Integer radiusX, Integer radiusY, Boolean autoTrigger, Integer duration, Integer usageCount, Integer rowLimit, Integer colLimit, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.forSelf = forSelf;
        this.forEnemy = forEnemy;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.autoTrigger = autoTrigger;
        this.duration = duration;
        this.usageCount = usageCount;
        this.rowLimit = rowLimit; 
        this.colLimit = colLimit;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isForSelf() {
        return this.forSelf;
    }

    public Boolean getForSelf() {
        return this.forSelf;
    }

    public void setForSelf(Boolean forSelf) {
        this.forSelf = forSelf;
    }

    public Boolean isForEnemy() {
        return this.forEnemy;
    }

    public Boolean getForEnemy() {
        return this.forEnemy;
    }

    public void setForEnemy(Boolean forEnemy) {
        this.forEnemy = forEnemy;
    }

    public Integer getRadiusX() {
        return this.radiusX;
    }

    public void setRadiusX(Integer radiusX) {
        this.radiusX = radiusX;
    }

    public Integer getRadiusY() {
        return this.radiusY;
    }

    public void setRadiusY(Integer radiusY) {
        this.radiusY = radiusY;
    }

    public Boolean isAutoTrigger() {
        return this.autoTrigger;
    }

    public Boolean getAutoTrigger() {
        return this.autoTrigger;
    }

    public void setAutoTrigger(Boolean autoTrigger) {
        this.autoTrigger = autoTrigger;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getUsageCount() {
        return this.usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Integer getRowLimit() {
        return this.rowLimit;
    }

    public void setRowLimit(Integer rowLimit) {
        this.rowLimit = rowLimit;
    }

    public Integer getColLimit() {
        return this.colLimit;
    }

    public void setCowLimit(Integer colLimit) {
        this.colLimit = colLimit;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PlayerSkillDataCountDto convertToDto(GameSkill data){
        return new PlayerSkillDataCountDto(
            this.id, 
            this.name,
            this.description, 
            this.forSelf, 
            this.forEnemy, 
            this.radiusX, 
            this.radiusY, 
            this.autoTrigger, 
            this.duration, 
            this.usageCount, 
            this.rowLimit, 
            this.colLimit,
            null
        );
    }

}
