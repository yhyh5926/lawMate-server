package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionMapper questionMapper;

    // 질문 등록
    @PostMapping("/question/write.do")
    public ResponseEntity<?> writeQuestion(@RequestBody QuestionVO questionVO) {
        questionMapper.insertQuestion(questionVO);
        return ResponseEntity.ok(Map.of("message", "질문이 등록되었습니다."));
    }

    // 질문 목록 (필터 포함)
    @GetMapping("/question/list.do")
    public ResponseEntity<?> getQuestionList(@RequestParam(required = false) String caseType) {
        List<QuestionVO> questions = questionMapper.findAllQuestions(caseType);
        return ResponseEntity.ok(Map.of("data", questions));
    }

    // 질문 상세
    @GetMapping("/question/detail.do")
    public ResponseEntity<?> getQuestionDetail(@RequestParam Long questionId) {
        QuestionVO question = questionMapper.findQuestionById(questionId);
        return ResponseEntity.ok(Map.of("data", question));
    }
}