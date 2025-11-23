package com.carpool.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequestDto {
    @NotBlank @Size(min = 3, max = 30)
    private String username;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8, max = 64)
    private String password;

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
