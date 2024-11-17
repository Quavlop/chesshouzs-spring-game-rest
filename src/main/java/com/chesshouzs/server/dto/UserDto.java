package com.chesshouzs.server.dto;

import java.util.UUID;

public class UserDto {

    private String id;
    private String username;
    private String email; 
    private String profilePicture; 
    private int eloPoints;  

    private int duration;


    public UserDto(String id, String username, String email, String profilePicture, int eloPoints) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.eloPoints = eloPoints;
    }

    public UserDto(String id, String username, String email, String profilePicture, int eloPoints, int duration) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.eloPoints = eloPoints;
        this.duration = duration;
    }

    public String getId(){
        return this.id;
    }

    public void setID(String id){
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getEloPoints() {
        return this.eloPoints;
    }

    public void setEloPoints(int eloPoints) {
        this.eloPoints = eloPoints;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
