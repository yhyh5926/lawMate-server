package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionMapper questionMapper;

	@PostConstruct
	public void init() {
		System.out.println("✅ [domain/question] 법률 질문 컨트롤러가 활성화되었습니다.");
	}

	// 등록
	@PostMapping("/write")
	public ResponseEntity<?> writeQuestion(@RequestBody QuestionVO questionVO) {
		int result = questionMapper.insertQuestion(questionVO);
		return ResponseEntity.ok(Map.of("success", result > 0));
	}

	// 수정
	@Transactional
	@PutMapping("/update")
	public ResponseEntity<?> updateQuestion(@RequestBody QuestionVO questionVO) {
		// 이미 채택된 질문인지 체크하는 로직이 필요할 경우 추가
		// QuestionVO existingQuestion =
		// questionMapper.selectQuestionById(questionVO.getQuestionId());
		// if ("ADOPTED".equals(existingQuestion.getStatus())) {
		// return ResponseEntity.status(400).body(Map.of("success", false, "message",
		// "채택된 질문은 수정할 수 없습니다."));
		// }

		int result = questionMapper.updateQuestion(questionVO);

		if (result > 0) {
			return ResponseEntity.ok(Map.of("success", true, "message", "수정되었습니다."));
		} else {
			return ResponseEntity.status(400).body(Map.of("success", false, "message", "수정에 실패했습니다."));
		}
	}

	//삭제
	@Transactional
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteQuestion(@RequestParam("questionId") Long questionId) {
		// 상세 조회를 통해 답변 존재 여부 확인 (Mapper에서 처리 가능)
		QuestionVO question = questionMapper.selectQuestionById(questionId);

		if (question == null) {
			return ResponseEntity.status(404).body(Map.of("success", false, "message", "존재하지 않는 질문입니다."));
		}

		// 답변이 이미 달린 경우 삭제 방지 (프론트엔드에서도 체크하지만 서버에서도 한 번 더 체크)
		if (question.getAnswers() != null && !question.getAnswers().isEmpty()) {
			return ResponseEntity.status(400).body(Map.of("success", false, "message", "이미 답변이 달린 질문은 삭제할 수 없습니다."));
		}

		int result = questionMapper.deleteQuestion(questionId);

		if (result > 0) {
			return ResponseEntity.ok(Map.of("success", true, "message", "삭제되었습니다."));
		} else {
			return ResponseEntity.status(400).body(Map.of("success", false, "message", "삭제에 실패했습니다."));
		}
	}

	/**
	 * 2. 질문 목록 조회 (검색 및 페이지네이션 반영)
	 * 
	 * @param page:     현재 페이지 (1부터 시작)
	 * @param size:     한 페이지당 게시물 수
	 * @param caseType: 사건 유형 필터
	 * @param title:    제목 검색어
	 */
	@GetMapping("/list")
	public ResponseEntity<?> getQuestionList(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "caseType", required = false) String caseType,
			@RequestParam(value = "title", required = false) String title) {

		// MyBatis 페이징 처리를 위한 시작 위치 계산 (offset)
		int offset = (page - 1) * size;

		Map<String, Object> params = new HashMap<>();
		params.put("offset", offset);
		params.put("size", size);
		params.put("caseType", caseType);
		params.put("title", title);

		// 검색 조건에 맞는 데이터 리스트와 전체 개수 조회
		List<QuestionVO> list = questionMapper.selectQuestionsWithPaging(params);
		int totalCount = questionMapper.selectQuestionCount(params);

		return ResponseEntity.ok(Map.of("data", list, "totalCount", totalCount, "currentPage", page, "totalPages",
				(int) Math.ceil((double) totalCount / size)));
	}

	// 3. 질문 상세 조회
	@GetMapping("/detail")
	public ResponseEntity<?> getQuestionDetail(@RequestParam("questionId") Long questionId) {
		return ResponseEntity.ok(Map.of("data", questionMapper.selectQuestionById(questionId)));
	}

	/**
	 * 4. 변호사 답변 채택
	 */
	@Transactional
	@PostMapping("/adopt")
	public ResponseEntity<?> adoptAnswer(@RequestBody Map<String, Object> params) {
		// Map에서 값을 꺼낼 때 타입 안정성을 위해 체크 필요
		Long questionId = Long.valueOf(params.get("questionId").toString());
		Long lawyerId = Long.valueOf(params.get("lawyerId").toString());
		Long memberId = Long.valueOf(params.get("memberId").toString());
		Long answerId = Long.valueOf(params.get("answerId").toString());

		int qResult = questionMapper.updateQuestionAdoption(questionId, lawyerId, memberId);
		int aResult = questionMapper.updateAnswerAdoption(answerId);

		if (qResult > 0 && aResult > 0) {
			return ResponseEntity.ok(Map.of("success", true, "message", "채택이 완료되었습니다."));
		} else {
			return ResponseEntity.status(400).body(Map.of("success", false, "message", "채택에 실패했습니다."));
		}
	}
}