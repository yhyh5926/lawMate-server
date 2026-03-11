package com.edu.springboot.domain.answer;

import com.edu.springboot.domain.answer.vo.AnswerVO;
import com.edu.springboot.domain.question.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerMapper answerMapper;
	private final QuestionMapper questionMapper; // 질문 상태 변경을 위해 주입 필요

	/**
	 * 1. 특정 질문의 답변 목록 조회
	 */
	public List<AnswerVO> getAnswersByQuestionId(Long questionId) {
		return answerMapper.selectAnswersByQuestionId(questionId);
	}

	/**
	 * 2. 답변 등록
	 */
	@Transactional
	public boolean registerAnswer(AnswerVO answerVO) {
		int result = answerMapper.insertAnswer(answerVO);
		// 답변 등록 시 필요하다면 TB_QUESTION의 STATUS를 'ANSWERED'로 변경하는 로직 추가 가능
		return result > 0;
	}

	/**
	 * 3. 답변 수정
	 */
	@Transactional
	public boolean modifyAnswer(AnswerVO answerVO) {
		// 수정 전 채택 여부 확인 (채택된 답변은 수정 불가 처리 권장)
		AnswerVO existing = answerMapper.selectAnswerById(answerVO.getAnswerId());
		if (existing != null && "Y".equals(existing.getIsAdopted())) {
			throw new IllegalStateException("이미 채택된 답변은 수정할 수 없습니다.");
		}

		int result = answerMapper.updateAnswer(answerVO);
		return result > 0;
	}

	/**
	 * 4. 답변 삭제
	 */
	@Transactional
	public boolean removeAnswer(Long answerId) {
		AnswerVO answer = answerMapper.selectAnswerById(answerId);

		// [검증] 채택된 답변은 삭제 불가 (TB_ANSWER.IS_ADOPTED 체크)
		if (answer != null && "Y".equals(answer.getIsAdopted())) {
			throw new IllegalStateException("채택된 답변은 삭제할 수 없습니다.");
		}

		int result = answerMapper.deleteAnswer(answerId);
		return result > 0;
	}

	/**
	 * 5. 답변 채택 (핵심 비즈니스 로직) TB_ANSWER와 TB_QUESTION 두 테이블을 동시에 업데이트합니다.
	 */
	@Transactional
	public boolean adoptAnswer(Map<String, Object> params) {
		try {
			// 1. 파라미터 추출
			Long questionId = Long.valueOf(params.get("questionId").toString());
			Long answerId = Long.valueOf(params.get("answerId").toString());
			Long lawyerId = Long.valueOf(params.get("lawyerId").toString()); // 💡 채택된 변호사 ID

			// 2. 답변 상태를 'Y'로 변경 (채택 처리)
			int aResult = answerMapper.updateAdoptStatus(answerId);

			// 3. 질문 상태를 'ADOPTED'로 변경하고 담당 변호사 지정

			int qResult = questionMapper.updateQuestionStatus(questionId, "ADOPTED", lawyerId);

			return aResult > 0 && qResult > 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("채택 처리 실패"); // 트랜잭션 롤백을 위해 런타임 예외 발생
		}
	}
}