package com.example.springai.ch7.service;

import com.example.springai.ch7.advisor.MaxCharLengthAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiAdvisorService3 {

    private final ChatClient chatClient;

    public AiAdvisorService3(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new MaxCharLengthAdvisor(Ordered.HIGHEST_PRECEDENCE),
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
                )
                .build();
    }

    public String advisorLogging(String question) {
        log.info(question);
        return chatClient.prompt()
                .advisors(advisorSpec -> advisorSpec.param(MaxCharLengthAdvisor.MAX_CHAR_LENGTH, 100))
                .user(question)
                .call()
                .content();
    }


}
