package com.example.springai.ch11.controller;

import com.example.springai.ch11.service.FileSystemService;
import com.example.springai.ch11.tools.FileSystemTools;
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
@RequiredArgsConstructor
@Slf4j
public class FileSystemController {

    private final FileSystemService fileSystemService;

    @PostMapping(
            value = "/file-system-tools",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String request(@RequestParam("question") String question, HttpSession session) {
        return fileSystemService.chat(question, session.getId());
    }
}
