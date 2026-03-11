package com.edu.springboot.domain.question;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.edu.springboot.common.util.FileUtil;
import com.edu.springboot.domain.attachment.AttachmentMapper;
import com.edu.springboot.domain.attachment.vo.AttachmentVO;
import com.edu.springboot.domain.question.vo.QuestionVO;

@Service
public class QuestionService {

	@Autowired
	private QuestionMapper questionMapper;

	@Autowired
	private AttachmentMapper attachmentMapper;

	@Autowired
	private FileUtil fileUtil;

	/**
	 * 1. 질문 등록 (텍스트 + 다중 파일)
	 */
	@Transactional
	public boolean writeQuestion(QuestionVO questionVO, List<MultipartFile> files) {
		int result = questionMapper.insertQuestion(questionVO);
		if (result <= 0)
			return false;

		this.saveFiles(questionVO.getQuestionId(), questionVO.getMemberId(), files);
		return true;
	}

	/**
	 * 2. 질문 수정 (채택 여부 확인 포함)
	 */
	@Transactional
	public boolean updateQuestion(QuestionVO questionVO, List<MultipartFile> files) {
		QuestionVO existing = questionMapper.selectQuestionById(questionVO.getQuestionId());
		if (existing == null || "ADOPTED".equals(existing.getStatus()))
			return false;

		int result = questionMapper.updateQuestion(questionVO);
		if (result > 0 && files != null && !files.isEmpty()) {
			this.saveFiles(questionVO.getQuestionId(), questionVO.getMemberId(), files);
		}
		return result > 0;
	}

	/**
	 * 3. 질문 삭제 (답변 유무 확인) 💡 수정 포인트: question.getAnswers() 대신 answerCount 필드 사용
	 */
	@Transactional
	public String deleteQuestion(Long questionId) {
		QuestionVO question = questionMapper.selectQuestionById(questionId);
		if (question == null)
			return "NOT_FOUND";

		// 💡 리스트를 조회하지 않고, DB에서 서브쿼리로 가져온 개수만 확인합니다.
		if (question.getAnswerCount() > 0)
			return "HAS_ANSWERS";

		attachmentMapper.deleteAttachmentsByRef("QUESTION", questionId);
		int result = questionMapper.deleteQuestion(questionId);
		return result > 0 ? "SUCCESS" : "FAIL";
	}

	/**
	 * 4. 답변 채택 (트랜잭션 필수)
	 */
	@Transactional
	public boolean adoptAnswer(Map<String, Object> params) {
		try {
			Long questionId = Long.valueOf(params.get("questionId").toString());
			Long lawyerId = Long.valueOf(params.get("lawyerId").toString());
			Long memberId = Long.valueOf(params.get("memberId").toString());
			Long answerId = Long.valueOf(params.get("answerId").toString());

			int qResult = questionMapper.updateQuestionAdoption(questionId, lawyerId, memberId);
			int aResult = questionMapper.updateAnswerAdoption(answerId);

			return qResult > 0 && aResult > 0;
		} catch (Exception e) {
			return false;
		}
	}

	private void saveFiles(Long refId, Long uploaderId, List<MultipartFile> files) {
		if (files == null || files.isEmpty())
			return;

		for (MultipartFile file : files) {
			if (file.isEmpty())
				continue;

			try {
				String savePath = fileUtil.saveFile(file, "question");

				if (savePath != null) {
					AttachmentVO attach = new AttachmentVO();
					attach.setRefType("QUESTION");
					attach.setRefId(refId);
					attach.setUploaderId(uploaderId);
					attach.setOrigName(file.getOriginalFilename());
					attach.setSavePath(savePath);
					attach.setFileSize(file.getSize());
					attach.setMimeType(file.getContentType());

					attachmentMapper.insertAttachment(attach);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("파일 저장 중 오류가 발생했습니다: " + file.getOriginalFilename());
			}
		}
	}

}