package com.edu.springboot.domain.precedent;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/precedents")
@CrossOrigin(origins = "http://localhost:5173")
public class PrecedentController {

    @Autowired
    private PrecedentService precedentService;

    // 판례 목록 반환: GET /api/precedents
    @GetMapping
    public List<PrecedentVO> list() {
        return precedentService.selectAll();
    }

    // 💡 판례 상세 반환: GET /api/precedents/{id}
    @GetMapping("/{id}")
    public PrecedentVO detail(@PathVariable("id") Long id) { // 👈 ("id")를 명시적으로 추가.
        return precedentService.selectOne(id);
    }
}