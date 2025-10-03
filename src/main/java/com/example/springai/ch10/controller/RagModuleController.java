package com.example.springai.ch10.controller;

import com.example.springai.ch10.service.RagService2;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.Media;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Slf4j
public class RagModuleController {

    private final RagService2 ragService2;

    @PostMapping(
            value = "/compression-query-transformer",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String compressionQueryTransformer(
            @RequestParam("question") String question,
            @RequestParam(value = "score", defaultValue = "0.0") double score,
            @RequestParam("source") String source,
            HttpSession httpSession
    ) {
        return ragService2.chatWithCompression(question, score, source, httpSession.getId());
    }

    @PostMapping(
            value = "/rewrite-query-transformer",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String rewriteQueryTransformer(
            @RequestParam("question") String question,
            @RequestParam(value = "score", defaultValue = "0.0") double score,
            @RequestParam("source") String source
    ) {
        return  ragService2.chatWithRewrite(question, score, source);
    }


    @PostMapping(
            value = "/translation-query-transformer",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String translationQueryTransformer(
            @RequestParam("question") String question,
            @RequestParam(value = "score", defaultValue = "0.0") double score,
            @RequestParam("source") String source
    ) {
        return ragService2.chatWithTranslation(question, score, source);
    }


    @PostMapping(
            value = "/multi-query-expander",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String multiQueryExpander(
            @RequestParam("question") String question,
            @RequestParam(value = "score", defaultValue = "0.0") double score,
            @RequestParam("source") String source
    ) {
        return ragService2.chatWithMultiQuery(question, score, source);
    }
}
