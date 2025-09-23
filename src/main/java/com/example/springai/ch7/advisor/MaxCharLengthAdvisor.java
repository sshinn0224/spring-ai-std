package com.example.springai.ch7.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

@Slf4j
public class MaxCharLengthAdvisor implements CallAdvisor {
    public static final String MAX_CHAR_LENGTH = "max-char-length";
    private int maxCharLength = 300;
    private int order;

    public MaxCharLengthAdvisor(int order) {
        this.order = order;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        log.info("전처리 작업 시작");
        // 전처리 작업
        ChatClientRequest mutatedRequest = augementPrompt(request);
        // 다음 Advisor 호출 또는 LLM 요청
        ChatClientResponse response = chain.nextCall(mutatedRequest);

        return response;
    }

    // 사용자 메시지 강화
    private ChatClientRequest augementPrompt(ChatClientRequest request) {
        // 추가할 사용자 텍스트 얻기
        String userText = this.maxCharLength + "자 이내로 답변해 주세요";
        Integer maxCharLength = (Integer) request.context().get(MAX_CHAR_LENGTH);
        if(maxCharLength != null) {
            userText = maxCharLength + "자 이내로 답변해 주세요";
        }

        String finalUserText = userText;
        log.info("finalUserText: {}", finalUserText);

        // 사용자 메시지를 강화한 프롬프트 얻기
        Prompt origianlPrompt = request.prompt();
        Prompt augmentedPrompt = origianlPrompt.augmentUserMessage(userMessage ->
            UserMessage.builder()
                    .text(userMessage.getText() + " " + finalUserText)
                    .build());

        // 수정된 chatClientRequest얻기
        ChatClientRequest mutatedRequest = request.mutate()
                .prompt(augmentedPrompt)
                .build();

        return mutatedRequest;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
