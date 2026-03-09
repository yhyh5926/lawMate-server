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
	 * 💡 3. 유사 판례 추천 조회 (수정됨) * @param caseType 현재 판례의 카테고리 (필수)
	 * 
	 * @param keyword   유사도 측정을 위한 핵심 키워드 (선택)
	 * @param excludeId 추천에서 제외할 ID (선택 - 질문 작성 시에는 null 가능)
	 * @param limit     가져올 개수 (기본값 4)
	 */
	@GetMapping("/related")
	public List<PrecedentVO> related(@RequestParam("caseType") String caseType,
			@RequestParam(value = "keyword", required = false) String keyword,
			// 💡 required = false를 추가하여 필수가 아니게 변경합니다.
			@RequestParam(value = "excludeId", required = false) Long excludeId,
			@RequestParam(value = "limit", defaultValue = "4") int limit) {

		// 만약 excludeId가 null이면 서비스나 매퍼에서 오류가 나지 않도록
		// 0 또는 시스템이 인식 가능한 더미 값으로 처리하거나, 그대로 보냅니다.
		long finalExcludeId = (excludeId == null) ? 0L : excludeId;

		return precedentService.selectRelated(caseType, keyword, finalExcludeId, limit);
	}
}