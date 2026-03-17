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

// 실제 비즈니스 로직이 구현된 서비스 클래스
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

//	아이디 중복 확인
	@Override
	public boolean isLoginIdAvailable(String loginId) {
		return memberMapper.findByLoginId(loginId) == null;
	}

//	데이터를 조합해서 회원가입 진행
	@Override
	@Transactional
	public boolean join(JoinDto dto) {
//		탈퇴한 지 30일 지났는지 체크
		validateSignup(dto.getLoginId(), dto.getEmail());

//		DB에 넣을 회원 데이터 조립
		MemberVO member = new MemberVO();
		member.setLoginId(dto.getLoginId());
//		비밀번호 암호화해서 넣음
		member.setPassword(passwordEncoder.encode(dto.getPassword()));
		member.setMemberType(dto.getMemberType());
		member.setName(dto.getName());
		member.setPhone(dto.getPhone());
		member.setEmail(dto.getEmail());
//		소셜 아니면 로컬로 세팅
		member.setProvider(dto.getProvider() != null ? dto.getProvider() : "LOCAL");
		member.setPhoneVerified("Y");
		member.setStatus("ACTIVE");
		member.setSaveIdYn("N");
		member.setAddress(dto.getAddress());
		member.setDetailAddress(dto.getDetailAddress());

//		회원 테이블에 인서트
		memberMapper.insertMember(member);

//		변호사로 가입하면 변호사 테이블에도 인서트
		if ("LAWYER".equals(dto.getMemberType())) {
			LawyerVO lawyer = new LawyerVO();

			lawyer.setMemberId(member.getMemberId().intValue());
			lawyer.setLicenseNo(dto.getLicenseNo());
			lawyer.setSpecialty(dto.getSpecialty());
			lawyer.setOfficeName(dto.getOfficeName());
			lawyer.setOfficeAddr(dto.getAddress());
			lawyer.setOfficeDetailAddr(dto.getDetailAddress());
//			관리자가 확인 전까지 대기 상태
			lawyer.setApproveStatus("PENDING");

			lawyerMapper.insertLawyer(lawyer);

//			첨부파일 있으면 서버 폴더에 저장하고 DB에 기록
			if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
				for (MultipartFile file : dto.getFiles()) {
					try {
						String savePath = fileUtil.saveFile(file, "member");
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
						throw new RuntimeException("파일 저장 중 오류 발생", e);
					}
				}
			}
		}
		return true;
	}

//	로그인 처리하고 토큰 만들어줌
	@Override
	public Map<String, Object> login(LoginDto dto) {
		MemberVO member = memberMapper.findByLoginId(dto.getLoginId());

		if (member == null) {
			throw new RuntimeException("아이디 또는 비밀번호가 틀림");
		}

//		옛날 방식 암호화 안 된 비밀번호 때문에 서버 터지는 거 막는 안전 장치
		boolean isPasswordMatch = false;
		try {
			isPasswordMatch = passwordEncoder.matches(dto.getPassword(), member.getPassword());
		} catch (IllegalArgumentException e) {
			isPasswordMatch = false;
		}

		if (!isPasswordMatch) {
			throw new RuntimeException("아이디 또는 비밀번호가 틀림");
		}

//		탈퇴한 계정 막음
		if ("WITHDRAWN".equals(member.getStatus()) || "ANONYMIZED".equals(member.getStatus())) {
			throw new RuntimeException("탈퇴한 회원임");
		}

//		승인 안 난 변호사 막음
		if ("LAWYER".equals(member.getMemberType()) && "PENDING".equals(member.getApproveStatus())) {
			throw new RuntimeException("관리자 승인 대기 중임");
		}

//		문제 없으면 토큰 발급해서 리턴
		String token = jwtUtil.generateToken(member.getLoginId(), member.getMemberType(), member.getMemberId());

		Map<String, Object> response = new HashMap<>();
		response.put("token", token);
		response.put("member", member);
		return response;
	}

