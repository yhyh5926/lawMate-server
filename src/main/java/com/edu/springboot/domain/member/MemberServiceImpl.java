package com.edu.springboot.domain.member;

import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import com.edu.springboot.domain.member.vo.MemberVO;
import com.edu.springboot.common.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(LoginDto dto) {
        MemberVO vo = memberMapper.findByLoginId(dto.getLoginId());
        
        if (vo == null) {
            System.out.println("❌ [로그인 실패] 존재하지 않는 아이디: " + dto.getLoginId());
            return null;
        }

        // 💡 입력값(1234)과 DB 해시값($2a$10$8.Un...)을 비교합니다.
        boolean isMatched = passwordEncoder.matches(dto.getPassword(), vo.getPassword());
        
        if (isMatched) {
            System.out.println("✅ [로그인 성공] 사용자: " + vo.getLoginId());
            String token = jwtUtil.generateToken(vo.getLoginId(), vo.getMemberType());
            
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("member", vo);
            return result;
        }
        
        System.out.println("❌ [로그인 실패] 비밀번호 불일치 (입력된 PW: " + dto.getPassword() + ")");
        return null;
    }

    // 나머지 메서드들은 기존과 동일 (생략 없이 전체 사용하세요)
    @Override public boolean isLoginIdAvailable(String loginId) { return memberMapper.findByLoginId(loginId) == null; }
    @Override @Transactional public boolean join(JoinDto dto) { 
        MemberVO vo = MemberVO.builder().loginId(dto.getLoginId()).password(passwordEncoder.encode(dto.getPassword()))
                .memberType(dto.getMemberType()).name(dto.getName()).phone(dto.getPhone()).email(dto.getEmail()).build();
        return memberMapper.insertMember(vo) > 0;
    }
    @Override public MemberVO getMemberInfo(String loginId) { return memberMapper.findByLoginId(loginId); }
    @Override public boolean updateProfile(MemberVO vo) { return memberMapper.updateMember(vo) > 0; }
    @Override public boolean withdraw(String loginId) { MemberVO vo = memberMapper.findByLoginId(loginId); return vo != null && memberMapper.deleteMember(vo.getMemberId()) > 0; }
    @Override public String findId(String name, String phone) { return memberMapper.findLoginIdByNameAndPhone(name, phone); }
    @Override public String sendAuthCode(String phone) { return "123456"; }
    @Override public List<MemberVO> getMembersByType(String memberType) { return memberMapper.findMembersByType(memberType); }
}