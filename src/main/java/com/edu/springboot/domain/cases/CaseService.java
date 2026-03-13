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
			List<AttachmentVO> files = attachmentMapper.findAttachmentsByRef("CASE", caseId);
			caseDetail.setFiles(files);
		}
		return caseDetail;
	}

	@Transactional
	public void updateCaseStep(Long caseId, String step) {
		caseMapper.updateCaseStep(caseId, step);
	}

	@Transactional
	public void createCaseManual(CaseVO caseVO) {
		caseMapper.insertManualCase(caseVO);
	}

	@Transactional
	public void createCaseManual(CaseVO caseVO, Long consultId) {
		caseMapper.insertManualCase(caseVO);

		if (consultId != null && caseVO.getCaseId() != null) {
			caseMapper.updateConsultCaseId(consultId, caseVO.getCaseId());
		}
	}

	@Transactional
	public void updateCaseInfoWithFiles(CaseVO caseVO, List<MultipartFile> files) {
		caseMapper.updateCaseInfo(caseVO);

		if (files != null && !files.isEmpty()) {
			for (MultipartFile file : files) {
				if (file.isEmpty())
					continue;
				try {
					String savePath = fileUtil.saveFile(file, "case");
					if (savePath != null) {
						AttachmentVO attach = new AttachmentVO();
						attach.setRefType("CASE");
						attach.setRefId(caseVO.getCaseId());
						attach.setUploaderId(1L);
						attach.setOrigName(file.getOriginalFilename());
						attach.setSavePath(savePath);
						attach.setFileSize(file.getSize());
						attachmentMapper.insertAttachment(attach);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Transactional
	public void submitCaseReview(Long caseId, int rating, String content) {
		CaseVO caseVO = caseMapper.selectCaseById(caseId);
		if (caseVO == null)
			return;

		Long consultId = caseMapper.selectConsultIdByCaseId(caseId);

		if (consultId == null) {
			consultId = caseMapper.selectFallbackConsultId(caseVO.getMemberId(), caseVO.getLawyerId());
		}

		caseMapper.insertCaseReview(consultId, caseVO.getMemberId(), caseVO.getLawyerId(), rating, content);
		// 💡 [에러 원인 제거] TB_LAWYER 테이블 업데이트 로직 삭제 완료
	}

	public List<CaseVO> getAllCasesForAdmin() {
		return caseMapper.selectAllCases();
	}
}