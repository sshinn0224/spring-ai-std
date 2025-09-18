package com.example.springai.controller;

import com.example.springai.service.AiService;
import com.example.springai.service.AiServiceDefaultMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class AiControllerDefaultMethod {

    private final AiServiceDefaultMethod aiService;

    @PostMapping(
            value = "/default-method",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> defaultMethod(@RequestParam("question") String question) {
        Flux<String> response = aiService.defaultMethod(question);

        return response;
    }
}
