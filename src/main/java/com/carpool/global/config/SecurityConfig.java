package com.carpool.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 끄기 (개발용)
                .csrf(AbstractHttpConfigurer::disable)
                // 모든 요청 허용
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );
        // 로그인폼 / httpBasic 이런 거도 전부 기본값으로 둬도 됨
        // .formLogin(AbstractHttpConfigurer::disable)
        // .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
