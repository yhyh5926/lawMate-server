/**
 * 파일위치: src/main/java/com/edu/springboot/domain/member/MemberController.java
 * 기능전체: 회원 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
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
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostConstruct
    public void init() {
        System.out.println("✅ [domain/member] 회원 컨트롤러가 활성화되었습니다.");
    }

    // 아이디 중복 확인
    @GetMapping("/check-id.do")
    public ResponseEntity<?> checkId(@RequestParam String loginId) {
        boolean available = memberService.isLoginIdAvailable(loginId);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 회원가입 처리
    @PostMapping("/join/form.do")
    public ResponseEntity<?> join(@RequestBody JoinDto joinDto) {
        boolean success = memberService.join(joinDto);
        return ResponseEntity.ok(Map.of("success", success));
    }

    // 로그인 처리
    @PostMapping("/login.do")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        Map<String, Object> result = memberService.login(loginDto);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(401).body(Map.of("message", "로그인 정보가 올바르지 않습니다."));
    }
}