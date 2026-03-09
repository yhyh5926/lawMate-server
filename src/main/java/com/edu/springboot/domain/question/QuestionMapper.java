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

    // 2. 질문 목록 조회 (검색 및 페이징 반영)
    List<QuestionVO> selectQuestionsWithPaging(Map<String, Object> params);

    // 3. 조건에 맞는 질문 전체 개수 조회 (페이징 계산용)
    int selectQuestionCount(Map<String, Object> params);

    // 4. 질문 상세 조회
    QuestionVO selectQuestionById(Long questionId);

    // 5. 질문 채택 업데이트 (상태를 'ADOPTED'로 변경하고 채택 변호사 지정)
    int updateQuestionAdoption(
        @Param("questionId") Long questionId, 
        @Param("lawyerId") Long lawyerId,
        @Param("memberId") Long memberId
    );

    // 6. 답변 채택 업데이트 (답변의 상태를 채택됨으로 변경)
    int updateAnswerAdoption(Long answerId);

    // 7. [관리자용] 전체 질문 목록 조회
    List<QuestionVO> selectAllQuestions();

    // 8. [관리자용] 질문 상태 변경 (게시글 숨김/삭제 처리용)
    int updateQuestionStatus(
        @Param("questionId") Long questionId, 
        @Param("status") String status
    );
}