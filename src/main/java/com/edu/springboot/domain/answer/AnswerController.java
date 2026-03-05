package com.edu.springboot.domain.answer;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.springboot.domain.answer.vo.AnswerVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/question/answer")
@RequiredArgsConstructor
public class AnswerController {
	private final AnswerService answerService;

	@PostMapping("/write")
	public ResponseEntity<?> writeAnswer(@RequestBody AnswerVO answerVO) {

		boolean success = answerService.registerAnswer(answerVO);
		if (success) {
			return ResponseEntity.ok(Map.of("success", true, "message", "답변이 등록되었습니다."));
		} else {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "답변 등록에 실패했습니다."));
		}
	}

}
