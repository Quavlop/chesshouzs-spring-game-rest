package com.chesshouzs.server.utils;

import java.util.Map;

public class DoubleMovementScanResultTestSuite extends TestSuite {
    
    private String oldStateArg; 
    private String newStateArg; 
    private  Map<String, Object> expectation;


    public DoubleMovementScanResultTestSuite(String name, String oldStateArg, String newStateArg, Map<String,Object> expectation) {
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

    public Map<String,Object> getExpectation() {
        return this.expectation;
    }

    public void setExpectation(Map<String,Object> expectation) {
        this.expectation = expectation;
    }



}
