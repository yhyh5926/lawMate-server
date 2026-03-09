package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionService questionService;
	private final QuestionMapper questionMapper;

	/**
	 * 1. 질문 등록 프론트의 FormData 필드명(title, content, memberId)이 QuestionVO의 필드명과 일치해야
	 * 합니다.
	 */
	@PostMapping("/write")
	public ResponseEntity<?> writeQuestion(@ModelAttribute QuestionVO questionVO,
			// 💡 이름을 'files' 대신 'uploadFiles'로 변경하여 VO 내부의 files 필드와 충돌 방지
			@RequestParam(value = "uploadFiles", required = false) List<MultipartFile> files) {

		System.out.println("질문 등록 요청: " + questionVO.getTitle());
		boolean success = questionService.writeQuestion(questionVO, files);
		return ResponseEntity.ok(Map.of("success", success));
	}

	/**
	 * 2. 질문 수정
	 */
	@PutMapping("/update/{questionId}")
	public ResponseEntity<?> updateQuestion(@PathVariable("questionId") Long questionId,
			@ModelAttribute QuestionVO questionVO,
			@RequestParam(value = "files", required = false) List<MultipartFile> files) {

		questionVO.setQuestionId(questionId);
		boolean success = questionService.updateQuestion(questionVO, files);

		if (success) {
			return ResponseEntity.ok(Map.of("success", true));
		} else {
			return ResponseEntity.status(400).body(Map.of("success", false, "message", "수정이 불가능한 상태이거나 오류가 발생했습니다."));
		}
	}

	/**
	 * 3. 질문 삭제
	 */
	@DeleteMapping("/delete/{questionId}")
	public ResponseEntity<?> deleteQuestion(@PathVariable("questionId") Long questionId) {
		String result = questionService.deleteQuestion(questionId);
		if ("SUCCESS".equals(result)) {
			return ResponseEntity.ok(Map.of("success", true));
		}

		String msg = "HAS_ANSWERS".equals(result) ? "이미 답변이 달린 질문은 삭제할 수 없습니다." : "삭제 실패";
		return ResponseEntity.status(400).body(Map.of("success", false, "message", msg));
	}

	/**
	 * 4. 질문 목록 조회 (검색 및 페이징)
	 */
	@GetMapping("/list")
	public ResponseEntity<?> getQuestionList(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "caseType", required = false) String caseType,
			@RequestParam(value = "title", required = false) String title) {

		int offset = (page - 1) * size;
		Map<String, Object> params = new HashMap<>();
		params.put("offset", offset);
		params.put("size", size);
		params.put("caseType", caseType);
		params.put("title", title);

		List<QuestionVO> list = questionMapper.selectQuestionsWithPaging(params);
		int totalCount = questionMapper.selectQuestionCount(params);
		int totalPages = (int) Math.ceil((double) totalCount / size);

		return ResponseEntity
				.ok(Map.of("data", list, "totalCount", totalCount, "currentPage", page, "totalPages", totalPages));
	}

	/**
	 * 5. 질문 상세 조회
	 */
	@GetMapping("/detail")
	public ResponseEntity<?> getQuestionDetail(@RequestParam("questionId") Long questionId) {
		QuestionVO detail = questionMapper.selectQuestionById(questionId);
		if (detail == null) {
			return ResponseEntity.status(404).body(Map.of("success", false, "message", "존재하지 않는 게시글입니다."));
		}
		return ResponseEntity.ok(Map.of("data", detail));
	}

	/**
	 * 6. 답변 채택
	 */
	@PostMapping("/adopt")
	public ResponseEntity<?> adoptAnswer(@RequestBody Map<String, Object> params) {
		boolean success = questionService.adoptAnswer(params);
		return success ? ResponseEntity.ok(Map.of("success", true))
				: ResponseEntity.status(400).body(Map.of("success", false, "message", "채택에 실패했습니다."));
	}
}