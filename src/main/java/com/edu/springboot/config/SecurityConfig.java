/**
 * 파일위치: src/main/java/com/edu/springboot/config/SecurityConfig.java
 * 기능전체: Spring Security 설정을 통해 JWT 인증 필터를 등록하고 페이지별 접근 권한(ADMIN/USER 등)을 제어합니다.
 */
package com.edu.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.edu.springboot.common.jwt.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 기존 print문 유지 및 보안 설정
        System.out.println("✅ [config] Security 및 JWT 필터 체인 설정이 완료되었습니다.");
        
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configure(http)) 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 은혁 파트: 공개 경로 허용
                .requestMatchers("/", "/error", "/api/**", "/uploads/**","/member/login.do", "/member/join/**", 
                               "/member/lawyer/**", "/member/find.do", "/main.do").permitAll()
                // 은혁 파트: 관리자 권한 설정
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}