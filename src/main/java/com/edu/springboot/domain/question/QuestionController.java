/**
 * 파일위치: src/main/java/com/edu/springboot/domain/question/QuestionController.java
 * 기능전체: 사용자의 법률 질문 등록, 목록 조회 및 상세 내용을 처리하는 컨트롤러입니다.
 */
package com.edu.springboot.domain.question;

import com.edu.springboot.domain.question.vo.QuestionVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionMapper questionMapper;

    @PostConstruct
    public void init() {
        // 서버 실행 시 폴더별 작동 확인 로그 (박은혁 담당)
        System.out.println("✅ [domain/question] 법률 질문 관리 모듈이 정상 작동 중입니다.");
    }

    // 1. 법률 질문 등록
    @PostMapping("/write.do")
    public ResponseEntity<?> writeQuestion(@RequestBody QuestionVO questionVO) {
        int result = questionMapper.insertQuestion(questionVO);
        return ResponseEntity.ok(Map.of("success", result > 0));
    }

    // 2. 법률 질문 목록 조회
    @GetMapping("/list.do")
    public ResponseEntity<?> getQuestionList() {
        return ResponseEntity.ok(Map.of("data", questionMapper.selectAllQuestions()));
    }

    // 3. 법률 질문 상세 조회
    @GetMapping("/detail.do")
    public ResponseEntity<?> getQuestionDetail(@RequestParam Long questionId) {
        return ResponseEntity.ok(Map.of("data", questionMapper.selectQuestionById(questionId)));
    }
}