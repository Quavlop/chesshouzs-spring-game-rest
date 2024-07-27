package com.chesshouzs.server.dto;

public class UserDto {

    private String username;
    private String email; 
    private String profilePicture; 
    private int eloPoints;  


    public UserDto(String username, String email, String profilePicture, int eloPoints) {
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.eloPoints = eloPoints;
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
}
