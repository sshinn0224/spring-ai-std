package com.example.springai.ch11.service;

import com.example.springai.ch11.tools.DateTimeTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DateTimeService {

    private final ChatClient chatClient;
    private final DateTimeTools dateTimeTools;

    public DateTimeService(ChatClient.Builder chatClientBuilder, DateTimeTools dateTimeTools) {
        this.chatClient = chatClientBuilder
                .build();
        this.dateTimeTools = dateTimeTools;
    }

    public String chat(String question) {
        String answer = this.chatClient.prompt()
                .user(question)
                .tools(dateTimeTools)
                .call()
                .content();

        return answer;
    }
}
