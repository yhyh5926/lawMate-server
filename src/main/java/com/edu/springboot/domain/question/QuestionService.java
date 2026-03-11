package com.edu.springboot.domain.question;

import java.util.List;
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
        if (result <= 0) return false;

        if (files != null && !files.isEmpty()) {
            this.saveFiles(questionVO.getQuestionId(), questionVO.getMemberId(), files);
        }
        return true;
    }

    /**
     * 2. 질문 수정 (채택 여부 확인 포함)
     */
    @Transactional
    public boolean updateQuestion(QuestionVO questionVO, List<MultipartFile> files) {
        QuestionVO existing = questionMapper.selectQuestionById(questionVO.getQuestionId());
        
        // 채택된 질문은 수정 불가
        if (existing == null || "ADOPTED".equals(existing.getStatus())) {
            return false;
        }

        int result = questionMapper.updateQuestion(questionVO);
        
        // 새 파일이 있는 경우 추가 저장 (기존 파일 유지 혹은 삭제 로직은 별도 구성 가능)
        if (result > 0 && files != null && !files.isEmpty()) {
            this.saveFiles(questionVO.getQuestionId(), questionVO.getMemberId(), files);
        }
        return result > 0;
    }

    /**
     * 3. 질문 삭제 (답변 유무 확인)
     */
    @Transactional
    public String deleteQuestion(Long questionId) {
        QuestionVO question = questionMapper.selectQuestionById(questionId);
        if (question == null) return "NOT_FOUND";

        // 답변이 이미 달린 질문은 삭제 불가 (정책에 따라 조정 가능)
        if (question.getAnswerCount() > 0) return "HAS_ANSWERS";

        // 첨부파일 DB 정보 및 실제 파일 삭제 (필요 시 fileUtil 연동)
        attachmentMapper.deleteAttachmentsByRef("QUESTION", questionId);
        
        int result = questionMapper.deleteQuestion(questionId);
        return result > 0 ? "SUCCESS" : "FAIL";
    }

    /**
     * 4. 질문 상태 업데이트 전용 (AnswerService에서 호출용)
     * 직접적인 adoptAnswer 비즈니스 로직은 AnswerService에서 담당합니다.
     */
    @Transactional
    public boolean updateStatus(Long questionId, String status, Long lawyerId) {
        int result = questionMapper.updateQuestionStatus(questionId, status, lawyerId);
        return result > 0;
    }

    /**
     * [내부 로직] 다중 파일 저장 처리
     */
    private void saveFiles(Long refId, Long uploaderId, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

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
                throw new RuntimeException("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
            }
        }
    }
}