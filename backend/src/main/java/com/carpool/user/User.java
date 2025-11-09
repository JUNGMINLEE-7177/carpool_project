package com.carpool.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// ✨ Lombok 적용: @Getter, @Setter, @NoArgsConstructor 추가
// (수동 Getter/Setter/생성자 모두 삭제)

@Getter
@Setter
@NoArgsConstructor // JPA는 @Entity 클래스에 대해 기본 생성자를 요구합니다.
@Entity
@Table(name = "users") // 'user'는 DB 예약어일 수 있으므로 'users' 사용
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호 저장

    // 회원가입 시 사용할 생성자 (수동으로 남겨둠)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
