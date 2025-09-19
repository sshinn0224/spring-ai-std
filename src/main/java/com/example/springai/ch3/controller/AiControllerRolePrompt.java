package com.example.springai.ch3.controller;

import com.example.springai.ch3.service.AiServiceRolePrompt;
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
public class AiControllerRolePrompt {

    private final AiServiceRolePrompt aiService;

    @PostMapping(
            value = "/role-assignment",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> doRequest(@RequestParam("requirements") String requirements) {
        return aiService.doRequest(requirements);
    }
}
