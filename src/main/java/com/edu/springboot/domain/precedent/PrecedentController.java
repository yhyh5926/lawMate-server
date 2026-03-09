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

	/**
	 * 1. 판례 목록 조회 (페이징 + 키워드 검색 + 카테고리 필터)
	 */
	@GetMapping
	public Map<String, Object> list(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "caseType", required = false) String caseType) {
		return precedentService.selectAll(page, size, query, caseType);
	}

	/**
	 * 2. 판례 상세 조회
	 */
	@GetMapping("/{id}")
	public PrecedentVO detail(@PathVariable("id") Long id) {
		return precedentService.selectOne(id);
	}

	/**
	 * 💡 3. 유사 판례 추천 조회 (추가됨)
	 * 
	 * @param caseType  현재 판례의 카테고리
	 * @param keyword   유사도 측정을 위한 핵심 키워드
	 * @param excludeId 현재 보고 있는 판례 ID (추천에서 제외)
	 * @param limit     가져올 개수 (기본값 4)
	 */
	@GetMapping("/related")
	public List<PrecedentVO> related(@RequestParam("caseType") String caseType,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam("excludeId") Long excludeId, @RequestParam(value = "limit", defaultValue = "4") int limit) {

		// 서비스 계층에서 유사 판례 리스트를 가져와 반환
		return precedentService.selectRelated(caseType, keyword, excludeId, limit);
	}
}