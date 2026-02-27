/**
 * 파일위치: src/main/java/com/edu/springboot/domain/member/MemberServiceImpl.java
 * 기능전체: MemberService 인터페이스를 실제로 구현하는 클래스입니다.
 * 💡 중요 수정: @Primary 어노테이션을 추가하여, 가짜 빈이 생성되더라도 무조건 이 클래스를 최우선으로 사용하게 강제합니다!
 */
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
@Primary // ✅ 핵심 해결책: 가짜 매퍼 빈과의 충돌을 완벽히 무시하고, 이 클래스를 무조건 1순위 일꾼으로 지정합니다.
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        // 서버 기동 시 작동 확인 콘솔 로그
        System.out.println("✅ [domain/member] 회원 서비스(MemberServiceImpl)가 1순위(@Primary)로 정상 가동되었습니다.");
    }

    @Override
    public boolean isLoginIdAvailable(String loginId) {
        return memberMapper.findByLoginId(loginId) == null;
    }

    @Override
    @Transactional
    public boolean join(JoinDto joinDto) {
        String encodedPassword = passwordEncoder.encode(joinDto.getPassword());
        
        MemberVO member = MemberVO.builder()
                .loginId(joinDto.getLoginId())
                .password(encodedPassword)
                .memberType(joinDto.getMemberType())
                .name(joinDto.getName())
                .phone(joinDto.getPhone())
                .email(joinDto.getEmail())
                .status("ACTIVE")
                .build();
        
        return memberMapper.insertMember(member) > 0;
    }

    @Override
    public Map<String, Object> login(LoginDto loginDto) {
        MemberVO member = memberMapper.findByLoginId(loginDto.getLoginId());
        
        if (member != null && passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            String token = jwtUtil.generateToken(member.getLoginId(), member.getMemberType());
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("member", member);
            return result;
        }
        return null;
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