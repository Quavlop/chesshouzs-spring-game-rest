package com.chesshouzs.server.dto.custom.match;

public class PositionDto {
    
    private Integer row;
    private Integer col;

    public PositionDto(Integer row, Integer col) {
        this.row = row;
        this.col = col;
    }

    public Integer getRow() {
        return this.row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getCol() {
        return this.col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

}
