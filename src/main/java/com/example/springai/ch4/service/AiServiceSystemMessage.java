package com.example.springai.ch4.service;

import com.example.springai.ch4.service.dto.ReviewClassification;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;

@Service
public class AiServiceSystemMessage {

    private final ChatClient chatClient;

    public AiServiceSystemMessage(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public ReviewClassification process(String review) {
        ReviewClassification reviewClassification = chatClient.prompt()
                .system("""
                        영화 리뷰를 [POSITIVE, NEUTRAL, NEGATIVE] 중에서 하나로 분류하고,
                        유효한 JSON을 반환하세요.
                        """)
                .user("%s".formatted(review))
                .options(ChatOptions.builder()
                        .temperature(0.0)
                        .build())
                .call()
                .entity(ReviewClassification.class);

        return reviewClassification;
    }
}
