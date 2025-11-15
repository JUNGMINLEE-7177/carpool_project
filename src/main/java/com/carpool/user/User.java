package com.carpool.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(name = "ux_users_username", columnList = "username", unique = true),
        @Index(name = "ux_users_email", columnList = "email", unique = true)
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=30)
    private String username;

    @Column(nullable=false, unique=true, length=190)
    private String email;

    @Column(name="password_hash", nullable=false, length=60)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role = Role.USER;

    @Column(nullable=false)
    private boolean enabled = true;

    @Column(name = "created_at", nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 회원가입/로그인에 필요한 생성자
    public User(String username, String email, String passwordHash, Role role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}
