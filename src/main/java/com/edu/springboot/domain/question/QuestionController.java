package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionMapper questionMapper;

	@PostConstruct
	public void init() {
		System.out.println("✅ [domain/question] 법률 질문 컨트롤러가 /api/question 경로로 활성화되었습니다.");
	}

	// 1. 새 질문 등록
	@PostMapping("/write")
	public ResponseEntity<?> writeQuestion(@RequestBody QuestionVO questionVO) {
		int result = questionMapper.insertQuestion(questionVO);
		return ResponseEntity.ok(Map.of("success", result > 0));
	}

	// 2. 전체 질문 목록 조회 (수정된 Mapper에 의해 작성자명, 답변수 포함)
	@GetMapping("/list")
	public ResponseEntity<?> getQuestionList() {
		return ResponseEntity.ok(Map.of("data", questionMapper.selectAllQuestions()));
	}

	// 3. 질문 상세 조회 (수정된 Mapper에 의해 질문자명, 답변 리스트 포함)
	@GetMapping("/detail")
	public ResponseEntity<?> getQuestionDetail(@RequestParam("questionId") Long questionId) {
		return ResponseEntity.ok(Map.of("data", questionMapper.selectQuestionById(questionId)));
	}

	/**
	 * 4. 변호사 답변 채택 API 기능: 질문 상태를 ADOPTED로 변경하고, 채택된 변호사 ID를 질문 테이블에 할당합니다.
	 * Transactional: 질문 업데이트와 답변 채택 상태 변경을 원자적으로 처리합니다.
	 */
	@Transactional
	@PostMapping("/adopt")
	public ResponseEntity<?> adoptAnswer(@RequestBody Map<String, Long> params) {
		Long questionId = params.get("questionId");
		Long lawyerId = params.get("lawyerId");
		Long memberId = params.get("memberId"); // 현재 로그인한 사용자 ID (작성자 확인용)
		Long answerId = params.get("answerId");

		// 1. 질문 테이블 업데이트 (상태 변경 및 변호사 할당)
		int qResult = questionMapper.updateQuestionAdoption(questionId, lawyerId, memberId);

		// 2. 답변 테이블 업데이트 (해당 답변 IS_ADOPTED = 'Y')
		int aResult = questionMapper.updateAnswerAdoption(answerId);

		if (qResult > 0 && aResult > 0) {
			return ResponseEntity.ok(Map.of("success", true, "message", "채택이 완료되었습니다."));
		} else {
			return ResponseEntity.status(400).body(Map.of("success", false, "message", "채택에 실패했습니다. 작성자 본인인지 확인하세요."));
		}
	}
}