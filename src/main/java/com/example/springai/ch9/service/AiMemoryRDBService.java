//package com.example.springai.ch9.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
//import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.memory.MessageWindowChatMemory;
//import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
//import org.springframework.stereotype.Service;
//
////@Service
//@Slf4j
//public class AiMemoryRDBService {
//
//    private final ChatClient chatClient;
//
//    public AiMemoryRDBService(JdbcChatMemoryRepository chatMemoryRepository,
//                              ChatClient.Builder chatClientBuilder) {
//
//        ChatMemory chatMemory = MessageWindowChatMemory.builder()
//                .chatMemoryRepository(chatMemoryRepository)
//                .maxMessages(20)
//                .build();
//
//        this.chatClient = chatClientBuilder
//                .defaultAdvisors(
//                        PromptChatMemoryAdvisor.builder(chatMemory).build(),
//                        new SimpleLoggerAdvisor()
//                )
//                .build();
//    }
//
//    public String chat(String userText, String conversationId) {
//        String answer = chatClient.prompt()
//                .user(userText)
//                .advisors(advisorSpec -> advisorSpec.param(
//                        ChatMemory.CONVERSATION_ID, conversationId
//                ))
//                .call()
//                .content();
//
//        return answer;
//    }
//}
