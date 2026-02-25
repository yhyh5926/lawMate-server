package com.edu.springboot.domain.report;

import com.edu.springboot.domain.report.vo.ReportVO;
import com.edu.springboot.domain.report.vo.SanctionVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ReportMapper {
    List<ReportVO> findAllReports();
    ReportVO findReportById(Long reportId);
    int updateReportStatus(ReportVO report);
    int insertSanction(SanctionVO sanction);
}