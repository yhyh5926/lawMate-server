package com.edu.springboot.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    // 실무에서는 application.yml에서 주입받아 사용합니다.
    private static final String SECRET_KEY = "LawMateSecretKeyForJwtTokenGenerationAlgorithmMakeItLonger";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24시간

    public String generateToken(String loginId, String role) {
        return Jwts.builder()
                .setSubject(loginId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 수정됨: 컨트롤러에서 토큰을 해석해 회원 번호(Long)를 꺼내기 위해 추가된 메서드
    public Long getMemberNo(String token) {
        try {
            Claims claims = extractClaims(token);
            // Subject에 저장된 값을 Long 타입으로 변환하여 반환
            return Long.valueOf(claims.getSubject());
        } catch (NumberFormatException e) {
            throw new RuntimeException("토큰의 Subject가 숫자가 아닙니다.", e);
        } catch (Exception e) {
            throw new RuntimeException("토큰에서 회원 번호를 추출할 수 없습니다.", e);
        }
    }
    // 원본: (해당 메서드가 아예 존재하지 않았음)
}