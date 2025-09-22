package com.example.springai.ch5.controller;

import com.example.springai.ch5.service.AiServiceAudio;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@Slf4j
@RequiredArgsConstructor
public class AiControllerAudio {

    private final AiServiceAudio aiService;

    @PostMapping(
            value = "/stt",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String stt(@RequestParam("speech") MultipartFile speech) throws IOException {
        log.info("START STT...");
        return aiService.stt(speech);
    }

    @PostMapping(
            value = "/tts",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public byte[] tts(@RequestParam("text") String text) {
        log.info("START TTS...");
        byte[] bytes = aiService.tts(text);
        return bytes;
    }

    @PostMapping(
            value = "/chat-text",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> chatText(@RequestParam("question") String question) {
        log.info("START CHAT TEXT...");
        Map<String,String> response = aiService.chatText(question);
        return response;
    }
}
