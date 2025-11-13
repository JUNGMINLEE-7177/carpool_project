package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
