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

// 변호사 정보 관리와 관련된 비즈니스 로직을 전담하는 서비스 클래스
@Service
public class LawyerService {

	@Autowired
	private LawyerMapper lawyerMapper;

	@Autowired
	private AttachmentMapper attachmentMapper;

	@Autowired
	private FileUtil fileUtil;

//	전체 변호사 목록을 조회하여 컨트롤러로 넘겨주는 로직
	public List<LawyerVO> getAllLawyers() {
		return lawyerMapper.selectAllLawyers();
	}

//	변호사 고유 아이디를 사용해 한 명의 정보를 상세히 조회하는 로직
	public LawyerVO getLawyerById(Long id) {
		return lawyerMapper.selectLawyerById(id);
	}

//	회원 아이디를 바탕으로 연결된 변호사 정보를 조회해 넘겨주는 로직
	public LawyerVO getLawyerByMemberId(Long memberId) {
		return lawyerMapper.selectLawyerByMemberId(memberId);
	}

//	변호사 프로필 수정 요청이 왔을 때 데이터베이스 반영을 지시하는 로직
	public int updateLawyerProfile(LawyerVO lawyerVO) {
		return lawyerMapper.updateLawyerProfile(lawyerVO);
	}

//	업로드된 프로필 이미지를 물리적 폴더에 저장하고 기존 사진 데이터는 삭제한 뒤 새로운 데이터로 등록하는 로직
	public String updateProfileImage(Long lawyerId, MultipartFile file) {
		try {
//			서버의 로컬 폴더 경로에 전달받은 파일을 실제로 저장
			String savePath = fileUtil.saveFile(file, "lawyer");
			if (savePath == null)
				return "fail";

//			첨부파일 관리를 위해 업로드하는 변호사의 회원 아이디를 조회
			LawyerVO lawyer = lawyerMapper.selectLawyerById(lawyerId);
			if (lawyer == null)
				return "fail";

//			중복해서 사진이 쌓이는 것을 막기 위해 기존 프로필 사진 데이터를 찾아 삭제 처리
			attachmentMapper.deleteAttachmentsByRef("LAWYER", lawyerId);

//			방금 저장한 새로운 사진의 경로와 정보를 첨부파일 테이블에 기록
			AttachmentVO attach = new AttachmentVO();
			attach.setRefType("LAWYER");
			attach.setRefId(lawyerId);
			attach.setUploaderId((long) lawyer.getMemberId());
			attach.setOrigName(file.getOriginalFilename());
			attach.setSavePath(savePath);
			attach.setFileSize(file.getSize());
			attach.setMimeType(file.getContentType());

			int result = attachmentMapper.insertAttachment(attach);
			return (result > 0) ? savePath : "fail";

		} 
		catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
	}
}