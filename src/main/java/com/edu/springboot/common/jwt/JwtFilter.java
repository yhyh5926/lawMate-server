package com.edu.springboot.common.jwt;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 1. 현재 요청 경로 확인
        String path = request.getRequestURI();
        
        // 💡 [최종 해결책] 판례 API(/api/) 경로는 토큰 검사 없이 즉시 통과
        // 이 로직이 있어야 /api/precedents/6 같은 상세 페이지도 403이 안 뜹니다.
        if (path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return; // 👈 중요: 이후 토큰 검증 로직을 타지 않도록 종료!
        }
        
        // 2. 로그인/회원가입 등 공개 경로 추가 예외 처리 (필요시)
        if (path.contains("/member/login") || path.contains("/member/join") || path.equals("/main.do")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 기존 토큰 검증 로직 (로그인이 필요한 서비스용)
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
                // 토큰이 잘못된 경우 context를 비워 보안 유지
                SecurityContextHolder.clearContext();
            }
        }
        
        filterChain.doFilter(request, response);
    }
}