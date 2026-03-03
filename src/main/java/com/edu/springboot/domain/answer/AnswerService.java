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
    private final QuestionMapper questionMapper;

    @Transactional
    public boolean registerAnswer(AnswerVO answerVO) {
        // 1. TB_ANSWER 테이블에 답변 저장
        int result = answerMapper.insertAnswer(answerVO);

        if (result > 0) {
            // 2. 답변 성공 시 TB_QUESTION의 상태를 'ANSWERED'로 업데이트
            questionMapper.updateQuestionStatus(answerVO.getQuestionId(), "ANSWERED");
            return true;
        }
        return false;
    }
}