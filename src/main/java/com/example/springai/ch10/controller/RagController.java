package com.example.springai.ch10.controller;

import com.example.springai.ch10.service.RagService1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/ai")
@RequiredArgsConstructor
public class RagController {

    private final RagService1 ragService1;

    @GetMapping(
            value = "/rag-clear",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String ragClear() {
        ragService1.clearVectorStore();
        return "벡터 저장소의 데이터를 모두 삭제했습니다.";
    }

    @PostMapping(
            value = "/rag-etl",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String ragEtl(@RequestParam("attach") MultipartFile attach,
                         @RequestParam("source") String source,
                         @RequestParam(value = "chunkSize", defaultValue = "200") int chunkSize,
                         @RequestParam(value = "minChunkChars", defaultValue = "100") int minChunkSizeChars) throws IOException {
        ragService1.ragEtl(attach, source, chunkSize, minChunkSizeChars);

        return "PDF ETL 과정을 성공적으로 처리 했습니다.";
    }

    @PostMapping(
            value = "/rag-chat",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String ragChat(@RequestParam("question") String question,
                          @RequestParam(value = "score", defaultValue = "0.0") double score,
                          @RequestParam("source") String source) {
        return ragService1.ragChat(question, score, source);
    }
}
