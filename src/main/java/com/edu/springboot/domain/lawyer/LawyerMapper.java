/**
 * 파일위치: src/main/java/com/edu/springboot/domain/lawyer/LawyerMapper.java
 * 기능전체: TB_LAWYER 테이블에 접근하는 매퍼입니다. 
 * 용현님의 기존 조회 기능과 은혁님의 관리자 승인 로직을 통합 관리합니다.
 */
package com.edu.springboot.domain.lawyer;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.edu.springboot.domain.lawyer.vo.LawyerVO;

@Mapper
public interface LawyerMapper {

    // --- 용현님 기존 코드 유지 ---
    List<LawyerVO> selectAllLawyers();
    LawyerVO selectLawyerById(Long lawyerId);

    // --- 은혁님 관리자 파트 추가 (AdminController 오류 해결용) ---
    // 승인 대기 중인(PENDING) 전문회원 목록 조회
    List<LawyerVO> findPendingLawyers();

    // 전문회원의 승인 상태(APPROVE_STATUS) 업데이트
    int updateApproveStatus(LawyerVO lawyerVO);
}