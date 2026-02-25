package com.edu.springboot.domain.precedent;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrecedentMapper {
    // 전체 목록 조회
    List<PrecedentVO> getPrecedentList();
    
    // 상세 정보 조회
    PrecedentVO getPrecedentDetail(Long precId);
}