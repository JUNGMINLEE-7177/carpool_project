package com.example.auth.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "ux_users_username", columnList = "username", unique = true),
        @Index(name = "ux_users_email", columnList = "email", unique = true)
})
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=30)
    private String username;

    @Column(nullable=false, unique=true, length=190)
    private String email;

    @Column(nullable=false, length=60)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(nullable=false)
    private boolean enabled = true;

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}

    public User(String username, String email, String passwordHash, Role role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public boolean isEnabled() { return enabled; }
}
