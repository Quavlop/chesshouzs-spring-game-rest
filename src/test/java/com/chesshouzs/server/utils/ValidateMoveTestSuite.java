package com.chesshouzs.server.utils;

public class ValidateMoveTestSuite extends TestSuite {

    private String oldStateArg; 
    private String newStateArg;
    private Boolean expectation;


    public ValidateMoveTestSuite(String name, String oldStateArg, String newStateArg, Boolean expectation) {
        super(name);
        this.oldStateArg = oldStateArg;
        this.newStateArg = newStateArg;
        this.expectation = expectation;
    }

    public String getOldStateArg() {
        return this.oldStateArg;
    }

    public void setOldStateArg(String oldStateArg) {
        this.oldStateArg = oldStateArg;
    }

    public String getNewStateArg() {
        return this.newStateArg;
    }

    public void setNewStateArg(String newStateArg) {
        this.newStateArg = newStateArg;
    }

    public Boolean isExpectation() {
        return this.expectation;
    }

    public Boolean getExpectation() {
        return this.expectation;
    }

    public void setExpectation(Boolean expectation) {
        this.expectation = expectation;
    }

}
