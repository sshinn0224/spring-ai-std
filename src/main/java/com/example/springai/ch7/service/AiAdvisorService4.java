package com.example.springai.ch7.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
public class AiAdvisorService4 {

    public final ChatClient chatClient;

    public AiAdvisorService4(ChatClient.Builder chatClient) {
        SafeGuardAdvisor safeGuardAdvisor = new SafeGuardAdvisor(
                List.of("욕설","계좌번호","폭력","폭탄"),
                "해당 질문은 민감한 콘텐츠 요청이므로 응답할 수 없습니다.",
                Ordered.HIGHEST_PRECEDENCE
        );

        this.chatClient = chatClient
                .defaultAdvisors(safeGuardAdvisor)
                .build();
    }

    public String advisorSafeGuard(String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }

}
