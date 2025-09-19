package com.example.springai.controller;

import com.example.springai.service.AiService;
import com.example.springai.service.AiServiceCot;
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
@RequiredArgsConstructor
@Slf4j
public class AiControllerCot {

    private final AiServiceCot aiService;

    @PostMapping(
            value = "/chain-of-thought",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> request(@RequestParam("question") String question) {
        return aiService.process(question);
    }
}
