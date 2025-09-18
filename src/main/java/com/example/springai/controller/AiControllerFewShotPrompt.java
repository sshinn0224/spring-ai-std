package com.example.springai.controller;

import com.example.springai.service.AiServiceFewShotPrompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@Slf4j
@RequiredArgsConstructor
public class AiControllerFewShotPrompt {

    private final AiServiceFewShotPrompt aiService;

    @PostMapping(
            value = "/few-shot-prompt",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String fewShotPrompt(@RequestParam("order") String order) {
        String res = aiService.fewShotPrompt(order);
        log.info(res.toString());
        return res;
    }
}
