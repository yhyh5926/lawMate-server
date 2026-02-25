package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface QuestionMapper {
    int insertQuestion(QuestionVO question);
    List<QuestionVO> findAllQuestions(String caseType);
    QuestionVO findQuestionById(Long questionId);
}