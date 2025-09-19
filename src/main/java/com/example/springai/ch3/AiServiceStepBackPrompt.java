package com.example.springai.ch3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AiServiceStepBackPrompt {

    private final ChatClient chatClient;

    public AiServiceStepBackPrompt(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String process(String question) throws Exception {
        String questions = chatClient.prompt()
                .user("""
                        사용자 질문을 처리하기 Step-Back 프롬프트 기법을 사용하려 합니다.
                        사용자 질문을 단계별 질문들로 재구성해 주세요.
                        맨 마지막 질문은 사용자 질문과 일치 해야 합니다.
                        단계별 질문을 항목으로 하는 JSON 배열로 출력해 주세요.
                        예시: ["...","...","...",...]
                        사용자 질문 : %s
                        """.formatted(question))
                .call()
                .content();

        String json = questions.substring(questions.indexOf("["),questions.indexOf("]")+1);

        log.info(json);

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> listQuestion = objectMapper.readValue(json,new TypeReference<>() {});

        String[] answerArray = new String[listQuestion.size()];
        for(int i=0 ; i < listQuestion.size() ; i++) {
            String stepQuestion = listQuestion.get(i);
            String stepAnswer = getStepAnswer(stepQuestion, answerArray);
            answerArray[i] = stepQuestion;
            log.info("단계: {}, 질문: {}, 답변: {}", i+1, stepQuestion, stepAnswer);
        }
        return answerArray[answerArray.length-1];
    }

    private String getStepAnswer(String question, String... pervStepAnswers) {
        String context = "";

        for(String pervStepAnswer: pervStepAnswers) {
            context = Objects.requireNonNullElse(pervStepAnswer, "");
        }

        String answer = chatClient.prompt()
                .user("""
                        %s
                        문맥 : %s
                        """.formatted(question, context))
                .call()
                .content();
        log.info(answer);
        return answer;
    }


}
