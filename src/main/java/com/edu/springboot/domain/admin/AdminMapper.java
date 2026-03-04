// src/main/java/com/edu/springboot/domain/admin/AdminMapper.java
package com.edu.springboot.domain.admin;

import org.apache.ibatis.annotations.Mapper;
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
}