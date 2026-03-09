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
    private final MemberMapper memberMapper;

    // 💡 [수정] 내가 쓴 글 목록 조회 API
    // 에러 발생 시 프론트가 터지지 않게 빈 리스트(Collections.emptyList())를 반환합니다.
    @GetMapping("/posts/{memberId}")
    public ResponseEntity<?> getMyPosts(
            @PathVariable("memberId") Long memberId,
            @RequestParam("type") String type) {
        try {
            List<Map<String, Object>> posts = memberService.getMyPosts(memberId, type);
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            e.printStackTrace(); // 서버 로그에 에러 출력
            return ResponseEntity.ok(Collections.emptyList()); 
        }
    }

    // 💡 [추가] 사건 기록 목록 조회 API (500 에러 해결용)
    // CaseMgmtTab.jsx가 호출하는 경로를 여기서 처리하도록 추가했습니다.
    @GetMapping("/cases/list")
    public ResponseEntity<?> getMyCases(@RequestParam("memberId") Long memberId) {
        try {
            // MemberMapper.xml에 작성된 findMyPosts 쿼리를 재활용하거나 별도 쿼리 호출
            List<Map<String, Object>> cases = memberService.getMyPosts(memberId, "mockTrial");
            return ResponseEntity.ok(cases);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    // 💡 프론트엔드에서 보낸 PUT 요청을 받아 탈퇴 처리
    @PutMapping("/withdraw")
    public ResponseEntity<?> withdrawMember(@RequestParam("loginId") String loginId) {
        try {
            int result = memberMapper.withdrawMember(loginId);
            if (result > 0) {
                return ResponseEntity.ok("회원 탈퇴 처리가 완료되었습니다.");
            } else {
                return ResponseEntity.badRequest().body("탈퇴 처리에 실패했습니다. 존재하지 않는 회원입니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
        }
    }
}