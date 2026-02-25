package com.edu.springboot.domain.precedent;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrecedentService {
    
    @Autowired
    private PrecedentMapper precedentMapper;

    public List<PrecedentVO> selectAll() {
        return precedentMapper.getPrecedentList();
    }

    public PrecedentVO selectOne(Long precId) {
        return precedentMapper.getPrecedentDetail(precId);
    }
}