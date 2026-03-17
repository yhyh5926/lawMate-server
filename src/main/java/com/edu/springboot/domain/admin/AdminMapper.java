// src/main/java/com/edu/springboot/domain/admin/AdminMapper.java
package com.edu.springboot.domain.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {

//	현재 서비스에 가입된 일반 회원의 총합을 조회하는 쿼리
	int countTotalPersonalMembers();

//	관리자 승인이 완료된 전문 회원(변호사)의 총합을 조회하는 쿼리
	int countTotalLawyerMembers();

//	최종적으로 답변이나 판결이 완료된 사건의 총 개수를 조회하는 쿼리
	int countCompletedCases();

//	대시보드 차트에 표시하기 위해 최근 5일 동안 하루에 가입한 회원 수를 집계하는 쿼리
	List<Map<String, Object>> getDailyMemberStats();

//	대시보드 차트에 표시하기 위해 최근 5일 동안 하루에 접수된 사건 수를 집계하는 쿼리
	List<Map<String, Object>> getDailyCaseStats();

//	전체 신고 내역을 VO 객체 없이 유연하게 Map 형태로 가져오는 쿼리
	List<Map<String, Object>> selectAllReports();

//	특정 신고 건의 상세 정보와 관련된 회원 데이터를 Map 형태로 가져오는 쿼리
	Map<String, Object> selectReportDetail(@Param("reportId") Long reportId);

//	결제자 정보와 결제 상태 등을 포함한 전체 결제 내역을 가져오는 쿼리
	List<Map<String, Object>> selectAllPayments();

//	변호사 정보와 수수료 등을 포함한 전체 정산 내역을 가져오는 쿼리
	List<Map<String, Object>> selectAllSettlements();
}