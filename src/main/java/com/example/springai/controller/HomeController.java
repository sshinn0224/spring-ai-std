package com.example.springai.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/chat-model")
    public String chatModel() {
        return "chat-model";
    }

    @GetMapping("/chat-model-stream")
    public String chatModelStream() {
        return "chat-model-stream";
    }

    @GetMapping("/prompt-template")
    public String promptTemplate() {
        return "prompt-template";
    }

    @GetMapping("/multi-messages")
    public String multiMessages() {
        return "multi-messages";
    }

    @GetMapping("/default-method")
    public String defaultMethod() {
        return "default-method";
    }

    @GetMapping("/zero-shot-prompt")
    public String zeroShotPrompt() {
        return "zero-shot-prompt";
    }

    @GetMapping("/few-shot-prompt")
    public String fewShotPrompt() {
        return "few-shot-prompt";
    }

    @GetMapping("/role-assignment")
    public String rollAssignment() {
        return "role-assignment";
    }

    @GetMapping("/step-back-prompt")
    public String stepBackPrompt() {
        return "step-back-prompt";
    }

    @GetMapping("/chain-of-thought")
    public String chainOfThought() {
        return "chain-of-thought";
    }

    @GetMapping("/self-consistency")
    public String selfConsistency() {
        return "self-consistency";
    }

    @GetMapping("/list-output-converter")
    public String listOutputConverter() {
        return "list-output-converter";
    }

    @GetMapping("/bean-output-converter")
    public String beanOutputConverter() {
        return "bean-output-converter";
    }

    @GetMapping("/generic-bean-output-converter")
    public String genericBeanOutputConverter() {
        return "generic-bean-output-converter";
    }

    @GetMapping("/map-output-converter")
    public String mapOutputConverter() {
        return "map-output-converter";
    }

    @GetMapping("/system-message")
    public String systemMessage() {
        return "system-message";
    }

    @GetMapping("/stt-tts")
    public String audioTest() {
        return "stt-tts";
    }

    @GetMapping("/stt-llm-tts")
    public String sttLlmTts2() {
        return "stt-llm-tts";
    }

    @GetMapping("/chat-voice-stt-llm-tts")
    public String chatVoiceSttLlmTts() {
        return "chat-voice-stt-llm-tts";
    }

    @GetMapping("/chat-voice-one-model")
    public String chatVoiceOneModel() {
        return "chat-voice-one-model";
    }

    @GetMapping("/image-analysis")
    public String imageAnalysis() {
        return "image-analysis";
    }

    @GetMapping("/video-analysis")
    public String videoAnalysis() {
        return "video-analysis";
    }

    @GetMapping("/image-generation")
    public String imageGeneration() {
        return "image-generation";
    }

    @GetMapping("/advisor-chain")
    public String advisorChain() {
        return "advisor-chain";
    }

    @GetMapping("/advisor-context")
    public String advisorContext() {
        return "advisor-context";
    }

    @GetMapping("/advisor-logging")
    public String advisorLogging() {
        return "advisor-logging";
    }

    @GetMapping("/advisor-safe-guard")
    public String advisorSafeGuard() {
        return "advisor-safe-guard";
    }

    @GetMapping("/text-embedding")
    public String textEmbedding() {
        return "text-embedding";
    }

    @GetMapping("/add-document")
    public String addDocument() {
        return "add-document";
    }

    @GetMapping("/search-document-1")
    public String searchDocument1() {
        return "search-document-1";
    }

    @GetMapping("/search-document-2")
    public String searchDocument2() {
        return "search-document-2";
    }

    @GetMapping("/delete-document")
    public String deleteDocument() {
        return "delete-document";
    }

    @GetMapping("/image-embedding")
    public String faceRecognition() {
        return "image-embedding";
    }

    @GetMapping("/txt-pdf-word-etl")
    public String txtPdfDocxEtl() {
        return "txt-pdf-word-etl";
    }

    @GetMapping("/html-etl")
    public String htmlEtl() {
        return "html-etl";
    }

    @GetMapping("/json-etl")
    public String jsonEtl() {
        return "json-etl";
    }

    @GetMapping("/rag")
    public String rag() {
        return "rag";
    }

    @GetMapping("/compression-query-transformer")
    public String compressionQueryTransformer() {
        return "compression-query-transformer";
    }

    @GetMapping("/rewrite-query-transformer")
    public String rewriteQueryTransformer() {
        return "rewrite-query-transformer";
    }

    @GetMapping("/translation-query-transformer")
    public String translationQueryTransformer() {
        return "translation-query-transformer";
    }

    @GetMapping("/multi-query-expander")
    public String multiQueryExpander() {
        return "multi-query-expander";
    }
}
