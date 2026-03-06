// src/main/java/com/edu/springboot/domain/lawyer/LawyerMapper.java
package com.edu.springboot.domain.lawyer;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;

@Mapper
public interface LawyerMapper {

    List<LawyerVO> selectAllLawyers();
    LawyerVO selectLawyerById(Long lawyerId);
    List<LawyerVO> findPendingLawyers();
    int updateApproveStatus(LawyerVO lawyerVO);

    int insertLawyer(LawyerVO lawyerVO);
    
    //26.03.04 원석 추가
    LawyerVO selectLawyerByMemberId(Long memberId);

    int updateLawyerProfile(LawyerVO lawyerVO);
}