package com.example.springai.ch11.controller;

import com.example.springai.ch11.service.RecommendMovieService;
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
public class MovieController {

    private final RecommendMovieService recommendMovieService;

    @PostMapping(
            value = "/recommend-movie-tools",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String request(@RequestParam("question") String question) {
        return recommendMovieService.chat(question);
    }

    @PostMapping(
            value = "/exception-handling",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String exceptionRequest(@RequestParam("question") String question) {
        try {
            return recommendMovieService.exceptionChat(question);
        } catch(Exception e) {
            return "[애플리케이션] 질문을 처리할 수 없습니다.";
        }
    }

}
