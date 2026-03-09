package com.edu.springboot.domain.answer;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@PutMapping("/update/{answerId}")
	public ResponseEntity<?> updateAnswer(@PathVariable("answerId") long answerId, @RequestBody AnswerVO answerVO) {

		// VO에 PathVariable로 받은 ID 설정
		answerVO.setAnswerId(answerId);

		boolean success = answerService.modifyAnswer(answerVO);
		if (success) {
			return ResponseEntity.ok(Map.of("success", true, "message", "답변이 수정되었습니다."));
		} else {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "답변 수정에 실패했습니다."));
		}
	}

	@DeleteMapping("/delete/{answerId}")
	public ResponseEntity<?> deleteAnswer(@PathVariable("answerId") int answerId) {

		boolean success = answerService.removeAnswer(answerId);
		if (success) {
			return ResponseEntity.ok(Map.of("success", true, "message", "답변이 삭제되었습니다."));
		} else {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "답변 삭제에 실패했습니다."));
		}
	}
}
