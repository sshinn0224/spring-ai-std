package com.example.springai.ch10.service;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class RagService2 {

    private final ChatClient chatClient;
    private final ChatModel chatModel;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;

    public RagService2(ChatClient.Builder chatClientBuilder,
                       ChatMemory chatMemory,
                       ChatModel chatModel,
                       VectorStore vectorStore) {
        this.chatMemory = chatMemory;
        this.vectorStore = vectorStore;
        this.chatModel = chatModel;
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    public CompressionQueryTransformer createCompressionQueryTransformer() {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                );

        return CompressionQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();
    }

    public VectorStoreDocumentRetriever createVectorStoreDocumentRetriever(double score, String source) {
        VectorStoreDocumentRetriever vectorStoreDocumentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(score)
                .topK(3)
                .filterExpression(() -> {
                    FilterExpressionBuilder builder = new FilterExpressionBuilder();
                    if(StringUtils.hasText(source)){
                        return builder.eq("source", source).build();
                    } else {
                        return null;
                    }
                })
                .build();

        return vectorStoreDocumentRetriever;
    }

    public String chatWithCompression(String question, double score, String source, String conversationId) {
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(createCompressionQueryTransformer())
                .documentRetriever(createVectorStoreDocumentRetriever(score, source))
                .build();


        String answer = this.chatClient.prompt()
                .user(question)
                .advisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        retrievalAugmentationAdvisor
                )
                .advisors(advisorSpec -> advisorSpec.param(
                        ChatMemory.CONVERSATION_ID, conversationId
                ))
                .call()
                .content();

        return answer;
    }

    private RewriteQueryTransformer createRewriteQueryTransformer() {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                );

        RewriteQueryTransformer rewriteQueryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();

        return rewriteQueryTransformer;
    }

    public String chatWithRewrite(String question, double score, String source) {
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor
                .builder()
                .queryTransformers(createRewriteQueryTransformer())
                .documentRetriever(createVectorStoreDocumentRetriever(score, source))
                .build();

        return this.chatClient.prompt()
                .user(question)
                .advisors(retrievalAugmentationAdvisor)
                .call()
                .content();
    }

    public TranslationQueryTransformer createTranslationQueryTransformer() {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor());

        TranslationQueryTransformer translationQueryTransformer = TranslationQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .targetLanguage("korean")
                .build();

        return translationQueryTransformer;
    }

    public String chatWithTranslation(String question, double score, String source) {
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor
                .builder()
                .queryTransformers(createTranslationQueryTransformer())
                .documentRetriever(createVectorStoreDocumentRetriever(score, source))
                .build();

        return this.chatClient.prompt()
                .user(question)
                .advisors(retrievalAugmentationAdvisor)
                .call()
                .content();
    }

    private MultiQueryExpander createMultiQueryExpander() {
        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                );

        // 질문 확성기 생성
        MultiQueryExpander multiQueryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .includeOriginal(true)
                .numberOfQueries(3)
                .build();

        return multiQueryExpander;
    }

    public String chatWithMultiQuery(String question, double score, String source) {
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor
                .builder()
                .queryExpander(createMultiQueryExpander())
                .documentRetriever(createVectorStoreDocumentRetriever(score, source))
                .build();

        // 프롬프트를 LLM으로 전송하고 응답을 받는 코드
        return this.chatClient.prompt()
                .user(question)
                .advisors(retrievalAugmentationAdvisor)
                .call()
                .content();
    }


}
