package com.edu.springboot.domain.answer;

import com.edu.springboot.domain.answer.vo.AnswerVO;
import com.edu.springboot.domain.question.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerMapper answerMapper;

	/**
	 * 답변 등록 로직 XML에서 서브쿼리(COUNT(*))로 개수를 계산하므로, 여기서는 답변만 저장하면 목록 조회 시 개수가 자동으로
	 * 반영됩니다.
	 */
	@Transactional
	public boolean registerAnswer(AnswerVO answerVO) {
		// 1. 답변 등록 (이것만으로 충분합니다)
		int result = answerMapper.insertAnswer(answerVO);

		// 2. 성공 여부 반환 (추후 알림 발송 로직 등을 여기에 추가 가능)
		return result > 0;
	}
}