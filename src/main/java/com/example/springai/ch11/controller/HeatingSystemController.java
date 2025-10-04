package com.example.springai.ch11.controller;

import com.example.springai.ch11.service.HeatingSystemService;
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
public class HeatingSystemController {

    private final HeatingSystemService heatingSystemService;

    @PostMapping(
            value = "/heating-system-tools",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String request(@RequestParam("question") String question) {
        return heatingSystemService.chat(question);
    }
}
