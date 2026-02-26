package com.edu.springboot.domain.lawyer;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;

@Mapper
public interface LawyerMapper {

	List<LawyerVO> selectAllLawyers();

	LawyerVO selectLawyerById(Long lawyerId);
}