package com.carpool.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

// ✨ Lombok 적용: @RequiredArgsConstructor 추가
// (수동 생성자 삭제)

@Service
@Transactional // DB 작업이므로 트랜잭션 적용
@RequiredArgsConstructor // final이 붙은 필드(userRepository, passwordEncoder)의 생성자를 자동 주입
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * * @param request (LoginRequestDto 사용)
     * @return 생성된 User 객체
     * @throws RuntimeException 이미 존재하는 아이디일 경우
     */
    public User signup(LoginRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다."); 
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User(request.getUsername(), encodedPassword);
        return userRepository.save(newUser);
    }

    /**
     * 로그인
     * * @param request (LoginRequestDto 사용)
     * @return 로그인 성공 시 User 객체
     * @throws RuntimeException 아이디 또는 비밀번호가 틀릴 경우
     */
    @Transactional(readOnly = true) // 읽기 전용
    public User login(LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 틀립니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("아이디 또는 비밀번호가 틀립니다.");
        }
        
        return user;
    }
}
