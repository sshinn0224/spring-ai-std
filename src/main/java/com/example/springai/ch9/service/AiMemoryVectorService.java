package com.example.springai.ch9.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiMemoryVectorService {

    private final ChatClient chatClient;

    public AiMemoryVectorService(JdbcTemplate jdbcTemplate,
                                 EmbeddingModel embeddingModel,
                                 ChatClient.Builder chatClientBuilder) {
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate , embeddingModel)
                .initializeSchema(false)
                .schemaName("public")
                .vectorTableName("chat_memory_vector_store")
                .dimensions(1536)
                .build();

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        VectorStoreChatMemoryAdvisor.builder(vectorStore).build(),
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
