// src/main/java/com/edu/springboot/domain/lawyer/LawyerService.java
package com.edu.springboot.domain.lawyer;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.edu.springboot.domain.lawyer.vo.LawyerVO;
import com.edu.springboot.common.util.FileUtil;
import com.edu.springboot.domain.attachment.AttachmentMapper;
import com.edu.springboot.domain.attachment.vo.AttachmentVO;

@Service // 💡 스프링이 관리하는 서비스 객체임을 선언
public class LawyerService {

	@Autowired
	private LawyerMapper lawyerMapper;

	@Autowired
	private AttachmentMapper attachmentMapper; // 💡 첨부파일 DB 관리를 위해 주입

	@Autowired
	private FileUtil fileUtil; // 💡 로컬 폴더에 실제 파일을 저장하기 위해 주입

	public List<LawyerVO> getAllLawyers() {
		return lawyerMapper.selectAllLawyers();
	}

	public LawyerVO getLawyerById(Long id) {
		return lawyerMapper.selectLawyerById(id);
	}

	// 26.03.04 원석 추가
	public LawyerVO getLawyerByMemberId(Long memberId) {
		return lawyerMapper.selectLawyerByMemberId(memberId);
	}

	public int updateLawyerProfile(LawyerVO lawyerVO) {
		return lawyerMapper.updateLawyerProfile(lawyerVO);
	}

	// 프로필 이미지 실제 저장 및 DB 기록 로직
	public String updateProfileImage(Long lawyerId, MultipartFile file) {
		try {
			// 1. FileUtil을 사용해 로컬 폴더(uploads)에 파일 물리적 저장
			String savePath = fileUtil.saveFile(file);
			if (savePath == null)
				return "fail";

			// 2. TB_ATTACHMENT의 업로더 ID를 넣기 위해 변호사의 Member ID 조회
			LawyerVO lawyer = lawyerMapper.selectLawyerById(lawyerId);
			if (lawyer == null)
				return "fail";

			// 3. 기존에 등록된 이 변호사의 프로필 사진 데이터 삭제 (중복 방지)
			attachmentMapper.deleteAttachmentsByRef("LAWYER", lawyerId);

			// 4. 새로운 프로필 사진 정보를 TB_ATTACHMENT에 등록
			AttachmentVO attach = new AttachmentVO();
			attach.setRefType("LAWYER");
			attach.setRefId(lawyerId);
			attach.setUploaderId((long) lawyer.getMemberId()); // DB 필수값 세팅
			attach.setOrigName(file.getOriginalFilename());
			attach.setSavePath(savePath);
			attach.setFileSize(file.getSize());
			attach.setMimeType(file.getContentType());

			attachmentMapper.insertAttachment(attach);

			return savePath;
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
}