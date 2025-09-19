package com.example.springai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AiServiceCot {

    private final ChatClient chatClient;

    public AiServiceCot(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public Flux<String> process(String question) {
        Flux<String> answer = chatClient.prompt()
                .user("""
                        %s
                        한 걸음씩 생각해 봅시다.
                        
                        [예시]
                        질문: 제 동생이 2살일 때, 저는 그의 나이의 두 배 였어요.
                        지금 저는 40살인데, 제 동생은 몇 살일까요? 한 걸음씩 생각해 봅시다.
                        
                        답변: 제 동생이 2살일 때, 저는 2 * 2 = 4 살이었어요.
                        그 때부터 2년 차이가 나며, 제가 더 나이가 많습니다.
                        지금 저는 40살이니, 제 동생은 40-2 = 38살이에요. 정답은 38살 입니다.
                        """.formatted(question))
                .stream()
                .content();

        return answer;
    }
}
