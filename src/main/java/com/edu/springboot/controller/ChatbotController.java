package com.edu.springboot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatbotController {

	private final ChatClient chatClient;

	public ChatbotController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}

	@PostMapping(value = "/ask", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> ask(@RequestParam("question") String question) {
		return chatClient.prompt().system("""
				당신은 LawMate의 법률 도우미 챗봇입니다.
				한국 법률에 관한 질문에 친절하고 명확하게 답변해주세요.
				답변은 간결하게 3~5문장 이내로 해주세요.
				전문 법률 상담이 필요한 경우 변호사 상담을 권유하세요.
				""").user(question)
				.options(ChatOptions.builder().model("gpt-4o").temperature(0.3).maxTokens(1000).build()).stream()
				.content();
	}
}