package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

	@PostMapping("/write.do")
	public ResponseEntity<?> writeQuestion(@RequestBody QuestionVO questionVO) {
		int result = questionMapper.insertQuestion(questionVO);
		return ResponseEntity.ok(Map.of("success", result > 0));
	}

	@GetMapping("/list.do")
	public ResponseEntity<?> getQuestionList() {
		return ResponseEntity.ok(Map.of("data", questionMapper.selectAllQuestions()));
	}

	/**
	 * 💡 수정 포인트: @RequestParam에 명시적으로 "questionId"를 지정했습니다. 이를 통해 컴파일러 파라미터 정보가 없어도
	 * 정확히 매핑됩니다.
	 */
	@GetMapping("/detail.do")
	public ResponseEntity<?> getQuestionDetail(@RequestParam("questionId") Long questionId) {
		return ResponseEntity.ok(Map.of("data", questionMapper.selectQuestionById(questionId)));
	}
}