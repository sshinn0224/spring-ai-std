package com.example.springai.ch8.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingResponseMetadata;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiEmbedService {

    private final EmbeddingModel embeddingModel;


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
}
