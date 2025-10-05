package com.example.springai.ch11.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

@Component
@Slf4j
public class SearchTools {

    private final String googleEndPoint;
    private final String apiKey;
    private final String engineId;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new  ObjectMapper();

    public SearchTools(@Value("${google.search.endpoint}") String googleEndPoint,
                       @Value("${google.search.apiKey}") String apiKey,
                       @Value("${google.search.engineId}") String engineId,
                       WebClient.Builder webClientBuilder) {
        this.googleEndPoint = googleEndPoint;
        this.apiKey = apiKey;
        this.engineId = engineId;
        this.webClient = webClientBuilder
                .baseUrl(googleEndPoint)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Tool(description = "인터넷 검색을 합니다. 제목, 링크, 요약을 문자열로 반환 합니다.")
    public String googleSearch(String query) {
        try {
            String responseBody = webClient.get()
                    .uri(uriBuilder ->
                        uriBuilder
                                .queryParam("key", apiKey)
                                .queryParam("cx", engineId)
                                .queryParam("q", query)
                                .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("응답 본문: {}", responseBody);

            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode items = root.path("items");

            if(!items.isArray() || items.isEmpty()){
                return "검색 결과가 없습니다.";
            }

            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < Math.min(3, items.size()); i++) {
                JsonNode item = items.get(i);
                String title = item.path("title").asText();
                String link = item.path("link").asText();
                String snippet = item.path("snippet").asText();
                sb.append(String.format("[%d] %s\n%s\n%s\n\n", i + 1, title, link, snippet));
            }

            return sb.toString().trim();

        } catch(Exception e) {
            return "인터넷 검색 중 오류 발생: " + e.getMessage();
        }
    }

    @Tool(description = "웹 페이지의 본문 텍스트를 반환 합니다.")
    public String fetchPageContent(String url) {
        try {
            String html = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if(html == null || html.isBlank()) {
                return "페이지 내용을 가져올 수 없습니다.";
            }

            Document doc = Jsoup.parse(html);
            String bodyText = doc.body().text();

            return bodyText.isBlank() ? "본문 텍스트라 비어 있습니다." : bodyText;
        } catch(Exception e) {
            return "페이지 로딩 중 오류 발생: " + e.getMessage();
        }
    }
}
