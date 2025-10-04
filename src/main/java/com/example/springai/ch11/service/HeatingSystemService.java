package com.example.springai.ch11.service;

import com.example.springai.ch11.tools.HeatingSystemTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class HeatingSystemService {

    private final ChatClient chatClient;
    private final HeatingSystemTools heatingSystemTools;

    public HeatingSystemService(ChatClient.Builder chatClientBuilder, HeatingSystemTools heatingSystemTools) {
        this.chatClient = chatClientBuilder.build();
        this.heatingSystemTools = heatingSystemTools;
    }

    public String chat(String question) {
        String answer = chatClient.prompt()
                .system("""
                        현재 온도가 사용자가 원하는 온도 이상이라면 난방 시스템을 중지 하세요.
                        현재 온도가 사용자가 원하는 온도 이하라면 난방 시스템을 가동시켜 주세요.
                        """)
                .user(question)
                .tools(heatingSystemTools)
                .toolContext(Map.of("controlKey", "heatingSystemKey"))
//                .toolContext(Map.of("controlKey", "wrongKey"))
                .call()
                .content();

        return answer;
    }
}
