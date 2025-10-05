package com.example.springai.ch11.service;

import com.example.springai.ch11.tools.SearchTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SearchService {

    private final ChatClient chatClient;
    private final SearchTools searchTools;

    public SearchService(ChatClient.Builder chatClientBuilder, SearchTools searchTools) {
        this.chatClient = chatClientBuilder.build();
        this.searchTools = searchTools;
    }

    public String chat(String question) {
        return chatClient.prompt()
                .system("""
                        HTML과 CSS를 사용해서 들여쓰기가 된 답변을 출력 하세요.
                        <div>에 들어가는 내용으로만 답변을 주세요. <h1>, <h2>, <h3> 태그는 사용하지 마세요.
                        """)
                .user(question)
                .tools(searchTools)
                .call()
                .content();
    }
}
