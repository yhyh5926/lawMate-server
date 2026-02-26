/**
 * 파일위치: src/main/java/com/edu/springboot/domain/question/QuestionMapper.java
 * 기능전체: TB_QUESTION 테이블에 접근하여 법률 질문 데이터를 관리하는 매퍼 인터페이스입니다.
 */
package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface QuestionMapper {
    
    // 새 질문 등록
    int insertQuestion(QuestionVO questionVO);
    
    // 전체 질문 목록 조회
    List<QuestionVO> selectAllQuestions();
    
    // 질문 상세 조회
    QuestionVO selectQuestionById(Long questionId);
    
    // 질문 상태 업데이트 (OPEN -> ANSWERED -> CLOSED)
    int updateQuestionStatus(Long questionId, String status);
}