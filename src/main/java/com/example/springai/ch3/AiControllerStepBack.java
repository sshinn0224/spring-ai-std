package com.example.springai.ch3;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ai")
public class AiControllerStepBack {

    private final AiServiceStepBackPrompt aiService;

    @PostMapping(
            value = "/step-back-prompt",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String request(@RequestParam("question") String question) throws Exception {
        return aiService.process(question);
    }
}
