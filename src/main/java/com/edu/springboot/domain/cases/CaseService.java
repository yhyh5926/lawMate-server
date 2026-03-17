// src/main/java/com/edu/springboot/domain/cases/CaseService.java
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

// 사건 관리와 관련된 비즈니스 로직을 처리하는 서비스 클래스
@Service
@RequiredArgsConstructor
public class CaseService {

	private final CaseMapper caseMapper;
	private final AttachmentMapper attachmentMapper;
	private final FileUtil fileUtil;

//	특정 회원의 사건 목록을 조회하여 반환하는 로직
	public List<CaseVO> getCaseListByMember(Long memberId) {
		return caseMapper.selectCasesByMemberId(memberId);
	}

//	특정 사건의 상세 정보를 조회하고 연관된 첨부파일 목록도 함께 가져오는 로직
	public CaseVO getCaseDetail(Long caseId) {
		CaseVO caseDetail = caseMapper.selectCaseById(caseId);
		if (caseDetail != null) {
			List<AttachmentVO> files = attachmentMapper.findAttachmentsByRef("CASE", caseId);
			caseDetail.setFiles(files);
		}
		return caseDetail;
	}

//	사건의 진행 단계를 변경하며 데이터베이스 트랜잭션을 보장하는 로직
	@Transactional
	public void updateCaseStep(Long caseId, String step) {
		caseMapper.updateCaseStep(caseId, step);
	}

//	변호사가 수동으로 사건을 등록할 때 사용하는 로직
	@Transactional
	public void createCaseManual(CaseVO caseVO) {
		caseMapper.insertManualCase(caseVO);
	}

//	변호사가 수동으로 사건을 등록하면서 기존 상담글과 연결해주는 로직
	@Transactional
	public void createCaseManual(CaseVO caseVO, Long consultId) {
		caseMapper.insertManualCase(caseVO);

		if (consultId != null && caseVO.getCaseId() != null) {
			caseMapper.updateConsultCaseId(consultId, caseVO.getCaseId());
		}
	}

//	사건 내용을 수정하면서 새로운 첨부파일이 있을 경우 서버에 저장하고 데이터베이스에 기록하는 로직
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

//	사건 완료 후 후기와 평점을 저장하는 로직
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
	}

//	관리자 페이지에서 전체 사건 목록을 조회하기 위한 로직
	public List<CaseVO> getAllCasesForAdmin() {
		return caseMapper.selectAllCases();
	}
}