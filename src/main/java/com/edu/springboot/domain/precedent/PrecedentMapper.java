package com.edu.springboot.domain.precedent;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PrecedentMapper {
    /**
     * 1. 판례 목록 조회 (페이징 + 키워드 검색 + 카테고리 필터)
     * @param start    시작 행 번호 (Oracle ROWNUM용)
     * @param end      끝 행 번호 (Oracle ROWNUM용)
     * @param query    검색 키워드
     * @param caseType 카테고리 필터명
     */
    List<PrecedentVO> getPrecedentList(
        @Param("start") int start, 
        @Param("end") int end, 
        @Param("query") String query,
        @Param("caseType") String caseType // 👈 카테고리 파라미터 추가
    );

    /**
     * 2. 검색 조건에 따른 전체 데이터 개수 조회
     * @param query    검색 키워드
     * @param caseType 카테고리 필터명
     */
    int getTotalCount(
        @Param("query") String query,
        @Param("caseType") String caseType // 👈 카테고리 파라미터 추가
    );

    /**
     * 3. 판례 상세 정보 조회
     * @param precId 판례 고유 ID
     */
    PrecedentVO getPrecedentDetail(Long precId);
}