/**
 * 파일위치: src/main/java/com/edu/springboot/domain/question/QuestionController.java
 * 수정사항: @RequestMapping에 /api를 추가했습니다.
 */
package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/question") // 💡 /api 추가
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

    @GetMapping("/detail.do")
    public ResponseEntity<?> getQuestionDetail(@RequestParam Long questionId) {
        return ResponseEntity.ok(Map.of("data", questionMapper.selectQuestionById(questionId)));
    }
}