package com.edu.springboot.domain.precedent;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/precedents")
@CrossOrigin(origins = "http://localhost:5173") 
public class PrecedentController {

	@Autowired
	private PrecedentService precedentService;

	/**
	 * 1. 판례 목록 조회 (페이징 + 키워드 검색 + 카테고리 필터)
	 * * @param page     현재 페이지 (기본값 1)
	 * @param size     페이지당 데이터 개수 (기본값 10)
	 * @param query    검색어 (제목, 사건번호 등 - 선택사항)
	 * @param caseType 카테고리 필터 (금융보험, 형사재산 등 - 선택사항)
	 */
	@GetMapping
	public Map<String, Object> list(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "caseType", required = false) String caseType) {

		// 키워드와 카테고리를 모두 서비스 계층으로 전달
		return precedentService.selectAll(page, size, query, caseType);
	}

	/**
	 * 2. 판례 상세 조회
	 * * @param id 판례 고유 ID (precId)
	 */
	@GetMapping("/{id}")
	public PrecedentVO detail(@PathVariable("id") Long id) {
		return precedentService.selectOne(id);
	}
}