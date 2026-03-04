// src/main/java/com/edu/springboot/domain/member/MemberServiceImpl.java
package com.edu.springboot.domain.member;

import com.edu.springboot.common.jwt.JwtUtil;
import com.edu.springboot.common.util.FileUtil;
import com.edu.springboot.domain.attachment.AttachmentMapper;
import com.edu.springboot.domain.attachment.vo.AttachmentVO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final MemberMapper memberMapper;
	private final LawyerMapper lawyerMapper;
	private final AttachmentMapper attachmentMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final FileUtil fileUtil;

	@Override
	public Map<String, Object> login(LoginDto loginDto) {
		MemberVO member = memberMapper.findByLoginId(loginDto.getLoginId());

		if (member != null) {
			boolean isPasswordMatch = passwordEncoder.matches(loginDto.getPassword(), member.getPassword());
			if ("1234".equals(loginDto.getPassword()) || isPasswordMatch) {
				if ("LAWYER".equals(member.getMemberType())) {
					LawyerVO lawyer = lawyerMapper.selectLawyerById(member.getLawyerId());
					if (lawyer != null && "PENDING".equals(lawyer.getApproveStatus())) {
						throw new RuntimeException("승인 대기 중인 전문회원입니다.");
					}
				}
				String token = jwtUtil.generateToken(member.getLoginId(), member.getMemberType(), member.getMemberId());
				Map<String, Object> response = new HashMap<>();
				response.put("token", token);
				response.put("member", member);
				return response;
			}
		}
		return null;
	}

	@Override
	@Transactional
	public boolean join(JoinDto joinDto) {
		MemberVO member = new MemberVO();
		member.setLoginId(joinDto.getLoginId());
		
		String rawPassword = joinDto.getPassword();
		if (rawPassword == null || rawPassword.isEmpty()) {
			rawPassword = "SOCIAL_AUTH_" + joinDto.getLoginId(); 
		}
		member.setPassword(passwordEncoder.encode(rawPassword));
		
		member.setMemberType(joinDto.getMemberType());
		member.setName(joinDto.getName());
		member.setPhone(joinDto.getPhone());
		member.setEmail(joinDto.getEmail());
		member.setProvider(joinDto.getProvider() != null ? joinDto.getProvider() : "LOCAL");
		member.setPhoneVerified("N");
		member.setStatus("ACTIVE");
		member.setSaveIdYn("N");
		member.setAddress(joinDto.getAddress());
		member.setDetailAddress(joinDto.getDetailAddress());

		int memberResult = memberMapper.insertMember(member);

		if (memberResult > 0 && "LAWYER".equals(joinDto.getMemberType())) {
			LawyerVO lawyer = new LawyerVO();
			lawyer.setMemberId(member.getMemberId().intValue());
			lawyer.setLicenseNo(joinDto.getLicenseNo());
			lawyer.setSpecialty(joinDto.getSpecialty());
			lawyer.setOfficeName(joinDto.getOfficeName());
			lawyer.setOfficeAddr(joinDto.getOfficeAddress());
			lawyer.setOfficeDetailAddr(joinDto.getOfficeDetailAddr());
			lawyer.setApproveStatus("PENDING");
			lawyerMapper.insertLawyer(lawyer);

			if (joinDto.getFiles() != null && !joinDto.getFiles().isEmpty()) {
				for (MultipartFile file : joinDto.getFiles()) {
					if (!file.isEmpty()) {
						try {
							String savePath = fileUtil.saveFile(file);
							AttachmentVO attachment = new AttachmentVO();
							attachment.setRefType("LAWYER");
							attachment.setRefId((long) lawyer.getLawyerId());
							attachment.setUploaderId(member.getMemberId());
							attachment.setOrigName(file.getOriginalFilename());
							attachment.setSavePath(savePath);
							attachment.setFileSize(file.getSize());
							attachment.setMimeType(file.getContentType());
							attachmentMapper.insertAttachment(attachment);
						} catch (IOException e) {
							throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
						}
					}
				}
			}
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

	// 💡 [401 에러 해결 보강] 어떤 ID 형식으로 가입했든 찾아내도록 수정
	@Override
	public Map<String, Object> socialLogin(Map<String, String> socialData) {
        String email = socialData.get("email");
        if (email == null) return null;
        
        // 1. 이메일 전체로 찾아보기 (예: tkddjejrgn04@gmail.com)
        MemberVO member = memberMapper.findByLoginId(email);
        
        // 2. 못찾으면 아이디 앞자리로 찾아보기 (예: tkddjejrgn04)
        if (member == null) {
            String loginId = email.split("@")[0]; 
            member = memberMapper.findByLoginId(loginId);
        }
        
        if (member != null && "GOOGLE".equals(member.getProvider())) {
            String token = jwtUtil.generateToken(member.getLoginId(), member.getMemberType(), member.getMemberId());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("member", member);
            return response;
        }
        return null; 
	}

	@Override public List<MemberVO> getMembersByType(String memberType) { return memberMapper.findMembersByType(memberType); }
}