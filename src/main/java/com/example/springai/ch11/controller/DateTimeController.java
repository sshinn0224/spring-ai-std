package com.example.springai.ch11.controller;

import com.example.springai.ch11.service.DateTimeService;
import com.example.springai.ch11.tools.DateTimeTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("/ai")
@Slf4j
@RequiredArgsConstructor
public class DateTimeController {

    private final DateTimeService dateTimeService;

    @PostMapping(
            value = "/date-time-tools",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String dateTimeTools(@RequestParam("question") String question) {
        return dateTimeService.chat(question);
    }
}
