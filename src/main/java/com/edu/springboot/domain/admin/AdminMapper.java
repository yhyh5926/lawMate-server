package com.edu.springboot.domain.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {

	// KPI 카드용 통계
	int countTotalPersonalMembers(); // 일반회원 총 가입 수

	int countTotalLawyerMembers(); // 승인된 전문회원 수

	int countCompletedCases(); // 해결 완료된 사건 수

	// 차트용 날짜별 통계 (최근 5일간)
	List<Map<String, Object>> getDailyMemberStats(); // 일별 가입자 수

	List<Map<String, Object>> getDailyCaseStats(); // 일별 접수 사건 수

	// 💡 [추가] 미구현 기능 DB 직접 조회 (VO 없이 Map으로 유연하게 반환)
	List<Map<String, Object>> selectAllReports();

	Map<String, Object> selectReportDetail(@Param("reportId") Long reportId);

	List<Map<String, Object>> selectAllPayments();

	List<Map<String, Object>> selectAllSettlements();
}