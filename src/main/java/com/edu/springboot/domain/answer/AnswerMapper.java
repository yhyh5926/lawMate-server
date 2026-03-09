package com.edu.springboot.domain.answer;

import com.edu.springboot.domain.answer.vo.AnswerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AnswerMapper {

	int insertAnswer(AnswerVO answerVO);

	int updateAnswer(AnswerVO answerVO);

	int deleteAnswer(@Param("answerId") int answerId);
}