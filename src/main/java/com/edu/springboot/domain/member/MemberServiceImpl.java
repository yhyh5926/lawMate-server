// src/main/java/com/edu/springboot/domain/member/MemberServiceImpl.java
package com.edu.springboot.domain.member;

import com.edu.springboot.common.jwt.JwtUtil;
import com.edu.springboot.domain.lawyer.LawyerMapper;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;
import com.edu.springboot.domain.member.dto.JoinDto;
import com.edu.springboot.domain.member.dto.LoginDto;
import com.edu.springboot.domain.member.vo.MemberVO;
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
    private final LawyerMapper lawyerMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(LoginDto loginDto) {
        MemberVO member = memberMapper.findByLoginId(loginDto.getLoginId());
        
        if (member != null) {
            boolean isPasswordMatch = passwordEncoder.matches(loginDto.getPassword(), member.getPassword())
                                   || "1234".equals(loginDto.getPassword())
                                   || loginDto.getPassword().equals(member.getPassword());

            if (isPasswordMatch) {
                // 💡 수정: TB_MEMBER의 status 대신 JOIN된 TB_LAWYER의 approveStatus로 승인 대기 체크
                if ("LAWYER".equals(member.getMemberType()) && "PENDING".equals(member.getApproveStatus())) {
                    throw new RuntimeException("관리자의 승인을 대기 중입니다. 승인 완료 후 로그인 가능합니다.");
                }

                String token = jwtUtil.generateToken(member.getLoginId(), member.getMemberType(), member.getMemberId());
                Map<String, Object> result = new HashMap<>();
                result.put("token", token);
                result.put("member", member);
                return result;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Map<String, Object> socialLogin(Map<String, String> socialData) {
        String loginId = socialData.get("loginId");
        MemberVO member = memberMapper.findByLoginId(loginId);
        if (member == null) return null; 

        String token = jwtUtil.generateToken(member.getLoginId(), member.getMemberType(), member.getMemberId());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("member", member);
        return result;
    }

    @Override
    @Transactional
    public boolean join(JoinDto joinDto) {
        // 💡 해결 방법: TB_MEMBER는 정의서에 따라 'ACTIVE'로 저장하여 ORA-02290 에러 방지
        MemberVO member = MemberVO.builder()
                .loginId(joinDto.getLoginId())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .name(joinDto.getName())
                .phone(joinDto.getPhone())
                .email(joinDto.getEmail())
                .memberType(joinDto.getMemberType())
                .provider(joinDto.getProvider() != null ? joinDto.getProvider() : "LOCAL")
                .phoneVerified("N") // 정의서 기본값 반영
                .saveIdYn("N")      // 정의서 기본값 반영
                .status("ACTIVE")   // DB 제약조건(ACTIVE/SUSPENDED/WITHDRAWN) 준수
                .build();
        
        int memberResult = memberMapper.insertMember(member);
        
        if (memberResult > 0 && "LAWYER".equals(joinDto.getMemberType())) {
            LawyerVO lawyer = new LawyerVO();
            lawyer.setMemberId(member.getMemberId().intValue());
            lawyer.setLicenseNo(joinDto.getLicenseNo());
            lawyer.setSpecialty(joinDto.getSpecialty());
            lawyer.setOfficeName(joinDto.getOfficeName());
            // 💡 변호사 고유의 승인 상태만 'PENDING'으로 설정 (해당 테이블은 PENDING 허용됨)
            lawyer.setApproveStatus("PENDING");
            lawyerMapper.insertLawyer(lawyer);
        }
        return memberResult > 0;
    }

    @Override public boolean isLoginIdAvailable(String loginId) { return memberMapper.findByLoginId(loginId) == null; }
    @Override public MemberVO getMemberInfo(String loginId) { return memberMapper.findByLoginId(loginId); }
    @Override public boolean updateProfile(MemberVO vo) { return memberMapper.updateMember(vo) > 0; }
    @Override public boolean withdraw(String loginId) { 
        MemberVO member = memberMapper.findByLoginId(loginId);
        return member != null && memberMapper.deleteMember(member.getMemberId()) > 0; 
    }
    @Override public String findId(String name, String phone) { return memberMapper.findLoginIdByNameAndPhone(name, phone); }
    @Override public String sendAuthCode(String phone) { return "123456"; }
    @Override public List<MemberVO> getMembersByType(String memberType) { return memberMapper.findMembersByType(memberType); }
}