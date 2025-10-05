package com.example.springai.ch11.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CarCheckTools {

    private final List<String> carNumbers = List.of("23가4567","234부8372","345가6789");

    @Tool(description = "차량 번호 등록 여부를 반환합니다.")
    public boolean checkCarNumber(@ToolParam(description = "차량 번호") String carNumber) {
        carNumber = carNumber.replaceAll("\\s+", ""); // 공백 제거
        log.info("LLM이 인식한 차량 번호 : {}", carNumber);

        return carNumbers.contains(carNumber);
    }
}
