package com.edu.springboot.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 💡 [중요] MemberVO가 위치한 정확한 패키지 경로를 임포트해야 에러가 사라집니다.
import com.edu.springboot.domain.member.vo.MemberVO;

import java.util.List;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

	private final MemberService memberService;
<<<<<<< HEAD
	private final MemberMapper memberMapper;
=======
>>>>>>> branch 'main' of https://github.com/yhyh5926/lawMate-server.git

<<<<<<< HEAD
	// 1. 내가 쓴 글 목록 조회 API
	@GetMapping("/posts/{memberId}")
	public ResponseEntity<?> getMyPosts(@PathVariable("memberId") Long memberId, @RequestParam("type") String type) {
		try {
			List<Map<String, Object>> posts = memberService.getMyPosts(memberId, type);
=======
	/**
	 * 1. 내가 쓴 글 목록 조회 API type: 'question', 'community', 'mockTrial'
	 */
	@GetMapping("/posts/{memberId}")
	public ResponseEntity<?> getMyPosts(@PathVariable("memberId") Long memberId, @RequestParam("type") String type) {
		try {
			// 💡 [수정] MemberService의 메서드명 findMyPosts와 일치시킴
			List<Map<String, Object>> posts = memberService.findMyPosts(memberId, type);
>>>>>>> branch 'main' of https://github.com/yhyh5926/lawMate-server.git
			return ResponseEntity.ok(posts);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

<<<<<<< HEAD
	// 2. 사건 기록 목록 조회 API
	@GetMapping("/cases/list")
	public ResponseEntity<?> getMyCases(@RequestParam("memberId") Long memberId) {
		try {
			List<Map<String, Object>> cases = memberService.getMyPosts(memberId, "mockTrial");
			return ResponseEntity.ok(cases);
		} catch (Exception e) {
=======
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
>>>>>>> branch 'main' of https://github.com/yhyh5926/lawMate-server.git
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

<<<<<<< HEAD
	// 3. 회원 탈퇴 처리
	@PutMapping("/withdraw")
	public ResponseEntity<?> withdrawMember(@RequestParam("loginId") String loginId) {
		try {
			// 서비스에 이미 구현된 withdraw 로직이 있다면 memberService.withdraw(loginId) 추천
			int result = memberMapper.withdrawMember(loginId);
			if (result > 0) {
				return ResponseEntity.ok("회원 탈퇴 처리가 완료되었습니다.");
			} else {
				return ResponseEntity.badRequest().body("탈퇴 처리에 실패했습니다.");
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
		}
	}

	// 💡 [여기서 추가] 정보 수정(Edit) API
	// 프론트의 EditInfoTab.jsx에서 보낸 submitData를 MemberVO로 매핑하여 받습니다.
	@PutMapping("/edit")
	public ResponseEntity<?> editProfile(@RequestBody MemberVO memberVO) {

		try {

			// ServiceImpl에 작성한 updateProfile 로직 호출
			boolean isUpdated = memberService.updateProfile(memberVO);

			if (isUpdated) {
				return ResponseEntity.ok(Map.of("success", true, "message", "회원 정보가 성공적으로 수정되었습니다."));
			} else {
				return ResponseEntity.badRequest().body(Map.of("success", false, "message", "수정된 내용이 없거나 실패했습니다."));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "서버 오류: " + e.getMessage()));
=======
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
>>>>>>> branch 'main' of https://github.com/yhyh5926/lawMate-server.git
		}
	}
}