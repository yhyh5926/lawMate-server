// src/main/java/com/edu/springboot/domain/member/MemberController.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

<<<<<<< HEAD
    @PostConstruct
    public void init() {
        System.out.println("✅ [MemberController] /api/member API 경로가 준비되었습니다.");
    }

    // 아이디 중복 체크 APIr
=======
    // 💡 수정 완료: @RequestParam("loginId")를 명시하여 500 에러 해결
>>>>>>> branch 'main' of https://github.com/yhyh5926/lawMate-server.git
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

    // 소셜 로그인(구글) 요청 API
    @PostMapping("/social-login.do")
    public ResponseEntity<?> socialLogin(@RequestBody Map<String, String> socialData) {
        Map<String, Object> result = memberService.socialLogin(socialData);
        if (result != null) return ResponseEntity.ok(result);
        return ResponseEntity.status(500).body(Map.of("message", "소셜 로그인 처리 중 오류 발생"));
    }
}