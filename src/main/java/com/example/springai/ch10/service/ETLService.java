package com.example.springai.ch10.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.reader.JsonMetadataGenerator;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ETLService {

    private final ChatModel chatModel;
    private final VectorStore vectorStore;


    public ETLService(ChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    public String etlFromFile(String title, String author, MultipartFile attach) throws IOException {
        List<Document> documents = extractFromFile(attach);
        if(documents.isEmpty()) {
            return ".txt, .pdf, .doc, .docx 파일 중에 하나를 올려 주세요";
        }
        log.info("추출 . Document 수 : {} 개", documents.size());

        // 메타데이터에 공통 정보 추가하기
        for(Document document : documents) {
            Map<String, Object> metadata = document.getMetadata();
            metadata.putAll(
                    Map.of("title",title,
                            "author", author,
                            "source", attach.getOriginalFilename())
            );
        }

        // 작은 사이즈로 분할 하기
        documents = transForm(documents);
        log.info("변환 . Document 수 : {} 개", documents.size());

        // 적재 하기
        vectorStore.add(documents);

        return "올린 문서를 추출-변환-적재 완료 하였습니다.";
    }

    private List<Document> extractFromFile(MultipartFile file) throws IOException {
        Resource resource = new ByteArrayResource(file.getBytes());

        List<Document> documents = new ArrayList<>();
        if(file.getContentType().equals("text/plain")) {
            // text (.txt)
            DocumentReader reader = new TextReader(resource);
            documents = reader.read();
        } else if(file.getContentType().equals("application/pdf")) {
            DocumentReader reader = new PagePdfDocumentReader(resource);
            documents = reader.read();
        } else if(file.getContentType().contains("wordprocessingml")) {
            DocumentReader reader = new TikaDocumentReader(resource);
            documents = reader.read();
        }

        return documents;
    }

    private List<Document> transForm(List<Document> documents) {
        List<Document> transformedDocuments = null;

        // 작게 분할 하기
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        transformedDocuments = tokenTextSplitter.apply(documents);

        // 메타 데이터에 키워드 추가 하기
//        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(chatModel, 5);
//        transformedDocuments = keywordMetadataEnricher.apply(transformedDocuments);

        return transformedDocuments;
    }

    public String etlFromHtml(String title, String author, String url) throws IOException {
        Resource resource = new UrlResource(url);

        JsoupDocumentReader reader = new JsoupDocumentReader(
                resource,
                JsoupDocumentReaderConfig.builder()
                        .charset("UTF-8")
                        .selector("#content")
                        .additionalMetadata(Map.of(
                                "title", title,
                                "author", author,
                                "url", url))
                        .build()
        );

        List<Document> documents = reader.read();
        log.info("추출된 Document 수: {} 개", documents.size());

        // T : 변환하기
        DocumentTransformer documentTransformer = new TokenTextSplitter();
        List<Document> transformedDocuments = documentTransformer.apply(documents);
        log.info("변환 된 Document 수: {}개",  transformedDocuments.size());

        // L : 적재하기
        vectorStore.add(transformedDocuments);

        return "html 에서 추출-변환-적재 완료 했습니다.";

    }

    public String etlFromJson(String url) throws Exception {
        Resource resource = new UrlResource(url);

        // E : 추출하기
        JsonReader reader = new JsonReader(
                resource,
                new JsonMetadataGenerator() {
                    @Override
                    public Map<String, Object> generate(Map<String, Object> jsonMap) {
                        return Map.of("title", jsonMap.get("title"),
                                "author", jsonMap.get("author"),
                                "url",url);
                    }
                }
        );

        List<Document> documents = reader.read();
        log.info("추출 된 Document수 : {} 개", documents.size());

        // T: 변환하기
        DocumentTransformer documentTransformer = new TokenTextSplitter();
        List<Document> transformedDocuments = documentTransformer.apply(documents);
        log.info("변환 된 Document 수: {}개",  transformedDocuments.size());

        vectorStore.add(transformedDocuments);

        return "JSON에서 추출-변환-적재 완료 하였습니다.";
    }


}
