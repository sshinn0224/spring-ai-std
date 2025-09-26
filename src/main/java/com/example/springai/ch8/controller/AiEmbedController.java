package com.example.springai.ch8.controller;

import com.example.springai.ch8.service.AiEmbedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping(
            value = "/search-document-1",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String searchDocument1(@RequestParam("question")String question) {
        List<Document> documents = aiEmbedService.searchDocument1(question);

        String text = "";
        for(Document document : documents) {
            text += "<div class='mb-2'>";
            text += "   <span class='me-2'>유사도 점수: %f,</span>".formatted(document.getScore());
            text += "   <span>%s</span>".formatted(document.getText(), document.getMetadata().get("year"));
            text += "</div>";
        }

        return text;
    }

    @PostMapping(
            value = "/search-document-2",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String searchDocument2(@RequestParam("question")String question) {
        List<Document> documents = aiEmbedService.searchDocument2(question);

        String text = "";
        for(Document document : documents) {
            text += "<div class='mb-2'>";
            text += "   <span class='me-2'>유사도 점수: %f,</span>".formatted(document.getScore());
            text += "   <span>%s</span>".formatted(document.getText(), document.getMetadata().get("year"));
            text += "</div>";
        }

        return text;

    }

    @PostMapping(
            value = "/delete-document",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String deleteDocument(@RequestParam("question") String question) {
        aiEmbedService.deleteDocument();
        return "document가 삭제 되었습니다.";
    }
}
