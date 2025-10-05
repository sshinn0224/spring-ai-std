package com.example.springai.ch11.service;

import com.example.springai.ch11.tools.FileSystemTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileSystemService {

    private final ChatClient chatClient;
    private final FileSystemTools fileSystemTools;

    public  FileSystemService(ChatClient.Builder chatClientBuilder,
                              FileSystemTools fileSystemTools,
                              ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem("""
                        HTML과 CSS를 사용하여 들여쓰기가 된 답변으로 출력 하세요.
                        <div>에 들어가는 내용으로만 답변을 주세요. <h1>, <h2> , <h3> 태그는 사용하지 마세요.
                        파일, 디렉토리 관련 질문은 반드시 도구를 사용 하세요.
                        """)
                .build();
        this.fileSystemTools = fileSystemTools;
    }

    public String chat(String question, String conversationId) {
        String answer = this.chatClient.prompt()
                .user(question)
                .advisors(advisorSpec -> {
                    advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId);
                })
                .tools(fileSystemTools)
                .call()
                .content();

        return answer;
    }
}
