package com.example.springai.ch11.service;


import com.example.springai.ch11.tools.RecommendMovieTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecommendMovieService {

    private final ChatClient chatClient;
    private final RecommendMovieTools recommendMovieTools;

    public RecommendMovieService(ChatClient.Builder chatClientBuilder, RecommendMovieTools recommendMovieTools) {
        this.chatClient = chatClientBuilder.build();
        this.recommendMovieTools = recommendMovieTools;
    }

    public String chat(String question) {
        return chatClient.prompt()
                .user(question)
                .tools(recommendMovieTools)
                .call()
                .content();
    }

    public String exceptionChat(String question) {
        return chatClient.prompt()
                .user("""
                            질문에 대해 답변해 주세요.
                            사용자 ID가 존재하지 않을 경우, 진행을 멈추고,
                            '[LLM] 질문을 처리할수 없습니다.' 라고 답변 . 주세요.
                            
                            질문 : %s
                        """.formatted(question))
                .tools(recommendMovieTools)
                .call()
                .content();
    }

}
