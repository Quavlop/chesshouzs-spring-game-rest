package com.chesshouzs.server.model.cassandra.types;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;

@PrimaryKeyClass
public class SkillStatus {

    @Column("position")
    private SkillPosition position;

    @Column("duration_left")
    private Integer durationLeft;

    // Constructors
    public SkillStatus() {}

    public SkillStatus(SkillPosition position, Integer durationLeft) {
        this.position = position;
        this.durationLeft = durationLeft;
    }

    // Getters and Setters
    public SkillPosition getPosition() {
        return position;
    }

    public void setPosition(SkillPosition position) {
        this.position = position;
    }

    public Integer getDurationLeft() {
        return durationLeft;
    }

    public void setDurationLeft(Integer durationLeft) {
        this.durationLeft = durationLeft;
    }
}
