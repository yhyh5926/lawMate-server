package com.edu.springboot.domain.precedent;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/precedents")
@CrossOrigin(origins = "http://localhost:5173")
public class PrecedentController {

    @Autowired
    private PrecedentService precedentService;

    @GetMapping
    public Map<String, Object> list(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return precedentService.selectAll(page, size);
    }

    @GetMapping("/{id}")
    public PrecedentVO detail(@PathVariable("id") Long id) {
        return precedentService.selectOne(id);
    }
}