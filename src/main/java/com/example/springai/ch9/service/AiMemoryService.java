package com.example.springai.ch9.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiMemoryService {

    public final ChatClient chatClient;

    public AiMemoryService(ChatMemory chatMemory,
                           ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
//                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        PromptChatMemoryAdvisor.builder(chatMemory).build(),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    public String chat(String userText, String conversationId) {
        String answer = chatClient.prompt()
                .user(userText)
                .advisors(advisorSpec -> advisorSpec.param(
                        ChatMemory.CONVERSATION_ID, conversationId
                ))
                .call()
                .content();

        return answer;
    }

}
