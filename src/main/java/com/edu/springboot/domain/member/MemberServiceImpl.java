// src/main/java/com/edu/springboot/domain/member/MemberServiceImpl.java
// 설명: DB에 저장된 비밀번호 해시값과 무관하게, 테스트를 위해 비밀번호란에 '1234'를 입력하거나 DB값과 똑같이 입력하면 무조건 로그인을 성공시켜주는 백엔드 서비스 구현체입니다.

package com.edu.springboot.domain.member;

import com.edu.springboot.common.jwt.JwtUtil;
import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import com.edu.springboot.domain.member.vo.MemberVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
@Primary 
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        System.out.println("✅ [domain/member] 회원 서비스(MemberServiceImpl)가 로드되었습니다. (비밀번호 1234 프리패스 적용)");
    }

    // ====== [핵심 수정 부분: 로그인 검증 로직] ======
    @Override
    public Map<String, Object> login(LoginDto loginDto) {
        MemberVO member = memberMapper.findByLoginId(loginDto.getLoginId());
        
        if (member != null) {
            // 1. 정상적인 암호화 검증 (passwordEncoder)
            // 2. 개발 편의를 위한 프리패스 (1234 입력 시 무조건 통과)
            // 3. 만약 DB에 1234가 평문으로 들어있을 경우를 대비한 문자열 비교 통과
            boolean isPasswordMatch = passwordEncoder.matches(loginDto.getPassword(), member.getPassword())
                                   || "1234".equals(loginDto.getPassword())
                                   || loginDto.getPassword().equals(member.getPassword());
  
            if (isPasswordMatch) {    
                // 인증 성공 시 토큰
                String token = jwtUtil.generateToken(member.getLoginId(), member.getMemberType());
                
                Map<String, Object> result = new HashMap<>();
                result.put("token", token);
                result.put("member", member);
                return result;
            }
        }
        return null;
    }

    // ====== 아래는 기존 로직 그대로 유지 ======

    @Override
    public boolean isLoginIdAvailable(String loginId) {
        return memberMapper.findByLoginId(loginId) == null;
    }

    @Override
    @Transactional
    public boolean join(JoinDto joinDto) {
        MemberVO member = MemberVO.builder()
                .loginId(joinDto.getLoginId())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .name(joinDto.getName())
                .phone(joinDto.getPhone())
                .email(joinDto.getEmail())
                .memberType(joinDto.getMemberType())
                .build();
        return memberMapper.insertMember(member) > 0;
    }

    @Override
    public MemberVO getMemberInfo(String loginId) {
        return memberMapper.findByLoginId(loginId);
    }

    @Override
    @Transactional
    public boolean updateProfile(MemberVO memberVO) {
        return memberMapper.updateMember(memberVO) > 0;
    }

    @Override
    @Transactional
    public boolean withdraw(String loginId) {
        MemberVO member = memberMapper.findByLoginId(loginId);
        if (member != null) {
            return memberMapper.deleteMember(member.getMemberId()) > 0;
        }
        return false;
    }

    @Override
    public String findId(String name, String phone) {
        return memberMapper.findLoginIdByNameAndPhone(name, phone);
    }

    @Override
    public String sendAuthCode(String phone) {
        return "123456";
    }

    @Override
    public List<MemberVO> getMembersByType(String memberType) {
        return memberMapper.findMembersByType(memberType);
    }
}