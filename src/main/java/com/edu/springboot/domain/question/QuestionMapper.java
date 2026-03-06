package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface QuestionMapper {

	// 1. 새 질문 등록
	int insertQuestion(QuestionVO questionVO);

	/**
	 * 2. [수정] 질문 목록 조회 (검색 및 페이징 반영)
	 * 
	 * @param params - { offset, size, caseType, title }
	 */
	List<QuestionVO> selectQuestionsWithPaging(Map<String, Object> params);

	/**
	 * 2-1. [추가] 조건에 맞는 질문 전체 개수 조회 (페이징 계산용)
	 * 
	 * @param params - { caseType, title }
	 */
	int selectQuestionCount(Map<String, Object> params);

	// 3. 질문 상세 조회
	QuestionVO selectQuestionById(Long questionId);

	/**
	 * 4. 질문 채택 업데이트 (상태를 'ADOPTED'로 변경하고 채택 변호사 지정)
	 */
	int updateQuestionAdoption(@Param("questionId") Long questionId, @Param("lawyerId") Long lawyerId,
			@Param("memberId") Long memberId);

	/**
	 * 5. 특정 답변 채택 상태 변경 (IS_ADOPTED = 'Y')
	 */
	int updateAnswerAdoption(Long answerId);

	/**
	 * 6. 답변 개수 1 증가
	 */
	int incrementAnswerCount(Long questionId);

	/**
	 * 7. 질문 상태 강제 변경 (관리자용)
	 */
	int updateQuestionStatus(@Param("questionId") Long questionId, @Param("status") String status);

	// 기존 메서드 (하위 호환성을 위해 유지하거나 제거 가능)
	List<QuestionVO> selectAllQuestions();
}