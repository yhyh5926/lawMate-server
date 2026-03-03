// src/main/java/com/edu/springboot/domain/member/MemberController.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
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

    @GetMapping("/check-id.do")
    public ResponseEntity<?> checkId(@RequestParam("loginId") String loginId) {
        return ResponseEntity.ok(Map.of("available", memberService.isLoginIdAvailable(loginId)));
    }

    @PostMapping("/join/form.do")
    public ResponseEntity<?> join(@RequestBody JoinDto joinDto) {
        if (memberService.join(joinDto)) {
            return ResponseEntity.ok(Map.of("success", true, "message", "회원가입 성공"));
        }
        return ResponseEntity.status(500).body(Map.of("success", false, "message", "가입 중 오류 발생"));
    }

    @PostMapping("/login.do")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        Map<String, Object> result = memberService.login(loginDto);
        if (result != null) return ResponseEntity.ok(result);
        return ResponseEntity.status(401).body(Map.of("message", "아이디 또는 비밀번호가 틀립니다."));
    }

    // 💡 수정 사항: 소셜 로그인 시 미가입 회원이면 404 에러 반환
    @PostMapping("/social-login.do")
    public ResponseEntity<?> socialLogin(@RequestBody Map<String, String> socialData) {
        Map<String, Object> result = memberService.socialLogin(socialData);
        if (result != null) return ResponseEntity.ok(result);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "가입된 정보가 없습니다. 회원가입을 먼저 진행해주세요."));
    }
}