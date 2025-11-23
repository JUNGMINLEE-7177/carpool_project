package com.carpool.user.dto;

public class LoginResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserResponseDto user;

    public LoginResponseDto(String accessToken, UserResponseDto user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
    public UserResponseDto getUser() { return user; }
}
