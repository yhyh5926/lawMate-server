package com.edu.springboot.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// MemberVO 패키지 경로를 프로젝트 구조에 맞게 확인해주세요.
import com.edu.springboot.domain.member.vo.MemberVO;

import java.util.List;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MemberService memberService;

    /**
     * 1. 내가 쓴 글 목록 조회 API 
     * type: 'question', 'community', 'mockTrial'
     */
    @GetMapping("/posts/{memberId}")
    public ResponseEntity<?> getMyPosts(@PathVariable("memberId") Long memberId, @RequestParam("type") String type) {
        try {
            // MemberService의 메서드명 확인 (findMyPosts 또는 getMyPosts 중 서비스에 정의된 것 사용)
            List<Map<String, Object>> posts = memberService.findMyPosts(memberId, type);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    /**
     * 2. 사건 기록(모의판결) 목록 조회 API
     */
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

    /**
     * 3. 회원 정보 수정 API
     */
    @PutMapping("/edit")
    public ResponseEntity<?> editProfile(@RequestBody MemberVO memberVO) {
        try {
            boolean isUpdated = memberService.updateProfile(memberVO);
            if (isUpdated) {
                return ResponseEntity.ok(Map.of("success", true, "message", "회원 정보가 성공적으로 수정되었습니다."));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "수정된 내용이 없거나 실패했습니다."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "서버 오류: " + e.getMessage()));
        }
    }

    /**
     * 4. 회원 탈퇴 처리 API
     */
    @PutMapping("/withdraw")
    public ResponseEntity<?> withdrawMember(@RequestParam("loginId") String loginId) {
        try {
            boolean success = memberService.withdraw(success(loginId)); 
            // 만약 Service에 withdraw가 없다면 memberService.withdrawMember(loginId) 확인
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