//	구글 소셜 로그인 검증
	@Override
	public Map<String, Object> socialLogin(Map<String, String> socialData) {
		String email = socialData.get("email");
		if (email == null)
			return null;

		MemberVO member = memberMapper.findByLoginId(email);

		if (member == null) {
			String loginId = email.split("@")[0];
			member = memberMapper.findByLoginId(loginId);
		}

		if (member != null && ("WITHDRAWN".equals(member.getStatus()) || "ANONYMIZED".equals(member.getStatus()))) {
			throw new RuntimeException("탈퇴한 회원임");
		}

//		기존에 가입된 구글 유저면 바로 로그인 시켜줌
		if (member != null && "GOOGLE".equals(member.getProvider())) {
			String token = jwtUtil.generateToken(member.getLoginId(), member.getMemberType(), member.getMemberId());
			Map<String, Object> response = new HashMap<>();
			response.put("token", token);
			response.put("member", member);
			return response;
		}
		return null;
	}

//	특정 회원 정보 가져옴
	@Override
	public MemberVO getMemberInfo(String loginId) {
		return memberMapper.findByLoginId(loginId);
	}

//	프로필 수정 적용
	@Override
	@Transactional
	public boolean updateProfile(MemberVO vo) {
//		비밀번호 바꿨으면 암호화해서 세팅
		if (vo.getPassword() != null && !vo.getPassword().isEmpty()) {
			vo.setPassword(passwordEncoder.encode(vo.getPassword()));
		} else {
//			안 바꿨으면 원래 거 유지
			vo.setPassword(null);
		}

		int result = memberMapper.updateMember(vo);

		return result > 0;
	}

//	탈퇴 처리 진행
	@Override
	public boolean withdraw(String loginId) {
		MemberVO member = memberMapper.findByLoginId(loginId);
		return member != null && memberMapper.deleteMember((long) member.getMemberId()) > 0;
	}

//	이름이랑 번호로 아이디 찾아줌
	@Override
	public String findId(String name, String phone) {
		return memberMapper.findLoginIdByNameAndPhone(name, phone);
	}

//	인증번호 보냄
	@Override
	public String sendAuthCode(String phone) {
		return "123456";
	}

//	탈퇴 이력 체크
	@Override
	public void validateSignup(String loginId, String email) {
		MemberVO existingById = memberMapper.findByLoginId(loginId);
		if (existingById != null) {
			checkWithdrawnStatus(existingById, "아이디");
		}

		MemberVO existingByEmail = memberMapper.findByEmail(email);
		if (existingByEmail != null) {
			checkWithdrawnStatus(existingByEmail, "이메일");
		}
	}

//	30일 제한 계산하는 로직
	private void checkWithdrawnStatus(MemberVO member, String type) {
		if ("ACTIVE".equals(member.getStatus())) {
			throw new IllegalStateException("이미 사용 중인 " + type + "임");
		}
		if ("WITHDRAWN".equals(member.getStatus()) && member.getWithdrawnAt() != null) {
			long diff = System.currentTimeMillis() - member.getWithdrawnAt().getTime();
			long days = diff / (1000 * 60 * 60 * 24);
			if (days < 30) {
				throw new IllegalStateException(
						"탈퇴 후 30일 이내에는 동일한 " + type + "(으)로 재가입할 수 없음 (남은 기간: " + (30 - days) + "일)");
			}
		}
	}

//	회원 번호로 탈퇴 상태 업데이트
	@Override
	@Transactional
	public boolean withdrawMember(Long memberId) {
		return memberMapper.withdrawMember(memberId) > 0;
	}

//	마이페이지 내가 쓴 글 목록 조회용 데이터 세팅
	@Override
	public List<Map<String, Object>> findMyPosts(Long memberId, String type) {
		Map<String, Object> params = new HashMap<>();
		params.put("memberId", memberId);
		params.put("type", type);

		return memberMapper.findMyPosts(params);
	}
}