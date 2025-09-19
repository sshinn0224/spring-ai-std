package com.example.springai.ch4.service;

import com.example.springai.ch4.service.dto.Hotel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiServiceListOutputConverter {

    private final ChatClient chatClient;

    public AiServiceListOutputConverter(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public List<String> process(String city) {
        // 구조화 된 출력 변환기 생성
        ListOutputConverter converter = new ListOutputConverter();

        // 프롬프트 탬플릿 생성
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("{city}에서 유명한 호텔 목록 5개를 출력하세요. {format}")
                .build();

        // 프롬프트 생성
        Prompt prompt = promptTemplate.create(Map.of("city", city, "format", converter.getFormat()));

        // LLM 쉼표로 구분된 텍스트 출력 얻기
        String commaSeparatedString = chatClient.prompt(prompt)
                .call()
                .content();

        // List<String> 으로 변환
        List<String> hotelList = converter.convert(commaSeparatedString);
        log.info("hotelList: {}", hotelList);
        return hotelList;
    }

    public List<String> process2(String city) {
        List<String> hotelList = chatClient
                .prompt()
                .user("%s 에서 유명한 호텔 목록 5개를 출력하세요".formatted(city))
                .call()
                .entity(new ListOutputConverter());

        return hotelList;
    }

}
