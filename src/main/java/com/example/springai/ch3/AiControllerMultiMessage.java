package com.example.springai.ch3;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ai")
@Slf4j
@RequiredArgsConstructor
public class AiControllerMultiMessage {

    private final AiServiceMultiMessage aiService;

    @PostMapping(
            value = "/multi-messages",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String multiMessages(@RequestParam("question") String question, HttpSession session) {
        List<Message> chatMemory = (List<Message>) session.getAttribute("chatMemory");

        if(chatMemory == null) {
            chatMemory = new ArrayList<>();
            session.setAttribute("chatMemory", chatMemory);
        }
        String answer = aiService.multiMessage(question, chatMemory);

        return answer;

    }
}
