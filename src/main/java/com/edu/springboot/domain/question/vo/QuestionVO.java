/**
 * 파일위치: src/main/java/com/edu/springboot/domain/question/vo/QuestionVO.java
 */
package com.edu.springboot.domain.question.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;

import com.edu.springboot.domain.answer.vo.AnswerVO;

@Data
public class QuestionVO {
	private Long questionId; // QUESTION_ID (PK)
	private Long memberId; // MEMBER_ID (FK)
	private Long caseId; // CASE_ID (FK, 선택)
	private String title; // TITLE
	private String content; // CONTENT
	private String caseType; // CASE_TYPE
	private Long assignedLawyer; // ASSIGNED_LAWYER (FK)
	private String status; // STATUS
	private Date createdAt; // CREATED_AT
	private Date updatedAt; // UPDATED_AT

	// 💡 추가 필드: MyBatis selectAllQuestions 및 selectQuestionById 매핑용
	private String memberName; // SQL의 MEMBER_NAME 매핑
	private int answerCount; // SQL의 ANSWER_COUNT 매핑

	private List<AnswerVO> answers;
}