package com.example.springai.ch10.controller;

import com.example.springai.ch10.service.ETLService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;

@RequestMapping("/ai")
@Slf4j
@RequiredArgsConstructor
@RestController
public class AiETLController {

    private final ETLService etlService;

    @PostMapping(
            value = "/txt-pdf-docx-etl",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String txtPdfDocsEtl(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("attach") MultipartFile attach) throws IOException {

        return etlService.etlFromFile(title, author, attach);
    }
}
