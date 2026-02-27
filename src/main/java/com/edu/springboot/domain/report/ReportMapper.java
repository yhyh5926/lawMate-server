/**
 * 파일위치: src/main/java/com/edu/springboot/domain/report/ReportMapper.java
 * 기능전체: TB_REPORT(신고) 및 TB_SANCTION(제재) 테이블과 연동되는 MyBatis 매퍼 인터페이스입니다.
 */
package com.edu.springboot.domain.report;

import com.edu.springboot.domain.report.vo.ReportVO;
import com.edu.springboot.domain.report.vo.SanctionVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ReportMapper {
    
    // [관리자] 전체 신고 접수 목록 조회 (AdminController 연동)
    List<ReportVO> findAllReports();
    
    // [사용자] 신고 내용 저장
    int insertReport(ReportVO reportVO);
    
    // [관리자] 신고 상태 변경 (PENDING -> RESOLVED/DISMISSED)
    int updateReportStatus(ReportVO reportVO);
    
    // [관리자] 제재 내역 등록 (TB_SANCTION)
    int insertSanction(SanctionVO sanctionVO);

    // 신고 상세 조회
    ReportVO findReportById(Long reportId);
}