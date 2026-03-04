// src/main/java/com/edu/springboot/domain/lawyer/LawyerMapper.java
package com.edu.springboot.domain.lawyer;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;

@Mapper
public interface LawyerMapper {

    // --- 기존 조회 로직 ---
    List<LawyerVO> selectAllLawyers();
    LawyerVO selectLawyerById(Long lawyerId);
    List<LawyerVO> findPendingLawyers();
    int updateApproveStatus(LawyerVO lawyerVO);

    // 💡 에러 해결: 전문회원 가입 시 전문가 정보를 저장할 메서드 정의
    int insertLawyer(LawyerVO lawyerVO);
    
    //26.03.04 원석 추가
    LawyerVO selectLawyerByMemberId(Long memberId);
}