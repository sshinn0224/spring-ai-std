package com.example.springai.ch3.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class AiServiceFewShotPrompt {

    private final ChatClient chatClient;

    public AiServiceFewShotPrompt(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String fewShotPrompt(String order) {
        String strPrompt = """
                고객 주문을 유효한 JSON 형식으로 바꿔주세요.
                추가 설명은 포함하지 마세요.
                응답을 JSON 형식으로 제공해주세요. 코드 블록(```)을 사용하지 말고 순수 JSON만 반환해주세요.
                
                예시1:
                작은 피자 하나, 치즈랑 토마토 소스, 페퍼로니 올려서 주세요.
                JSON 응답:
                {
                    "size":"small",
                    "type":"normal",
                    "ingredients":["cheese","tomato sauce","pepperoni"]
                }
                
                예시2:
                큰 피자 하나, 토마토 소스랑 바질, 모짜렐라 올려서 주세요.
                JSON 응답:
                {
                    "size":"large",
                    "type":"normal",
                    "ingredients":["tomato sauce","basil","mozzarella"]
                }
                
                고객 주문: %s""".formatted(order);

        Prompt prompt = Prompt.builder()
                .content(strPrompt)
                .build();

        // LLM 요청 후 받아옴
        return chatClient.prompt(prompt)
                .options(ChatOptions.builder()
                        .temperature(0.0)
                        .maxTokens(300)
                        .build())
                .call()
                .content();
    }
}
