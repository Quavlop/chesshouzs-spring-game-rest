package com.chesshouzs.server.model.cassandra.types;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;

public class SkillPosition {

    @Column("row")
    private Integer row;

    @Column("col")
    private Integer col;

    // Constructors
    public SkillPosition() {}

    public SkillPosition(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }

    // Getters and Setters
    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }
}
