package com.example.auth.dto;

import com.example.auth.domain.Role;
import com.example.auth.domain.User;

public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private Role role;

    public static UserResponseDto of(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.role = user.getRole();
        return dto;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
}
