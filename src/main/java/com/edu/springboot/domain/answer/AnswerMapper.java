package com.edu.springboot.domain.answer;

import com.edu.springboot.domain.answer.vo.AnswerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnswerMapper {
	/**
	 * 답변 등록
	 * 
	 * @param answerVO 답변 데이터
	 * @return 영향받은 행 수
	 */
	int insertAnswer(AnswerVO answerVO);
}