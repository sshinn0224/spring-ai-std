package com.example.springai.controller;

import com.example.springai.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@Slf4j
@RequiredArgsConstructor
public class FluxAiController {

    private final AiService aiService;

    @PostMapping(
            value = "/chat-model-stream",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE // 라인으로 구분된 청크 텍스트
    )
    public Flux<String> chatModelStream(@RequestParam("question") String question) {
        Flux<String> answerStreamText = aiService.generateStreamText(question);

        return answerStreamText;
    }
}
