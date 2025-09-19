package com.example.springai.ch4.controller;

import com.example.springai.ch4.service.AiServiceBeanOutputConverter;
import com.example.springai.ch4.service.dto.Hotel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class AiControllerBeanOutput {

    private final AiServiceBeanOutputConverter aiService;

    @PostMapping(
            value = "/bean-output-converter",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Hotel request(@RequestParam("city") String city) {
        return aiService.process(city);
    }
}
