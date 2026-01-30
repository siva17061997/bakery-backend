package com.example.demo.dto;

public class JwtResponse {

    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private UserDto user;

    /* ========== GETTERS & SETTERS ========== */

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
 
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getType() {
        return type;
    }
 
    public void setType(String type) {
        this.type = type;
    }

    public UserDto getUser() {
        return user;
    }
 
    public void setUser(UserDto user) {
        this.user = user;
    }
}
