// src/main/java/com/edu/springboot/domain/member/MypageController.java
package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;
import java.util.Map;

// 마이페이지 기능 관련 컨트롤러
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

	private final MemberService memberService;

//	자유게시판이나 의견조사 등에 내가 쓴 글 가져오기
	@GetMapping("/posts/{memberId}")
	public ResponseEntity<?> getMyPosts(@PathVariable("memberId") Long memberId, @RequestParam("type") String type) {
		try {
			List<Map<String, Object>> posts = memberService.findMyPosts(memberId, type);
			return ResponseEntity.ok(posts);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

//	내 모의판결 목록 가져오기
	@GetMapping("/cases/list")
	public ResponseEntity<?> getMyCases(@RequestParam("memberId") Long memberId) {
		try {
			List<Map<String, Object>> cases = memberService.findMyPosts(memberId, "mockTrial");
			return ResponseEntity.ok(cases);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

//	내 정보 수정 적용
	@PutMapping("/edit")
	public ResponseEntity<?> editProfile(@RequestBody MemberVO memberVO) {
		try {
			boolean success = memberService.updateProfile(memberVO);

			if (success) {
				return ResponseEntity.ok(Map.of("success", true, "message", "수정 성공"));
			} else {
				return ResponseEntity.badRequest().body(Map.of("success", false, "message", "수정 실패"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "서버 에러"));
		}
	}

//	마이페이지에서 탈퇴 처리 진행
	@PutMapping("/withdraw")
	public ResponseEntity<?> withdrawMember(@RequestParam("loginId") String loginId) {
		try {
			boolean success = memberService.withdraw(loginId);

			if (success) {
				return ResponseEntity.ok(Map.of("success", true, "message", "탈퇴 성공"));
			} else {
				return ResponseEntity.badRequest().body(Map.of("success", false, "message", "탈퇴 실패"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "서버 에러"));
		}
	}
}