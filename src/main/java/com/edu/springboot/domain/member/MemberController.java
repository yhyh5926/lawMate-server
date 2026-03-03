// src/main/java/com/edu/springboot/domain/member/MemberController.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostConstruct
    public void init() {
        System.out.println("✅ [MemberController] /api/member API 경로가 준비되었습니다.");
    }

    /**
     * 아이디 중복 체크 API
     * Spring Boot 3.2+ 버전 호환을 위해 @RequestParam("loginId") 명시 완료
     */
    @GetMapping("/check-id.do")
    public ResponseEntity<?> checkId(@RequestParam("loginId") String loginId) {
        return ResponseEntity.ok(Map.of("available", memberService.isLoginIdAvailable(loginId)));
    }

    // 일반 회원가입
    @PostMapping("/join/form.do")
    public ResponseEntity<?> join(@RequestBody JoinDto joinDto) {
        if (memberService.join(joinDto)) {
            return ResponseEntity.ok(Map.of("success", true, "message", "회원가입 성공"));
        }
        return ResponseEntity.status(500).body(Map.of("success", false, "message", "가입 중 오류 발생"));
    }

    // 일반 로그인
    @PostMapping("/login.do")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        Map<String, Object> result = memberService.login(loginDto);
        if (result != null) return ResponseEntity.ok(result);
        return ResponseEntity.status(401).body(Map.of("message", "아이디 또는 비밀번호가 틀립니다."));
    }

    /**
     * 소셜 로그인(구글 등) 요청 API
     * 💡 수정 사항: 소셜 로그인 시 미가입 회원이면 404(NOT_FOUND) 에러 반환
     */
    @PostMapping("/social-login.do")
    public ResponseEntity<?> socialLogin(@RequestBody Map<String, String> socialData) {
        Map<String, Object> result = memberService.socialLogin(socialData);
        if (result != null) return ResponseEntity.ok(result);
        
        // 가입된 정보가 없을 경우 프론트엔드에서 회원가입 페이지로 유도할 수 있도록 404 반환
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Map.of("message", "가입된 정보가 없습니다. 회원가입을 먼저 진행해주세요."));
    }
}