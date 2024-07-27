package com.chesshouzs.server.model;

import java.util.UUID;
import java.util.List;
import java.util.Collection;
import java.time.LocalDateTime;

import com.chesshouzs.server.dto.UserDto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Users implements ModelInterface<Users, UserDto>, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private UUID id;

    private String username; 
    private String email; 
    private String profilePicture; 
    private boolean isPremium; 
    private int eloPoints; 
    private String password; 
    private String googleId; 
    private LocalDateTime emailVerifiedAt; 
    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt; 

    public Users(){}

    public Users(UUID id, String username, String email, String profilePicture, boolean isPremium, int eloPoints, String password, String googleId, LocalDateTime emailVerifiedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.isPremium = isPremium;
        this.eloPoints = eloPoints;
        this.password = password;
        this.googleId = googleId;
        this.emailVerifiedAt = emailVerifiedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
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

    public boolean isIsPremium() {
        return this.isPremium;
    }

    public boolean getIsPremium() {
        return this.isPremium;
    }

    public void setIsPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }

    public int getEloPoints() {
        return this.eloPoints;
    }

    public void setEloPoints(int eloPoints) {
        this.eloPoints = eloPoints;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoogleId() {
        return this.googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public LocalDateTime getEmailVerifiedAt() {
        return this.emailVerifiedAt;
    }

    public void setEmailVerifiedAt(LocalDateTime emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public UserDto convertToDto(Users data){
        return new UserDto();
    }

}
