package com.edu.springboot.domain.answer;

import com.edu.springboot.domain.answer.vo.AnswerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AnswerMapper {

    /** 1. 특정 질문에 대한 답변 목록 조회 (TB_ANSWER) */
    List<AnswerVO> selectAnswersByQuestionId(@Param("questionId") Long questionId);

    /** 2. 답변 단건 상세 조회 (채택 여부 및 작성자 확인용) */
    AnswerVO selectAnswerById(@Param("answerId") Long answerId);

    /** 3. 변호사 답변 등록 (TB_ANSWER) */
    int insertAnswer(AnswerVO answerVO);

    /** 4. 변호사 답변 수정 (TB_ANSWER) */
    int updateAnswer(AnswerVO answerVO);

    /** 5. 변호사 답변 삭제 */
    int deleteAnswer(@Param("answerId") Long answerId);

    /** * 6. 답변 채택 처리 (핵심 로직)
     * IS_ADOPTED를 'Y'로 변경하고 ADOPTED_AT을 SYSDATE로 업데이트
     */
    int updateAdoptStatus(@Param("answerId") Long answerId);
}