package com.example.springai.ch4.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class AiServiceMapOutput {

    private final ChatClient chatClient;

    public AiServiceMapOutput(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public Map<String,Object> mapOutputConverter(String hotel) {
        MapOutputConverter converter = new MapOutputConverter();

        PromptTemplate promptTemplate = new PromptTemplate("호텔 {hotel} 에 대해 정보를 알려주세요. {format}");

        Prompt prompt = promptTemplate.create(Map.of("hotel", hotel, "format", converter.getFormat()));

        String json = chatClient.prompt(prompt)
                .call()
                .content();

        Map<String, Object> hotelInfo = converter.convert(json);
        log.info(hotelInfo.toString());

        return hotelInfo;
    }

    public Map<String,Object> mapOutputConverterHighLevel(String hotel) {
        Map<String,Object> hotelInfo = chatClient.prompt()
                .user("호텔 %s에 대해 정보를 알려주세요".formatted(hotel))
                .call()
                .entity(new MapOutputConverter());

        return hotelInfo;
    }
}
