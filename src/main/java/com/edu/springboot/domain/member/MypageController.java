// src/main/java/com/edu/springboot/domain/member/MypageController.java
package com.edu.springboot.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

	private final MemberService memberService;

	/**
	 * 1. 내가 쓴 글 목록 조회 API type: 'question', 'community', 'mockTrial'
	 */
	@GetMapping("/posts/{memberId}")
	public ResponseEntity<?> getMyPosts(@PathVariable("memberId") Long memberId, @RequestParam("type") String type) {
		try {
			// 💡 [수정] MemberService의 메서드명 findMyPosts와 일치시킴
			List<Map<String, Object>> posts = memberService.findMyPosts(memberId, type);
			return ResponseEntity.ok(posts);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	/**
	 * 2. 사건 기록(모의판결/의견조사) 목록 조회 API CaseMgmtTab.jsx 등에서 호출 시 'mockTrial' 타입을 기본으로
	 * 처리합니다.
	 */
	@GetMapping("/cases/list")
	public ResponseEntity<?> getMyCases(@RequestParam("memberId") Long memberId) {
		try {
			// 💡 [수정] MemberService의 메서드명 findMyPosts와 일치시킴
			List<Map<String, Object>> cases = memberService.findMyPosts(memberId, "mockTrial");
			return ResponseEntity.ok(cases);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	/**
	 * 3. 회원 탈퇴 처리 API 서비스 계층(memberService)을 거쳐 트랜잭션을 보장합니다.
	 */
	@PutMapping("/withdraw")
	public ResponseEntity<?> withdrawMember(@RequestParam("loginId") String loginId) {
		try {
			// memberMapper를 직접 쓰지 않고 서비스의 기능을 호출하는 것이 안전합니다.
			boolean success = memberService.withdraw(loginId);
			if (success) {
				return ResponseEntity.ok(Map.of("success", true, "message", "회원 탈퇴 처리가 완료되었습니다."));
			} else {
				return ResponseEntity.badRequest().body(Map.of("success", false, "message", "탈퇴 처리에 실패했습니다."));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "서버 오류가 발생했습니다."));
		}
	}
}