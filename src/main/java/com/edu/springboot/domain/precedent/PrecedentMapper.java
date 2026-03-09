package com.edu.springboot.domain.precedent;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PrecedentMapper {
	/**
	 * 1. 판례 목록 조회 (페이징 + 키워드 검색 + 카테고리 필터)
	 */
	List<PrecedentVO> getPrecedentList(@Param("start") int start, @Param("end") int end, @Param("query") String query,
			@Param("caseType") String caseType);

	/**
	 * 2. 검색 조건에 따른 전체 데이터 개수 조회
	 */
	int getTotalCount(@Param("query") String query, @Param("caseType") String caseType);

	/**
	 * 3. 판례 상세 정보 조회
	 */
	PrecedentVO getPrecedentDetail(Long precId);

	/**
	 * 4. 유사 판례 추천 목록 조회
	 * 
	 * @param caseType  현재 판례의 카테고리 (필수)
	 * @param keyword   유사도 비교를 위한 핵심 단어 (선택)
	 * @param excludeId 현재 판례 제외용 ID (필수)
	 * @param limit     조회할 데이터 개수 (예: 4)
	 */
	List<PrecedentVO> getRelatedPrecedents(@Param("caseType") String caseType, @Param("keyword") String keyword,
			@Param("excludeId") Long excludeId, @Param("limit") int limit);
}