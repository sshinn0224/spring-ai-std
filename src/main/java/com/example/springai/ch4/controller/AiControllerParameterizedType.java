package com.example.springai.ch4.controller;

import com.example.springai.ch4.service.AiServiceParameterizedTypeReference;
import com.example.springai.ch4.service.dto.Hotel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.content.MediaContent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class AiControllerParameterizedType {

    private final AiServiceParameterizedTypeReference aiService;

    @PostMapping(
            value = "/generic-bean-output-converter",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Hotel> request(@RequestParam("cities") String cities) {
        return aiService.process(cities);
    }
}
