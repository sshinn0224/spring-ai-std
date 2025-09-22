package com.example.springai.ch6.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.netty.internal.shaded.reactor.pool.PooledRefMetadata;

import java.io.IOException;

@Service
@Slf4j
public class AiVisionService {

    private final ChatClient chatClient;
    private final ImageModel imageModel;

    public AiVisionService(ChatClient.Builder chatClientBuilder, ImageModel imageModel) {
        this.chatClient = chatClientBuilder.build();
        this.imageModel = imageModel;
    }

    public Flux<String> imageAnalysis(String question, String contentType, MultipartFile file) throws IOException {
        log.info("image 분석 시작... ");
        // resource
        Resource audioResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        // 시스템 메시지
        SystemMessage systemMessage = SystemMessage.builder()
                .text("""
                        당신은 이미지 분석 전문가 입니다.
                        사용자 질문에 맞게 이미지를 분석하고 답변을 한국어로 말하세요.
                        """)
                .build();

        // 미디어 생성
        Media media = Media.builder()
                .mimeType(MimeType.valueOf(contentType))
                .data(audioResource)
                .build();

        // 사용자 메시지 생성
        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .media(media)
                .build();

        // 프롬프트 생성
        Prompt prompt = Prompt.builder()
                .messages(userMessage)
                .build();

        // 요청 및 응답 처리
        Flux<String> flux = chatClient.prompt(prompt)
                .stream().content();

        return flux;
    }
}
