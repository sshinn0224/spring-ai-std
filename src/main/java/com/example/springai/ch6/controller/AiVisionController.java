package com.example.springai.ch6.controller;

import com.example.springai.ch6.service.AiVisionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiVisionController {

    private final AiVisionService aiVisionService;

    @PostMapping(
            value ="/image-analysis",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> imageAnalysis(@RequestParam("question") String question,
                                      @RequestParam("attach") MultipartFile attach) throws IOException {
        // 이미지가 업로드 되지 않았을 경우 처리
        if(attach == null || !attach.getContentType().contains("image/")) {
            Flux<String> response = Flux.just("이미지를 올려주세요.");
            return response;
        }

        Flux<String> flux = aiVisionService.imageAnalysis(question, attach.getContentType(), attach);

        return flux;
    }

    @PostMapping(
            value = "/image-generate",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String imageGenerate(@RequestParam("description") String description) {
        try {
            String base64Json = aiVisionService.generateImage(description);
            return base64Json;
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }
    }

    @PostMapping(
            value = "/image-edit",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String imageEdit(@RequestParam("description") String description,
                            @RequestParam("originalImage") MultipartFile originalImage,
                            @RequestParam("maskImage") MultipartFile maskImage) throws IOException {

        try {
            String b64Json = aiVisionService.editImage(description, originalImage.getBytes(), maskImage.getBytes());

            return b64Json;
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }

    }


}
