package com.example.springai.ch4.service;

import com.example.springai.ch4.service.dto.Hotel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AiServiceBeanOutputConverter {

    private final ChatClient chatClient;

    public AiServiceBeanOutputConverter(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public Hotel process(String city) {
        BeanOutputConverter<Hotel> converter = new BeanOutputConverter<>(Hotel.class);

        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("{city}에서 유명한 호텔 목록 5개를 출력하세요. {format}")
                .build();

        // 프롬프트 생성
        Prompt prompt = promptTemplate.create(Map.of("city", city, "format", converter.getFormat()));

        String json = chatClient.prompt(prompt)
                .call()
                .content();

        log.info(json);

        Hotel hotel = converter.convert(json);
        return hotel;

    }
}
