package com.example.springai.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AiServiceSelfConsistency {

    private final ChatClient chatClient;
    private final PromptTemplate promptTemplate = PromptTemplate.builder()
            .template("""
                    다음 내용을 [IMPORTANT, NOT_IMPORTANT] 둘 중 하나로 분류 해 주세요.
                    레이블만 변환 하세요.
                    내용 : {content}
                    """)
            .build();

    public AiServiceSelfConsistency(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String process(String content) {
        int importantCount = 0;
        int notImportantCount = 0;

        String userText = promptTemplate.render(Map.of("content", content));

        // 다섯 번에 걸쳐 응답 받아 오기
        for(int i = 0 ; i < 5; i++) {
            String output = chatClient.prompt()
                    .user(userText)
                    .options(ChatOptions.builder()
                            .build())
                    .call()
                    .content();

            log.info("{} :{}", i, output.toString());

            if(output.equals("IMPORTANT")) {
                importantCount++;
            } else {
                notImportantCount++;
            }
        }

        // 다수결로 최종 분류를 결정
        return importantCount > notImportantCount ? "중요함" : "중요하지 않음";
    }
}
