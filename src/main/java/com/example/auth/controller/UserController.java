package com.example.auth.controller;

import com.example.auth.dto.*;
import com.example.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody SignupRequestDto req) {
        return ResponseEntity.ok(userService.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto req) {
        return ResponseEntity.ok(userService.login(req));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(Authentication auth) {
        String username = (String) auth.getPrincipal();
        return ResponseEntity.ok(userService.me(username));
    }
}
