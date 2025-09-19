package com.example.springai.ch4.service;

import com.example.springai.ch4.service.dto.Hotel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiServiceParameterizedTypeReference {

    private final ChatClient chatClient;

    public AiServiceParameterizedTypeReference(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public List<Hotel> process(String cities) {
        BeanOutputConverter<List<Hotel>> converter
                = new BeanOutputConverter<>(new ParameterizedTypeReference<>() {});

        PromptTemplate promptTemplate = new PromptTemplate("""
                다음 도시들에서 유명한 호텔 3개를 출력하세요.
                {cities}
                {format}
                """);

        Prompt prompt = promptTemplate.create(
                Map.of("cities",cities,"format",converter.getFormat())
        );

        String json = chatClient.prompt(prompt)
                .call()
                .content();

        List<Hotel> hotelList = converter.convert(json);
        log.info(hotelList.toString());
        return hotelList;
    }

    public List<Hotel> process2(String cities) {
        List<Hotel> hotelList = chatClient.prompt()
                .user("""
                        다음 도시들에서 유명한 호텔 3개를 출력하세요.
                        %s
                        """.formatted(cities))
                .call()
                .entity(new ParameterizedTypeReference<>() {});

        return hotelList;
    }
}
