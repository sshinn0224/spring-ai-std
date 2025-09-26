package com.example.springai.ch8.controller;

import com.example.springai.ch8.service.AiFaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
@Slf4j
@RequiredArgsConstructor
public class AiFaceController {

    public final AiFaceService aiFaceService;

    @PostMapping(
            value ="/add-face",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String addFace(@RequestParam("personName") String personName,
                          @RequestParam("attach") MultipartFile[] attach) throws IOException {
        for(MultipartFile mf : attach) {
            aiFaceService.addFace(personName, mf);

        }

        return "얼굴이 저장되었습니다.";
    }

    @PostMapping(
            value = "/find-face",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String findFace(@RequestParam("attach") MultipartFile attach) throws IOException {
        return aiFaceService.findFace(attach);
    }
}
