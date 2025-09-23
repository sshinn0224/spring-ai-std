package com.example.springai.ch7.service;

import com.example.springai.ch7.advisor.MaxCharLengthAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiAdvisorService2 {
    private ChatClient chatClient;

    public AiAdvisorService2(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new MaxCharLengthAdvisor(Ordered.HIGHEST_PRECEDENCE))
                .build();
    }

    public String advisorContext(String question) {
        String response = chatClient.prompt()
//                .advisors(advisorSpec -> advisorSpec.param(MaxCharLengthAdvisor.MAX_CHAR_LENGTH, 100))
                .user(question)
                .call()
                .content();

        return response;
    }
}
