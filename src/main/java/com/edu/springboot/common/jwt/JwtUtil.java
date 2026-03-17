// src/main/java/com/edu/springboot/common/jwt/JwtUtil.java
package com.edu.springboot.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

// JWT 토큰의 생성과 정보 추출 및 유효성 검증을 담당하는 클래스
@Component
public class JwtUtil {
//	토큰 암호화에 사용할 비밀키 설정
	private static final String SECRET_KEY = "LawMateSecretKeyForJwtTokenGenerationAlgorithmMakeItLonger";

//	비밀키를 바이트 배열로 변환하여 암호화 키 객체 생성
	private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

//	토큰 만료 시간을 24시간으로 설정
	private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

//	사용자 정보를 바탕으로 새로운 JWT 토큰을 생성하는 메서드
	public String generateToken(String loginId, String role, Long memberId) {
		return Jwts.builder().setSubject(loginId).claim("role", role).claim("memberId", memberId)
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

//	토큰을 해독하여 그 안에 담긴 정보들을 추출하는 메서드
	public Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

//	전달받은 토큰이 변조되지 않았고 만료되지 않았는지 확인하는 메서드
	public boolean validateToken(String token) {
		try {
			extractClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

//	토큰 안에서 회원의 고유 식별 번호를 꺼내오는 메서드
	public Long getMemberNo(String token) {
		try {
			Claims claims = extractClaims(token);
			return claims.get("memberId", Long.class);
		} catch (Exception e) {
			return null;
		}
	}
}