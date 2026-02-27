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

    public Map<String, Object> selectAll(int page, int size) {
        // 오라클 ROWNUM 방식 계산
        int start = (page - 1) * size + 1;
        int end = page * size;

        List<PrecedentVO> list = precedentMapper.getPrecedentList(start, end);
        int totalCount = precedentMapper.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("totalPages", totalPages);
        response.put("totalCount", totalCount);
        
        return response;
    }

    public PrecedentVO selectOne(Long precId) {
        return precedentMapper.getPrecedentDetail(precId);
    }
}