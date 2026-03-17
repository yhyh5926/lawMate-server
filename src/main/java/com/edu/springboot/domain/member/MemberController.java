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
import java.util.List;

// 회원 관련 요청을 처리하는 컨트롤러
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

//	컨트롤러가 잘 켜졌는지 확인하는 용도
	@PostConstruct
	public void init() {
		System.out.println("[MemberController] /api/member API 경로가 준비됨");
	}

//	아이디 중복 확인 요청 처리
	@GetMapping("/check-id")
	public ResponseEntity<?> checkId(@RequestParam("loginId") String loginId) {
		return ResponseEntity.ok(Map.of("available", memberService.isLoginIdAvailable(loginId)));
	}

//	일반 회원가입 요청 처리
	@PostMapping("/join/form")
	public ResponseEntity<?> join(@ModelAttribute JoinDto joinDto) {
		try {
			if (memberService.join(joinDto)) {
				return ResponseEntity.ok(Map.of("success", true, "message", "회원가입 성공"));
			}
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "가입 중 오류 발생"));
		} catch (IllegalStateException e) {
//			탈퇴 후 30일이 안 지났을 때 나는 에러 처리
			return ResponseEntity.status(400).body(Map.of("success", false, "message", e.getMessage()));
		}
	}

//	일반 로그인 요청 처리
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
		try {
			Map<String, Object> result = memberService.login(loginDto);
			if (result != null)
				return ResponseEntity.ok(result);
			return ResponseEntity.status(401).body(Map.of("message", "아이디 또는 비밀번호가 틀림"));
		} catch (RuntimeException e) {
//			탈퇴한 계정이거나 승인 안 된 변호사일 때 에러 처리
			return ResponseEntity.status(401).body(Map.of("success", false, "message", e.getMessage()));
		}
	}

//	구글 로그인으로 들어왔을 때 이미 가입된 유저인지 확인
	@PostMapping("/social-login")
	public ResponseEntity<?> socialLogin(@RequestBody Map<String, String> socialData) {
		try {
			Map<String, Object> result = memberService.socialLogin(socialData);
			if (result != null)
				return ResponseEntity.ok(result);
//			가입 안 된 계정이면 프론트에서 가입 창으로 보내도록 401 에러 전송
			return ResponseEntity.status(401).body(Map.of("success", false, "message", "가입되지 않은 소셜 계정임"));
		} 
		catch (RuntimeException e) {
			return ResponseEntity.status(403).body(Map.of("success", false, "message", e.getMessage()));
		}
	}

//	구글 계정으로 새로 회원가입할 때 처리
	@PostMapping("/join/social")
	public ResponseEntity<?> socialJoin(@ModelAttribute JoinDto joinDto) {
		try {
			if (memberService.join(joinDto)) {
				return ResponseEntity.ok(Map.of("success", true, "message", "소셜 회원가입 완료"));
			}
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "소셜 가입 중 오류 발생"));
		} catch (IllegalStateException e) {
			return ResponseEntity.status(400).body(Map.of("success", false, "message", e.getMessage()));
		}
	}

//	아이디나 비밀번호 찾기 기능 요청 처리
	@PostMapping("/find")
	public ResponseEntity<?> findIdPw(@RequestBody FindDto findDto) {
		return ResponseEntity.ok(Map.of("success", true, "message", "찾기 기능은 준비중임"));
	}

//	회원이 직접 탈퇴할 때 처리
	@PutMapping("/{memberId}/withdraw")
	public ResponseEntity<?> withdrawMember(@PathVariable("memberId") Long memberId) {
		if (memberService.withdrawMember(memberId)) {
			return ResponseEntity.ok(Map.of("success", true, "message", "회원 탈퇴가 완료됨"));
		}
		return ResponseEntity.status(500).body(Map.of("success", false, "message", "탈퇴 처리 중 오류가 발생함"));
	}

//	내가 작성한 글 목록 불러오기
	@GetMapping("/posts")
	public ResponseEntity<?> getMyPosts(@RequestParam("memberId") Long memberId, @RequestParam("type") String type) {
		List<Map<String, Object>> posts = memberService.findMyPosts(memberId, type);
		return ResponseEntity.ok(posts);
	}
}