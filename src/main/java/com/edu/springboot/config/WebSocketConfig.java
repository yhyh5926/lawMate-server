package com.edu.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // 추가 또는 유지
@EnableWebSocketMessageBroker // 추가됨: 이 어노테이션이 핵심입니다.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer { // 수정됨

// 기존 코드가 있었다면 아래에 주석으로 남깁니다 (예상되는 기존 코드 형태)
// public class WebSocketConfig {
// }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) { // 추가됨
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // 추가됨
        registry.addEndpoint("/ws-stomp") 
                .setAllowedOriginPatterns("*") 
                .withSockJS();
    }
}