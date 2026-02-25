package com.edu.springboot.domain.lawyer;

import com.edu.springboot.domain.lawyer.vo.LawyerVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface LawyerMapper {
    // 전문회원 정보 등록 (가입 시)
    int insertLawyer(LawyerVO lawyer);
    
    // 관리자: 승인 대기 중인 전문회원 목록 조회
    List<LawyerVO> findPendingLawyers();
    
    // 관리자: 전문회원 승인 상태 변경
    int updateApproveStatus(LawyerVO lawyer);
}