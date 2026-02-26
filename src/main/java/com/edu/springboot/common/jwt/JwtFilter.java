/**
 * 파일위치: src/main/java/com/edu/springboot/common/jwt/JwtFilter.java
 * 기능전체: 모든 API 요청에서 헤더의 JWT 토큰을 검사하여 유효한 경우 인증 세션을 부여하는 필터입니다.
 */
package com.edu.springboot.common.jwt;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        System.out.println("✅ [common/jwt] JWT 인증 필터가 준비되었습니다.");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // 인증 제외 경로
        if (path.startsWith("/api/") || path.contains("/member/login") || 
            path.contains("/member/join") || path.equals("/main.do") || path.contains("/member/find")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtUtil.validateToken(token)) {
                    String loginId = jwtUtil.extractClaims(token).getSubject();
                    String role = jwtUtil.extractClaims(token).get("role", String.class);
                    
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            loginId, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }
        
        filterChain.doFilter(request, response);
    }
}