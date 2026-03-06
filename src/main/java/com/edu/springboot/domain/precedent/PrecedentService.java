package com.edu.springboot.domain.precedent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrecedentService {

    @Autowired
    private PrecedentMapper precedentMapper;

    /**
     * 판례 목록 조회 (검색어 + 카테고리 필터 포함)
     */
    public Map<String, Object> selectAll(int page, int size, String query, String caseType) {
        // 1. 오라클 ROWNUM 방식 페이징 계산
        int start = (page - 1) * size + 1;
        int end = page * size;

        // 2. 검색어 및 카테고리와 함께 데이터 조회
        // (query나 caseType이 빈 문자열인 경우 MyBatis <if>문에서 처리됨)
        List<PrecedentVO> list = precedentMapper.getPrecedentList(start, end, query, caseType);
        
        // 3. 필터링된 조건에 맞는 전체 게시글 수 조회
        int totalCount = precedentMapper.getTotalCount(query, caseType);
        
        // 4. 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / size);

        // 5. 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("totalPages", totalPages);
        response.put("totalCount", totalCount);

        return response;
    }

    /**
     * 판례 상세 조회
     */
    public PrecedentVO selectOne(Long precId) {
        return precedentMapper.getPrecedentDetail(precId);
    }
}