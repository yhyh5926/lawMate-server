/**
 * 파일위치: src/main/java/com/edu/springboot/domain/question/QuestionMapper.java
 */
package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface QuestionMapper {

	// 1. 새 질문 등록
	int insertQuestion(QuestionVO questionVO);

	// 2. 전체 질문 목록 조회 (작성자 이름, 답변 개수 포함)
	List<QuestionVO> selectAllQuestions();

	// 3. 질문 상세 조회 (작성자 이름, 답변 리스트, 답변자 이름 포함)
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
	 * 6. [추가] 답변 개수 1 증가 AnswerService.registerAnswer 성공 시 호출됨
	 */
	int incrementAnswerCount(Long questionId);

	/**
	 * 7. [추가] 질문 상태 강제 변경 (관리자용) AdminController.deleteBoardItem에서 'CLOSED' 등으로 변경
	 * 시 사용
	 */
	int updateQuestionStatus(@Param("questionId") Long questionId, @Param("status") String status);
}