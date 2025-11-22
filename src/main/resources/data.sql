-- 초기 사용자 계정 (테스트용)

-- ⚠ password_hash 컬럼에 지금은 편의상 '1234' 평문을 넣었음.
--    SecurityConfig 에서 BCryptPasswordEncoder 를 쓰고 있어서
--    실제로는 BCrypt 해시를 넣거나, NoOpPasswordEncoder 로 바꾸지 않으면
--    로그인 검증이 실패할 수 있다.



INSERT INTO users (username, email, password_hash, role, enabled, created_at)
VALUES
    ('user_a', 'user_a@example.com', '1234', 'USER', TRUE, NOW()),
    ('user_b', 'user_b@example.com', '1234', 'USER', TRUE, NOW());
