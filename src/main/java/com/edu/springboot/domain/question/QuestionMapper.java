package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface QuestionMapper {

    /** 1. 질문 등록 */
    int insertQuestion(QuestionVO questionVO);

    /** 2. 질문 수정 (내용, 제목 등) */
    int updateQuestion(QuestionVO questionVO);

    /** 3. 질문 삭제 */
    int deleteQuestion(Long questionId);

    /** 4. 질문 상세 조회 (첨부파일 포함) */
    QuestionVO selectQuestionById(Long questionId);

    /** 5. 질문 목록 조회 (페이징/검색) */
    List<QuestionVO> selectQuestionsWithPaging(Map<String, Object> params);

    /** 6. 전체 질문 개수 조회 (페이징용) */
    int selectQuestionCount(Map<String, Object> params);

    /** * 7. 질문 상태 및 담당 변호사 업데이트 (채택 시 사용) 
     * XML의 <if test="lawyerId != null"> 로직과 매칭됩니다.
     */
    int updateQuestionStatus(
        @Param("questionId") Long questionId, 
        @Param("status") String status, 
        @Param("lawyerId") Long lawyerId
    );

    /** [관리자용] 전체 질문 목록 조회 */
    List<QuestionVO> selectAllQuestions();

    /** * 기존에 있던 updateQuestionAdoption은 
     * 위 updateQuestionStatus(..., "ADOPTED", lawyerId)로 통합하여 사용 가능하므로 
     * 유지하거나 필요에 따라 제거하셔도 됩니다.
     */
    int updateQuestionAdoption(
        @Param("questionId") Long questionId, 
        @Param("lawyerId") Long lawyerId,
        @Param("memberId") Long memberId
    );
}