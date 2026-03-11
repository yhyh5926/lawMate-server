package com.edu.springboot.domain.answer;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edu.springboot.domain.answer.vo.AnswerVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/question/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    /**
     * 💡 [추가] 특정 질문의 답변 목록 조회
     * React의 QuestionAnswerList 컴포넌트 마운트 시 호출됨
     */
    @GetMapping("/list")
    public ResponseEntity<?> getAnswerList(@RequestParam("questionId") Long questionId) {
        List<AnswerVO> list = answerService.getAnswersByQuestionId(questionId);
        // 클라이언트가 다루기 편하게 data 키에 담아 반환
        return ResponseEntity.ok(Map.of("success", true, "data", list));
    }

    /**
     * 답변 등록
     */
    @PostMapping("/write")
    public ResponseEntity<?> writeAnswer(@RequestBody AnswerVO answerVO) {
        boolean success = answerService.registerAnswer(answerVO);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true, "message", "답변이 등록되었습니다."));
        } else {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "답변 등록에 실패했습니다."));
        }
    }

    /**
     * 답변 수정
     */
    @PutMapping("/update/{answerId}")
    public ResponseEntity<?> updateAnswer(@PathVariable("answerId") long answerId, @RequestBody AnswerVO answerVO) {
        // VO에 URL Path로 받은 ID 설정
        answerVO.setAnswerId(answerId);

        boolean success = answerService.modifyAnswer(answerVO);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true, "message", "답변이 수정되었습니다."));
        } else {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "답변 수정에 실패했습니다."));
        }
    }

    /**
     * 답변 삭제
     */
    @DeleteMapping("/delete/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable("answerId") long answerId) {
        try {
            boolean success = answerService.removeAnswer(answerId);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "답변이 삭제되었습니다."));
            } else {
                return ResponseEntity.status(500).body(Map.of("success", false, "message", "답변 삭제에 실패했습니다."));
            }
        } catch (IllegalStateException e) {
            // 서비스에서 던진 "채택된 답변 삭제 불가" 예외 처리
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}