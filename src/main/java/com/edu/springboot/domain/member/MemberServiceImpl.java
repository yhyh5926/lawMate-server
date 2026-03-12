// 파일위치: src/main/java/com/edu/springboot/domain/member/MemberServiceImpl.java
// IntelliJ
// 파일위치: src/main/java/com/edu/springboot/domain/member/MemberServiceImpl.java
package com.edu.springboot.domain.member;

// import 
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

// Spring 프레임워크 서비스 계층 선언
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

//	아이디 중복 여부 확인
	@Override
	public boolean isLoginIdAvailable(String loginId) {
		return memberMapper.findByLoginId(loginId) == null;
	}

//	회원가입 처리 및 트랜잭션 적용
	@Override
	@Transactional
	public boolean join(JoinDto dto) {
//		가입 전 30일 이내 탈퇴 이력 검증
		validateSignup(dto.getLoginId(), dto.getEmail());

//		회원 정보 세팅
		MemberVO member = new MemberVO();
		member.setLoginId(dto.getLoginId());
//		비밀번호 단방향 암호화 적용
		member.setPassword(passwordEncoder.encode(dto.getPassword()));
		member.setMemberType(dto.getMemberType());
		member.setName(dto.getName());
		member.setPhone(dto.getPhone());
		member.setEmail(dto.getEmail());
//		제공자 정보가 없으면 로컬 가입으로 기본값 설정
		member.setProvider(dto.getProvider() != null ? dto.getProvider() : "LOCAL");
		member.setPhoneVerified("Y");
		member.setStatus("ACTIVE");
		member.setSaveIdYn("N");
		member.setAddress(dto.getAddress());
		member.setDetailAddress(dto.getDetailAddress());

//		기본 회원 정보 DB 저장
		memberMapper.insertMember(member);

//		변호사 회원일 경우 추가 로직 실행
		if ("LAWYER".equals(dto.getMemberType())) {
			LawyerVO lawyer = new LawyerVO();

			lawyer.setMemberId(member.getMemberId().intValue());
			lawyer.setLicenseNo(dto.getLicenseNo());
			lawyer.setSpecialty(dto.getSpecialty());
			lawyer.setOfficeName(dto.getOfficeName());
			lawyer.setOfficeAddr(dto.getOfficeAddress());
			lawyer.setOfficeDetailAddr(dto.getOfficeDetailAddr());
//			관리자 승인 대기(PENDING) 상태로 초기 설정
			lawyer.setApproveStatus("PENDING");

//			변호사 부가 정보 DB 저장
			lawyerMapper.insertLawyer(lawyer);

//			증빙 파일이 존재할 경우 파일 저장 수행
			if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
				for (MultipartFile file : dto.getFiles()) {
					try {
//						증빙서류는 "lawyer" 폴더에 분리 저장
						String savePath = fileUtil.saveFile(file, "lawyer");
						if (savePath != null) {
							AttachmentVO attach = new AttachmentVO();
							attach.setRefType("LAWYER");
							attach.setRefId((long) lawyer.getLawyerId());
							attach.setUploaderId((long) member.getMemberId());
							attach.setOrigName(file.getOriginalFilename());
							attach.setSavePath(savePath);
							attach.setFileSize(file.getSize());
							attach.setMimeType(file.getContentType());
//							파일 메타데이터 DB 기록
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

//	일반 로그인 처리 및 JWT 발급
	@Override
	public Map<String, Object> login(LoginDto dto) {
		MemberVO member = memberMapper.findByLoginId(dto.getLoginId());

//		아이디가 없으면 예외 발생
		if (member == null) {
			throw new RuntimeException("아이디 또는 비밀번호가 올바르지 않습니다.");
		}

//		DB에 암호화되지 않은 옛날 비밀번호(1234 등)가 남아있을 경우 서버가 뻗는 것을 방지
		boolean isPasswordMatch = false;
		try {
//			정상적인 BCrypt 해시값이면 안전하게 비교
			isPasswordMatch = passwordEncoder.matches(dto.getPassword(), member.getPassword());
		} catch (IllegalArgumentException e) {
//			DB의 비밀번호가 해시 형태가 아니라서 나는 에러를 잡아내어 무시(무시 안하면 서버 에러 발생함)
			isPasswordMatch = false;
		}

//		비밀번호가 일치하지 않으면 예외 발생
		if (!isPasswordMatch) {
			throw new RuntimeException("아이디 또는 비밀번호가 올바르지 않습니다.");
		}

//		탈퇴 및 비식별화 회원 로그인 차단 적용
		if ("WITHDRAWN".equals(member.getStatus()) 
				|| "ANONYMIZED".equals(member.getStatus())) {
			throw new RuntimeException("탈퇴한 회원입니다.");
		}

//		승인 대기 중인 변호사 회원 접근 차단
		if ("LAWYER".equals(member.getMemberType()) 
				&& "PENDING".equals(member.getApproveStatus())) {
			throw new RuntimeException("관리자 승인 대기 중인 전문회원 계정입니다.");
		}

//		모든 검증 통과 시 JWT 토큰 발급
		String token = jwtUtil.generateToken(member.getLoginId(),
				member.getMemberType(), member.getMemberId());

		Map<String, Object> response = new HashMap<>();
		response.put("token", token);
		response.put("member", member);
		return response;
	}

//	소셜 로그인 처리 및 JWT 발급
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

//		소셜 로그인 시에도 탈퇴/비식별화 상태면 차단 적용
		if (member != null && ("WITHDRAWN".equals(member.getStatus()) || "ANONYMIZED".equals(member.getStatus()))) {
			throw new RuntimeException("탈퇴한 회원입니다.");
		}

//		구글 로그인 회원인 경우 토큰 발급 후 응답 반환
		if (member != null && "GOOGLE".equals(member.getProvider())) {
			String token = jwtUtil.generateToken(member.getLoginId(), member.getMemberType(), member.getMemberId());
			Map<String, Object> response = new HashMap<>();
			response.put("token", token);
			response.put("member", member);
			return response;
		}
		return null;
	}

//	특정 회원 상세 정보 조회
	@Override
	public MemberVO getMemberInfo(String loginId) {
		return memberMapper.findByLoginId(loginId);
	}

//	회원 프로필 정보 업데이트
	@Override
	@Transactional
	public boolean updateProfile(MemberVO vo) {
		// 1. 비밀번호 변경 처리
		if (vo.getPassword() != null && !vo.getPassword().isEmpty()) {
			vo.setPassword(passwordEncoder.encode(vo.getPassword()));
		} else {
			// 비밀번호가 빈 값이면 기존 비밀번호 유지 (쿼리에서 null 처리 방지)
			vo.setPassword(null);
		}

		// 2. 일반 회원 정보 업데이트 (TB_MEMBER)
		int result = memberMapper.updateMember(vo);

		return result > 0;
	}

//	회원 탈퇴 처리
	@Override
	public boolean withdraw(String loginId) {
		MemberVO member = memberMapper.findByLoginId(loginId);
		return member != null && memberMapper.deleteMember((long) member.getMemberId()) > 0;
	}

//	이름과 전화번호로 아이디 찾기
	@Override
	public String findId(String name, String phone) {
		return memberMapper.findLoginIdByNameAndPhone(name, phone);
	}

//	인증 번호 발송 처리
	@Override
	public String sendAuthCode(String phone) {
		return "123456";
	}

//	아이디와 이메일 기준으로 기존 회원 상태 확인
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

//	탈퇴 회원의 재가입 가능 여부를 30일 기준으로 검증
	private void checkWithdrawnStatus(MemberVO member, String type) {
		if ("ACTIVE".equals(member.getStatus())) {
			throw new IllegalStateException("이미 사용 중인 " + type + "입니다.");
		}
//		탈퇴 상태이고 탈퇴일자가 존재하면 30일 경과 여부 계산
		if ("WITHDRAWN".equals(member.getStatus()) && member.getWithdrawnAt() != null) {
			long diff = System.currentTimeMillis() - member.getWithdrawnAt().getTime();
			long days = diff / (1000 * 60 * 60 * 24);
			if (days < 30) {
				throw new IllegalStateException(
						"탈퇴 후 30일 이내에는 동일한 " + type + "(으)로 재가입할 수 없습니다. (남은 기간: " + (30 - days) + "일)");
			}
		}
	}

//	프론트엔드 연동용 탈퇴 처리
	@Override
	@Transactional
	public boolean withdrawMember(Long memberId) {
		return memberMapper.withdrawMember(memberId) > 0;
	}

//	내가 쓴 글 목록 조회
	@Override
	public List<Map<String, Object>> getMyPosts(Long memberId, String type) {
		return memberMapper.findMyPosts(memberId, type);
	}
}