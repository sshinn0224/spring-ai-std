package com.example.springai.ch4.controller;

import com.example.springai.ch3.AiServiceListOutputConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai")
@Slf4j
@RequiredArgsConstructor
public class AiControllerListOutputConverter {

    private final AiServiceListOutputConverter aiService;

    @PostMapping(
            value = "/list-output-converter",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<String> request(@RequestParam("city") String city) {
        return aiService.process2(city);
//        return aiService.process(city);
    }
}
