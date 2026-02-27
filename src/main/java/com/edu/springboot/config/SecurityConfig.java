/**
 * 파일 위치: src/main/java/com/edu/springboot/config/SecurityConfig.java
 * 수정 이유: 회원가입 시 발생하는 403 Forbidden 및 로그인 401 에러 해결을 위해 권한 설정을 최적화했습니다.
 */
package com.edu.springboot.config;

import com.edu.springboot.common.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (JWT 사용 시 필수)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
<<<<<<< HEAD
                // 💡 .do 확장자나 경로 패턴에 상관없이 /api/member/로 시작하는 모든 인증 관련 경로는 허용합니다.
                .requestMatchers("/api/member/login.do", "/api/member/check-id.do", "/api/member/join/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
=======
                // 은혁 파트: 공개 경로 허용
                .requestMatchers("/", "/error", "/api/**", "/uploads/**","/member/login.do", "/member/join/**", 
                               "/member/lawyer/**", "/member/find.do", "/main.do").permitAll()
                // 은혁 파트: 관리자 권한 설정
                .requestMatchers("/admin/**").hasRole("ADMIN")
>>>>>>> branch 'main' of https://github.com/yhyh5926/lawMate-server.git
                .anyRequest().authenticated()
            )
            // 💡 JWT 필터를 필터 체인에 추가
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}