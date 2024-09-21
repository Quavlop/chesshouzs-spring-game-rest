package com.chesshouzs.server.utils;

import java.util.ArrayList;
import com.chesshouzs.server.service.lib.interfaces.Character;
import com.datastax.oss.driver.shaded.guava.common.collect.Table;


public class InCheckTestSuite extends TestSuite{
    
    private String state;
    private String playerColor;
    private Boolean expectedIsInCheck;
    private ArrayList<Character> expectedAttackers;
    private Table<Integer, Integer, Boolean> expectedValidKingMoves;


    public InCheckTestSuite(String name, String state, String playerColor, Boolean expectedIsInCheck, ArrayList<Character> expectedAttackers, Table<Integer,Integer,Boolean> expectedValidKingMoves) {
        super(name);
        this.state = state;
        this.playerColor = playerColor;
        this.expectedIsInCheck = expectedIsInCheck;
        this.expectedAttackers = expectedAttackers;
        this.expectedValidKingMoves = expectedValidKingMoves;
    }


    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPlayerColor() {
        return this.playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }    

    public Boolean isExpectedIsInCheck() {
        return this.expectedIsInCheck;
    }

    public Boolean getExpectedIsInCheck() {
        return this.expectedIsInCheck;
    }

    public void setExpectedIsInCheck(Boolean expectedIsInCheck) {
        this.expectedIsInCheck = expectedIsInCheck;
    }

    public ArrayList<Character> getExpectedAttackers() {
        return this.expectedAttackers;
    }

    public void setExpectedAttackers(ArrayList<Character> expectedAttackers) {
        this.expectedAttackers = expectedAttackers;
    }

    public Table<Integer,Integer,Boolean> getExpectedValidKingMoves() {
        return this.expectedValidKingMoves;
    }

    public void setExpectedValidKingMoves(Table<Integer,Integer,Boolean> expectedValidKingMoves) {
        this.expectedValidKingMoves = expectedValidKingMoves;
    }

}
