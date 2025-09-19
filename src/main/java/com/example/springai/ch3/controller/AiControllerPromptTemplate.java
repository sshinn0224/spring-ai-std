package com.example.springai.ch3.controller;

import com.example.springai.ch3.service.AiServiceByPromptTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ai")
public class AiControllerPromptTemplate {

    private final AiServiceByPromptTemplate aiservice;

    @PostMapping(
            value ="/prompt-template",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> promptTemplate(
            @RequestParam("statement") String statement,
            @RequestParam("language") String language) {
//        Flux<String> response = aiservice.promptTemplate1(statement, language);
//        Flux<String> response = aiservice.promptTemplate2(statement, language);
        Flux<String> response = aiservice.promptTemplate3(statement, language);
//        Flux<String> response = aiservice.promptTemplate4(statement, language);

        return response;
    }
}
