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
	public boolean isLoginIdAvailable(String loginId) {
		return memberMapper.findByLoginId(loginId) == null;
	}

	@Override
	@Transactional
	public boolean join(JoinDto dto) {
		MemberVO member = new MemberVO();
		member.setLoginId(dto.getLoginId());
		member.setPassword(passwordEncoder.encode(dto.getPassword())); // 정상적인 단방향 암호화 적용
		member.setMemberType(dto.getMemberType());
		member.setName(dto.getName());
		member.setPhone(dto.getPhone());
		member.setEmail(dto.getEmail());
		member.setProvider(dto.getProvider() != null ? dto.getProvider() : "LOCAL");
		member.setPhoneVerified("Y");
		member.setStatus("ACTIVE");
		member.setSaveIdYn("N");
		member.setAddress(dto.getOfficeAddress());
		member.setDetailAddress(dto.getOfficeDetailAddr());

		memberMapper.insertMember(member);

		if ("LAWYER".equals(dto.getMemberType())) {
			LawyerVO lawyer = new LawyerVO();
			
			lawyer.setMemberId(member.getMemberId().intValue());
			lawyer.setLicenseNo(dto.getLicenseNo());
			lawyer.setSpecialty(dto.getSpecialty());
			lawyer.setOfficeName(dto.getOfficeName());
			lawyer.setOfficeAddr(dto.getOfficeAddress());
			lawyer.setOfficeDetailAddr(dto.getOfficeDetailAddr());
			lawyer.setApproveStatus("PENDING");

			lawyerMapper.insertLawyer(lawyer);

			if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
				for (MultipartFile file : dto.getFiles()) {
					try {
						String savePath = fileUtil.saveFile(file);
						if (savePath != null) {
							AttachmentVO attach = new AttachmentVO();
							attach.setRefType("LAWYER");
							attach.setRefId((long) lawyer.getLawyerId());
							attach.setUploaderId((long) member.getMemberId());
							attach.setOrigName(file.getOriginalFilename());
							attach.setSavePath(savePath);
							attach.setFileSize(file.getSize());
							attach.setMimeType(file.getContentType());
							attachmentMapper.insertAttachment(attach);
						}
					} catch (IOException e) {
						throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
					}
				}
			}
		}
		return true;
	}

	@Override
	public Map<String, Object> login(LoginDto dto) {
		MemberVO member = memberMapper.findByLoginId(dto.getLoginId());

		if (member == null) {
			throw new RuntimeException("아이디 또는 비밀번호가 올바르지 않습니다.");
		}

		// 💡 [500 에러 해결] DB에 암호화되지 않은 옛날 비밀번호(p1 등)가 남아있을 경우 서버가 뻗는 것을 방지
		boolean isPasswordMatch = false;
		try {
			// 정상적인 BCrypt 해시값이면 안전하게 비교
			isPasswordMatch = passwordEncoder.matches(dto.getPassword(), member.getPassword());
		} catch (IllegalArgumentException e) {
			// DB의 비밀번호가 해시 형태가 아니라서 나는 에러를 잡아내어 무시 (서버 안 뻗게 방어)
			isPasswordMatch = false;
		}

		if (!isPasswordMatch) {
			throw new RuntimeException("아이디 또는 비밀번호가 올바르지 않습니다.");
		}

		if ("WITHDRAWN".equals(member.getStatus())) {
			throw new RuntimeException("탈퇴한 회원입니다.");
		}

		if ("LAWYER".equals(member.getMemberType()) && "PENDING".equals(member.getApproveStatus())) {
			throw new RuntimeException("관리자 승인 대기 중인 전문회원 계정입니다.");
		}

		String token = jwtUtil.generateToken(member.getLoginId(), member.getMemberType(), member.getMemberId());

		Map<String, Object> response = new HashMap<>();
		response.put("token", token);
		response.put("member", member);
		return response;
	}

	@Override
	public Map<String, Object> socialLogin(Map<String, String> socialData) {
		String email = socialData.get("email");
		if (email == null) return null;
		
		MemberVO member = memberMapper.findByLoginId(email);
		
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

	@Override
	public MemberVO getMemberInfo(String loginId) {
		return memberMapper.findByLoginId(loginId);
	}

	@Override
	public boolean updateProfile(MemberVO vo) {
		return memberMapper.updateMember(vo) > 0;
	}

	@Override
	public boolean withdraw(String loginId) { 
		MemberVO member = memberMapper.findByLoginId(loginId);
		return member != null && memberMapper.deleteMember((long) member.getMemberId()) > 0; 
	}

	@Override 
	public String findId(String name, String phone) { 
		return memberMapper.findLoginIdByNameAndPhone(name, phone); 
	}

	@Override 
	public String sendAuthCode(String phone) { 
		return "123456"; 
	}
}