package com.example.springai.ch7.service;

import com.example.springai.ch7.advisor.AdvisorA;
import com.example.springai.ch7.advisor.AdvisorB;
import com.example.springai.ch7.advisor.AdvisorC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class AiAdvisorService1 {

    private final ChatClient chatClient;
    private final ChatMemoryRepository chatMemoryRepository;

    public AiAdvisorService1(ChatClient.Builder chatClientBuilder, ChatMemoryRepository chatMemoryRepository) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new AdvisorA(),
                        new AdvisorB()
                )
                .build();
        this.chatMemoryRepository = chatMemoryRepository;
    }

    public String advisorChain1(String question) {
        String response = chatClient.prompt()
                .advisors(new AdvisorC())
                .user(question)
                .call()
                .content();

        return response;
    }

    public Flux<String> advisorChain2(String question) {
        Flux<String> response = chatClient.prompt()
                .advisors(new AdvisorC())
                .user(question)
                .stream()
                .content();

        return response;
    }
}
