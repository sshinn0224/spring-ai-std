package com.example.springai.ch6.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.*;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class AiVisionService {

    private final ChatClient chatClient;
    private final ImageModel imageModel;

    public AiVisionService(ChatClient.Builder chatClientBuilder, ImageModel imageModel) {
        this.chatClient = chatClientBuilder.build();
        this.imageModel = imageModel;
    }

    public Flux<String> imageAnalysis(String question, String contentType, MultipartFile file) throws IOException {
        log.info("image 분석 시작... ");
        // resource
        Resource audioResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        // 시스템 메시지
        SystemMessage systemMessage = SystemMessage.builder()
                .text("""
                        당신은 이미지 분석 전문가 입니다.
                        사용자 질문에 맞게 이미지를 분석하고 답변을 한국어로 말하세요.
                        """)
                .build();

        // 미디어 생성
        Media media = Media.builder()
                .mimeType(MimeType.valueOf(contentType))
                .data(audioResource)
                .build();

        // 사용자 메시지 생성
        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .media(media)
                .build();

        // 프롬프트 생성
        Prompt prompt = Prompt.builder()
                .messages(userMessage)
                .build();

        // 요청 및 응답 처리
        Flux<String> flux = chatClient.prompt(prompt)
                .stream().content();

        return flux;
    }

    public String koToEn(String text) {
        String question = """
                당신은 번역사입니다. 아래 문장을 영어 문장으로 번역 해 주세요.
                %s
                """
                .formatted(text);

        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .build();

        Prompt prompt = Prompt.builder()
                .messages(userMessage)
                .build();

        // LLM 호출하고 텍스트 답변 얻기
        String englishDescription = chatClient.prompt(prompt)
                .call()
                .content();

        return englishDescription;
    }

    public String generateImage(String description) {
        String englishDescription = koToEn(description);

        ImageMessage imageMessage = new ImageMessage(englishDescription);

        // 이미지 설명을 포함하는 imageMessage 생성
        OpenAiImageOptions openAiImageOptions = OpenAiImageOptions.builder()
                .model("dall-e-3")
                .responseFormat("b64_json")
                .width(1024)
                .height(1024)
                .N(1)
                .build();

        List<ImageMessage> imageMessageList = List.of(imageMessage);
        ImagePrompt imagePrompt = new ImagePrompt(imageMessageList, openAiImageOptions);

        // 모델 호출 및 응답 받기
        ImageResponse imageResponse = imageModel.call(imagePrompt);


        String base64json = imageResponse.getResult().getOutput().getB64Json();

        return base64json;
    }

    public String editImage(String description, byte[] originalImage, byte[] maskImage) {
        String englishDescription = koToEn(description);

        ByteArrayResource originalRes = new ByteArrayResource(originalImage) {
            @Override
            public String getFilename() {
                return "image.png";
            }
        };

        ByteArrayResource maskRes = new ByteArrayResource(maskImage) {
            @Override
            public String getFilename() {
                return "mask.png";
            }
        };

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("model", "dall-e-2");
        form.add("image", originalRes);
        form.add("mask", maskRes);
        form.add("prompt", description);
        form.add("n","1");
        form.add("size","1536X1024");
        form.add("quality","low");
        form.add("response_format","b64_json");

        String apiKey = System.getProperty("openai.api.key");

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/image/edits")
                .defaultHeader("Authorization", "Bearer" + apiKey)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(10 * 1536 * 1024))
                        .build())
                .build();

        Mono<OpenAIImageEditResponse> mono = webClient.post()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(form))
                .retrieve()
                .bodyToMono(OpenAIImageEditResponse.class);

        OpenAIImageEditResponse response = mono.block();

        String b64Json = response.data().get(0).b64_json();
        return b64Json;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record OpenAIImageEditResponse(List<Image> data) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Image(String url, String b64_json) {}
    }
}
