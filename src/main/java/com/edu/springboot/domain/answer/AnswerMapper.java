package com.edu.springboot.domain.answer;

import com.edu.springboot.domain.answer.vo.AnswerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AnswerMapper {

	/**
	 * 1. 특정 질문에 대한 답변 목록 조회 (변호사 정보 및 채택 통계 포함 - AnswerMapper.xml에서 처리)
	 */
	List<AnswerVO> selectAnswersByQuestionId(@Param("questionId") Long questionId);

	/**
	 * 2. 답변 단건 상세 조회 (삭제 전 채택 여부 확인 및 수정 시 검증용)
	 */
	AnswerVO selectAnswerById(@Param("answerId") Long answerId);

	/**
	 * 3. 답변 등록
	 */
	int insertAnswer(AnswerVO answerVO);

	/**
	 * 4. 답변 수정
	 */
	int updateAnswer(AnswerVO answerVO);

	/**
	 * 5. 답변 삭제
	 */
	int deleteAnswer(@Param("answerId") Long answerId);
}