package com.example.springai.ch11.controller;

import com.example.springai.ch11.service.BoomBarrierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.mime4j.dom.Multipart;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class BoomBarrierController {
    private final BoomBarrierService boomBarrierService;

    @PostMapping(
            value = "/boom-barrier-tools",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String request(@RequestParam("attach")MultipartFile attach) throws IOException {
        return boomBarrierService.chat(attach.getContentType(), attach.getBytes());
    }
}
