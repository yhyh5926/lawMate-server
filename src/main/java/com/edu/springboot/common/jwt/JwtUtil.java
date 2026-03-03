/**
 * 파일위치: src/main/java/com/edu/springboot/common/jwt/JwtUtil.java
 * 기능전체: JWT 토큰의 생성, 클레임(정보) 추출 및 유효성 검증 기능을 제공하는 유틸리티입니다.
 */
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
    private static final String SECRET_KEY = "LawMateSecretKeyForJwtTokenGenerationAlgorithmMakeItLonger";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    public String generateToken(String loginId, String role, Long memberId) {
        return Jwts.builder()
                .setSubject(loginId)
                .claim("role", role)
                .claim("memberId", memberId)   // ← 추가(원석, 26.03.03)
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

    public Long getMemberNo(String token) {
        try {
            Claims claims = extractClaims(token);
            //return Long.valueOf(claims.getSubject());
            return claims.get("memberId", Long.class); //26.03.03 원석 추가
        } catch (Exception e) {
            return null;
        }
    }
}