package com.carpool.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// 이것은 인터페이스(interface)입니다! (클래스가 아님)
public interface UserRepository extends JpaRepository<User, Long> {
    
    // [핵심!]
    // "SELECT * FROM users WHERE username = ?" 쿼리를 자동으로 생성해주는
    // Spring Data JPA의 "쿼리 메소드" 선언입니다.
    //
    // 이 한 줄이 없으면, UserService에서 이 메소드를 찾을 수 없습니다.
    Optional<User> findByUsername(String username);
}