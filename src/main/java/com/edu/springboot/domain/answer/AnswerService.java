package com.edu.springboot.domain.answer;

import com.edu.springboot.domain.answer.vo.AnswerVO;
import com.edu.springboot.domain.question.QuestionMapper;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerMapper answerMapper;

	/**
	 * 0. 특정 질문의 답변 목록 조회 QuestionAnswerList 컴포넌트에서 호출하는 핵심 로직
	 */
	public List<AnswerVO> getAnswersByQuestionId(Long questionId) {
		return answerMapper.selectAnswersByQuestionId(questionId);
	}

	/**
	 * 1. 답변 등록 XML에서 서브쿼리로 개수를 계산하므로, 저장만으로 목록에 자동 반영됩니다.
	 */
	@Transactional
	public boolean registerAnswer(AnswerVO answerVO) {
		int result = answerMapper.insertAnswer(answerVO);
		return result > 0;
	}

	/**
	 * 2. 답변 수정 작성자 본인 확인 로직은 컨트롤러나 Security 수준에서 처리하거나, 여기서 answerId로 기존 답변을 조회하여
	 * 검증 로직을 추가할 수 있습니다.
	 */
	@Transactional
	public boolean modifyAnswer(AnswerVO answerVO) {
		int result = answerMapper.updateAnswer(answerVO);
		return result > 0;
	}

	/**
	 * 3. 답변 삭제 채택된 답변인 경우 삭제를 막는 로직 등을 추가하기에 적합한 위치입니다.
	 */
	@Transactional
	public boolean removeAnswer(Long answerId) {
		// 💡 [검증] 채택된 답변은 삭제할 수 없도록 방어 로직 추가
		AnswerVO answer = answerMapper.selectAnswerById(answerId);
		if (answer != null && "Y".equals(answer.getIsAdopted())) {
			throw new IllegalStateException("채택된 답변은 삭제할 수 없습니다.");
		}

		int result = answerMapper.deleteAnswer(answerId);
		return result > 0;
	}
}