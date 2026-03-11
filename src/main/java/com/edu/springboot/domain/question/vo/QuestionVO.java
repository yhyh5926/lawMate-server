package com.edu.springboot.domain.question.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;

import com.edu.springboot.domain.attachment.vo.AttachmentVO;

@Data
public class QuestionVO {
    // 기본 테이블 컬럼
    private Long questionId;      // QUESTION_ID (PK)
    private Long memberId;        // MEMBER_ID (FK)
    private Long caseId;          // CASE_ID (FK, 선택)
    private String title;         // TITLE
    private String content;       // CONTENT
    private String caseType;      // CASE_TYPE
    private Long assignedLawyer;  // ASSIGNED_LAWYER (FK)
    private String status;        // STATUS
    private Date createdAt;       // CREATED_AT
    private Date updatedAt;       // UPDATED_AT

    // 조인 및 통계용 필드
    private String memberName;    // 작성자 이름
    private int answerCount;      // 답변 개수 (목록/상세에서 활용)
    private int fileCount;        // 첨부파일 개수 (목록에서 활용)

    // 질문글에 포함된 첨부파일 목록
    private List<AttachmentVO> files;
}