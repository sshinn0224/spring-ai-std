package com.example.springai.ch9.controller;

import com.example.springai.ch9.service.AiMemoryVectorService;
import jakarta.servlet.http.HttpSession;
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
public class AiMemoryVectorController {

    private final AiMemoryVectorService aiMemoryVectorService;

    @PostMapping(
            value = "/chat",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String doRequest(@RequestParam("question") String question, HttpSession session) {
        return aiMemoryVectorService.chat(question, session.getId());
    }
}
