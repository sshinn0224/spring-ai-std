package com.example.springai.ch8.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiFaceService {

    private final JdbcTemplate jdbcTemplate;
    private final WebClient webClient;

    public AiFaceService(WebClient.Builder webClientBuilder, JdbcTemplate jdbcTemplate) {
        this.webClient = webClientBuilder.build();
        this.jdbcTemplate = jdbcTemplate;
    }

    public float[] getFaceVector(MultipartFile mf) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", mf.getBytes())
                .filename(mf.getOriginalFilename())
                .contentType(MediaType.valueOf(mf.getContentType()));

        MultiValueMap<String, HttpEntity<?>> multipartForm = builder.build();

        FaceEmbedApiResponse response = webClient.post()
                .uri("http://localhost:50001/get-face-vector")
                .body(BodyInserters.fromMultipartData(multipartForm))
                .retrieve()
                .bodyToMono(FaceEmbedApiResponse.class)
                .block();

        float[] vector = response.vector();
        return vector;
    }

    public void addFace(String personName, MultipartFile mf) throws IOException {
        float[] vector = getFaceVector(mf);
        log.info(vector.toString());

        String strVector = Arrays.toString(vector).replace(" ", "");
        String sql = """
                INSERT INTO face_vector_store (content, embedding)
                VALUES (?, ?::vector)
                """;

        jdbcTemplate.update(sql, personName, strVector);
    }

    public String findFace(MultipartFile mf) throws IOException {
        float[] vector = getFaceVector(mf);
        String strVector = Arrays.toString(vector).replace(" ", "");

        String sql = """
                SELECT content, (embedding <=> ?::vector) AS similarity
                FROM face_vector_store
                ORDER BY embedding <=> ?::vector
                LIMIT 3
                """;

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, strVector, strVector);
        for(Map<String, Object> map : list) {
            String personName = (String) map.get("personName");
            Double similarity = (Double) map.get("similarity");
            log.info("{} (L2 거리: {})", personName, similarity);
        }

        double similarity = (Double) list.get(0).get("similarity");
        if(similarity > 0.3) {
            return "등록된 사람이 아닙니다.";
        }

        return (String) list.get(0).get("content");
    }

    public record FaceEmbedApiResponse(float[] vector) {}

}
