/**
 * 파일 위치: src/main/java/com/edu/springboot/domain/member/MemberController.java
 * 수정 내용: @RequiredArgsConstructor를 통한 생성자 주입 방식으로 MemberService를 호출합니다.
 */
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService; // 인터페이스 타입을 주입받음

    @PostConstruct
    public void init() {
        System.out.println("✅ [MemberController] /api/member API 경로가 준비되었습니다.");
    }

    // 아이디 중복 체크 APIr
    @GetMapping("/check-id.do")
    public ResponseEntity<?> checkId(@RequestParam String loginId) {
        boolean available = memberService.isLoginIdAvailable(loginId);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 회원가입 요청 API
    @PostMapping("/join/form.do")
    public ResponseEntity<?> join(@RequestBody JoinDto joinDto) {
        boolean success = memberService.join(joinDto);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true, "message", "회원가입 성공"));
        }
        return ResponseEntity.status(500).body(Map.of("success", false, "message", "가입 도중 오류 발생"));
    }

    // 로그인 요청 API
    @PostMapping("/login.do")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        Map<String, Object> result = memberService.login(loginDto);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(401).body(Map.of("message", "아이디 또는 비밀번호가 틀립니다."));
    }
}