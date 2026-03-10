// IntelliJ
// 파일위치: src/main/java/com/edu/springboot/domain/cases/CaseService.java

package com.edu.springboot.domain.cases;

import com.edu.springboot.common.util.FileUtil;
import com.edu.springboot.domain.attachment.AttachmentMapper;
import com.edu.springboot.domain.attachment.vo.AttachmentVO;
import com.edu.springboot.domain.cases.vo.CaseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CaseService {

	private final CaseMapper caseMapper;
	private final AttachmentMapper attachmentMapper;
	private final FileUtil fileUtil;

	public List<CaseVO> getCaseListByMember(Long memberId) {
		return caseMapper.selectCasesByMemberId(memberId);
	}

	public CaseVO getCaseDetail(Long caseId) {
		CaseVO caseDetail = caseMapper.selectCaseById(caseId);
		if (caseDetail != null) {
			// 💡 파일 목록을 TB_ATTACHMENT 테이블에서 가져와 VO에 세팅
			List<AttachmentVO> files = attachmentMapper.findAttachmentsByRef("CASE", caseId);
			caseDetail.setFiles(files); // CaseVO.java 에 private List<AttachmentVO> files; 필드 필요
		}
		return caseDetail;
	}

	@Transactional
	public void updateCaseStep(Long caseId, String step) {
		caseMapper.updateCaseStep(caseId, step);
	}

	// 💡 수동 사건 등록
	@Transactional
	public void createCaseManual(CaseVO caseVO) {
		caseMapper.insertCase(caseVO);
	}

	// 💡 내용 수정 및 파일 첨부 동시 처리
	@Transactional
	public void updateCaseInfoWithFiles(CaseVO caseVO, List<MultipartFile> files) {
		// 1. 텍스트 정보(내용, 코멘트) 업데이트
		caseMapper.updateCaseInfo(caseVO);

		// 2. 파일이 존재할 경우 서버 물리 디렉토리에 저장 후 DB 기록
		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				if (file.isEmpty())
					continue;
				try {
					// FileUtil을 통해 uploads/case/ 폴더에 저장한다고 가정
					String savePath = fileUtil.saveFile(file, "case");
					if (savePath != null) {
						AttachmentVO attach = new AttachmentVO();
						attach.setRefType("CASE");
						attach.setRefId(caseVO.getCaseId());
						attach.setUploaderId(1L); // 테스트용 (실제로는 세션 유저 ID)
						attach.setOrigName(file.getOriginalFilename());
						attach.setSavePath(savePath);
						attach.setFileSize(file.getSize());
						attach.setMimeType(file.getContentType());

						attachmentMapper.insertAttachment(attach);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 💡 [에러 해결] 관리자 컨트롤러(AdminController)에서 호출하는 전체 사건 조회 로직 복구
	public List<CaseVO> getAllCasesForAdmin() {
		return caseMapper.selectAllCases();
	}
}