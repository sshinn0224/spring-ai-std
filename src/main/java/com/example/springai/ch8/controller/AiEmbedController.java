package com.example.springai.ch8.controller;

import com.example.springai.ch8.service.AiEmbedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiEmbedController {

    private final AiEmbedService aiEmbedService;

    @PostMapping(
            value = "/text-embedding",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String textEmbedding(@RequestParam("question") String question) {
        aiEmbedService.textEmbedding(question);

        return "서버 콘솔 터미널을 확인하세요";
    }

    @PostMapping(
            value = "/add-document",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String addDocument(@RequestParam("question") String question) {
        aiEmbedService.addDocument();

        return "백터 저장소에 Document가 저장되었습니다.";
    }
}
