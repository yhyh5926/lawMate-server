// src/main/java/com/edu/springboot/domain/member/MemberController.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.FindDto;
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

	private final MemberService memberService;

	@PostConstruct
	public void init() {
		System.out.println("✅ [MemberController] /api/member API 경로가 준비되었습니다.");
	}

	@GetMapping("/check-id.do")
	public ResponseEntity<?> checkId(@RequestParam("loginId") String loginId) {
		return ResponseEntity.ok(Map.of("available", memberService.isLoginIdAvailable(loginId)));
	}

	@PostMapping("/join/form.do")
	public ResponseEntity<?> join(@ModelAttribute JoinDto joinDto) {
		if (memberService.join(joinDto)) {
			return ResponseEntity.ok(Map.of("success", true, "message", "회원가입 성공"));
		}
		return ResponseEntity.status(500).body(Map.of("success", false, "message", "가입 중 오류 발생"));
	}

	@PostMapping("/login.do")
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
		Map<String, Object> result = memberService.login(loginDto);
		if (result != null)
			return ResponseEntity.ok(result);
		return ResponseEntity.status(401).body(Map.of("message", "아이디 또는 비밀번호가 틀립니다."));
	}

	// 💡 [오류 해결] 구글 로그인 (이미 가입된 회원 확인용)
	@PostMapping("/social-login.do")
	public ResponseEntity<?> socialLogin(@RequestBody Map<String, String> socialData) {
		Map<String, Object> result = memberService.socialLogin(socialData);
		if (result != null)
			return ResponseEntity.ok(result);
		// 가입 정보가 없으면 401 에러를 보내어 프론트에서 가입 페이지로 이동시키게 함
		return ResponseEntity.status(401).body(Map.of("success", false, "message", "가입되지 않은 소셜 계정입니다."));
	}

	// 💡 [오류 해결] 구글 회원가입 (신규 가입 및 파일 업로드용)
	@PostMapping("/join/social.do")
	public ResponseEntity<?> socialJoin(@ModelAttribute JoinDto joinDto) {
		if (memberService.join(joinDto)) {
			return ResponseEntity.ok(Map.of("success", true, "message", "소셜 회원가입 완료"));
		}
		return ResponseEntity.status(500).body(Map.of("success", false, "message", "소셜 가입 중 오류 발생"));
	}

	@PostMapping("/find.do")
	public ResponseEntity<?> findIdPw(@RequestBody FindDto findDto) {
		return ResponseEntity.ok(Map.of("success", true, "message", "찾기 기능은 준비중입니다."));
	}
}