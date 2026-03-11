package com.edu.springboot.domain.answer;

import com.edu.springboot.domain.answer.vo.AnswerVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    /** 1. 특정 질문에 대한 답변 목록 조회 */
    @GetMapping("/list")
    public ResponseEntity<?> getAnswersByQuestionId(@RequestParam("questionId") Long questionId) {
        List<AnswerVO> list = answerService.getAnswersByQuestionId(questionId);
        return ResponseEntity.ok(Map.of("success", true, "data", list));
    }

    /** 2. 변호사 답변 등록 */
    @PostMapping("/write")
    public ResponseEntity<?> writeAnswer(@RequestBody AnswerVO answerVO) {
        // answerVO 내부에는 QUESTION_ID, LAWYER_ID, CONTENT 등이 포함되어야 함
        boolean success = answerService.registerAnswer(answerVO);
        return success 
            ? ResponseEntity.ok(Map.of("success", true, "message", "답변이 등록되었습니다."))
            : ResponseEntity.internalServerError().body(Map.of("success", false, "message", "등록 실패"));
    }

    /** 3. 변호사 답변 수정 */
    @PutMapping("/update/{answerId}")
    public ResponseEntity<?> updateAnswer(@PathVariable("answerId") Long answerId, @RequestBody AnswerVO answerVO) {
        answerVO.setAnswerId(answerId);
        boolean success = answerService.modifyAnswer(answerVO);
        return success 
            ? ResponseEntity.ok(Map.of("success", true, "message", "답변이 수정되었습니다."))
            : ResponseEntity.status(404).body(Map.of("success", false, "message", "수정 대상 없음"));
    }

    /** 4. 변호사 답변 삭제 */
    @DeleteMapping("/delete/{answerId}")
    public ResponseEntity<?> deleteAnswer(@PathVariable("answerId") Long answerId) {
        try {
            boolean success = answerService.removeAnswer(answerId);
            return success 
                ? ResponseEntity.ok(Map.of("success", true, "message", "삭제되었습니다."))
                : ResponseEntity.status(404).body(Map.of("success", false, "message", "삭제 실패"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /** 5. 변호사 답변 채택 (핵심 로직) */
    @PostMapping("/adopt")
    public ResponseEntity<?> adoptAnswer(@RequestBody Map<String, Object> data) {
        // 데이터 구조: { "answerId": 10, "questionId": 100 }
        try {
            boolean success = answerService.adoptAnswer(data);
            return success 
                ? ResponseEntity.ok(Map.of("success", true, "message", "답변이 채택되었습니다."))
                : ResponseEntity.badRequest().body(Map.of("success", false, "message", "채택 실패"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}