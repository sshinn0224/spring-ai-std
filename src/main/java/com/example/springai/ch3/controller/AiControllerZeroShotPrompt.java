package com.example.springai.ch3.controller;

import com.example.springai.ch3.service.AiServiceZeroShotPrompt;
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
public class AiControllerZeroShotPrompt {

    private final AiServiceZeroShotPrompt aiService;

    @PostMapping(
            value = "/zero-shot-prompt",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String zeroShotPrompt(@RequestParam("review") String review) {
        return aiService.zeroShotPrompt(review);
    }
}
