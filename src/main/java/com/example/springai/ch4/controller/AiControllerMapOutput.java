package com.example.springai.ch4.controller;


import com.example.springai.ch4.service.AiServiceMapOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@Slf4j
@RequiredArgsConstructor
public class AiControllerMapOutput {

    private final AiServiceMapOutput aiService;

    @PostMapping(
            value = "/map-output-converter",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Object> request(@RequestParam("hotel") String hotel) {
        return aiService.mapOutputConverter(hotel);
    }
}
