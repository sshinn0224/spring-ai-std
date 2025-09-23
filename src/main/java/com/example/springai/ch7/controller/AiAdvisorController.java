package com.example.springai.ch7.controller;

import com.example.springai.ch7.service.AiAdvisorService1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiAdvisorController {

    private final AiAdvisorService1 aiAdvisorService1;

//    @PostMapping(
//            value = "/advisor-chain",
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
//            produces = MediaType.TEXT_PLAIN_VALUE
//    )
//    public String advisorChain(@RequestParam("question") String question) {
//        return aiAdvisorService1.advisorChain1(question);
//    }

    @PostMapping(
            value = "/advisor-chain",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> advisorChain(@RequestBody Map<String, String> map) {
        log.info("STREAM...");
        Flux<String> response = aiAdvisorService1.advisorChain2(map.get("question"));

        return response;
    }


}
