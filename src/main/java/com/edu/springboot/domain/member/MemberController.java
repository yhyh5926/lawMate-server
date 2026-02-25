package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import com.edu.springboot.domain.member.vo.MemberVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 개인 회원가입
    @PostMapping("/member/join/form.do")
    public ResponseEntity<?> join(@RequestBody JoinDto joinDto) {
        try {
            memberService.join(joinDto);
            return ResponseEntity.ok(Map.of("message", "가입이 완료되었습니다."));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 전문 회원가입
    @PostMapping("/member/lawyer/form.do")
    public ResponseEntity<?> joinLawyer(@RequestBody JoinDto joinDto) {
        try {
            memberService.joinLawyer(joinDto);
            return ResponseEntity.ok(Map.of("message", "관리자 승인 대기 중입니다."));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 로그인
    @PostMapping("/member/login.do")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            MemberVO member = memberService.login(loginDto);
            return ResponseEntity.ok(Map.of("message", "로그인 성공", "data", member));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 정보 수정
    @PostMapping("/mypage/edit.do")
    public ResponseEntity<?> editInfo(@RequestBody MemberVO memberVO) {
        try {
            memberService.modifyInfo(memberVO);
            return ResponseEntity.ok(Map.of("message", "정보가 수정되었습니다."));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // 회원 탈퇴
    @PostMapping("/mypage/withdraw.do")
    public ResponseEntity<?> withdraw(@RequestParam Long memberId) {
        try {
            memberService.withdraw(memberId);
            return ResponseEntity.ok(Map.of("message", "정보가 삭제됩니다 (탈퇴 처리 완료)."));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}