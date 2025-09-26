package com.example.springai.ch8.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingResponseMetadata;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiEmbedService {

    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;


    public void textEmbedding(String question) {
        // do embedding
        EmbeddingResponse response = embeddingModel.embedForResponse(List.of(question));

        // get embedding model
        EmbeddingResponseMetadata metadata = response.getMetadata();
        log.info("Embedding model name: {}", metadata.getModel());
        log.info("Embedding dimension: {}", embeddingModel.dimensions());

        // get Response
        Embedding embedding = response.getResults().get(0);
        log.info("vector dimension: {}", embedding.getOutput().length);
        log.info("vector : {}", embedding.getOutput());
    }

    public void addDocument() {
        List<Document> documents = List.of(
                new Document("대통령 선거는 5년마다 있습니다.", Map.of("source", "헌법", "year", 1987)),
                new Document("대통령 임기는 4년입니다.", Map.of("source", "헌법", "year", 1980)),
                new Document("국회의원은 법률안을 심의·의결합니다.", Map.of("source", "헌법", "year", 1987)),
                new Document("자동차를 사용하려면 등록을 해야합니다.", Map.of("source", "자동차관리법")),
                new Document("대통령은 행정부의 수반입니다.", Map.of("source", "헌법", "year", 1987)),
                new Document("국회의원은 4년마다 투표로 뽑습니다.", Map.of("source", "헌법", "year", 1987)),
                new Document("승용차는 정규적인 점검이 필요합니다.", Map.of("source", "자동차관리법")));

        // 백터 저장소에 저장
        vectorStore.add(documents);
    }

    public List<Document> searchDocument1(String question) {
        List<Document> documents = vectorStore.similaritySearch(question);
        return documents;
    }

    public List<Document> searchDocument2(String question) {
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(1)
                        .similarityThreshold(0.4)
                        .filterExpression("source == '헌법' && year >= 1987")
                        .build()
        );
        return documents;
    }

    public void deleteDocument() {
        vectorStore.delete("source == '헌법' && year < 1987");
    }
}
