package com.chesshouzs.server.utils;

public abstract class TestSuite {
    private String name;
    

    public TestSuite(String name) {
        this.name = name;
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
