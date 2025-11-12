package com.example.auth.service;

import com.example.auth.domain.Role;
import com.example.auth.domain.User;
import com.example.auth.dto.*;
import com.example.auth.repository.UserRepository;
import com.example.auth.config.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public UserResponseDto signup(SignupRequestDto req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        String hashed = passwordEncoder.encode(req.getPassword());
        User user = new User(req.getUsername(), req.getEmail(), hashed, Role.USER);
        userRepository.save(user);
        return UserResponseDto.of(user);
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole().name());
        return new LoginResponseDto(token, UserResponseDto.of(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDto me(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return UserResponseDto.of(user);
    }
}
