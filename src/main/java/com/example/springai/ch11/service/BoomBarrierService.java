package com.example.springai.ch11.service;

import com.example.springai.ch11.tools.BoomBarrierTools;
import com.example.springai.ch11.tools.CarCheckTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

@Service
@Slf4j
public class BoomBarrierService {

    private final ChatClient chatClient;
    private final CarCheckTools carCheckTools;
    private final BoomBarrierTools boomBarrierTools;

    public  BoomBarrierService(ChatClient.Builder chatClientBuilder,
                               CarCheckTools carCheckTools,
                               BoomBarrierTools boomBarrierTools) {
        this.chatClient = chatClientBuilder.build();
        this.carCheckTools = carCheckTools;
        this.boomBarrierTools = boomBarrierTools;
    }

    public String chat(String contentType, byte[] bytes) {
        // 미디어 생성
        Media media = Media.builder()
                .mimeType(MimeType.valueOf(contentType))
                .data(new ByteArrayResource(bytes))
                .build();

        // 사용자 메시지 생성
        UserMessage userMessage = UserMessage.builder()
                .text("""
                        이미지에서 '(숫자 2개~3개)-(한글 1자)-(숫자4개)'로 구성된
                        차량 번호를 인식 하세요. 예: 78라1234, 566가1234
                        인식된 차량 번호가 등록된 차량 번호인지 도구로 확인 하세요.
                        등록된 번호라면 도구로 차단봉을 올리고, 답변은 '차단기 올림'으로 하세요.
                        등록되지 않은 번호라면 도구로 차단봉을 내리고, 답변은 '차단기 내림'으로 하세요
                        """)
                .media(media)
                .build();

        // LLM 요청
        String answer = chatClient.prompt()
                .messages(userMessage)
                .tools(carCheckTools, boomBarrierTools)
                .call()
                .content();
        return answer;
    }


}
