package com.chesshouzs.server.model.cassandra.types;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import java.util.List;

public class State {

    @Column("duration_left")
    private Integer durationLeft;

    @Column("list")
    private List<SkillStatus> list;

    public State() {}

    public State(Integer durationLeft, List<SkillStatus> list) {
        this.durationLeft = durationLeft;
        this.list = list;
    }

    public Integer getDurationLeft() {
        return durationLeft;
    }

    public void setDurationLeft(Integer durationLeft) {
        this.durationLeft = durationLeft;
    }

    public List<SkillStatus> getList() {
        return list;
    }

    public void setList(List<SkillStatus> list) {
        this.list = list;
    }
}