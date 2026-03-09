package com.edu.springboot.domain.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mypage")
public class MypageController {

    private final MemberMapper memberMapper;

    public MypageController(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
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