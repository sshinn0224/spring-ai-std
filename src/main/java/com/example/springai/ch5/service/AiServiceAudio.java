package com.example.springai.ch5.service;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiServiceAudio {

    private final ChatClient chatClient;
    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    public AiServiceAudio(ChatClient.Builder chatClientBuilder,
                          OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel,
                          OpenAiAudioSpeechModel openAiAudioSpeechModel) {
        this.chatClient = chatClientBuilder.build();
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }

    public String stt(MultipartFile file) throws IOException {
        Resource audioResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        // 모델 옵션을 결정한다.
        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .model("whisper-1")
                .language("ko")
                .build();

        // 프롬프트를 생성한다
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(audioResource, options);

        // 응답 받기
        AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(prompt);
        String text = response.getResult().getOutput();

        return text;
    }

    public byte[] tts(String text) {
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .model("gpt-4o-mini-tts")
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0f)
                .build();

        // 프롬프트 생성
        SpeechPrompt prompt = new SpeechPrompt(text, options);

        // 모델을 호출 하고 응답 받기
        SpeechResponse response = openAiAudioSpeechModel.call(prompt);
        byte[] bytes = response.getResult().getOutput();

        return bytes;
    }

    public Map<String,String> chatText(String question) {
        String textAnswer = chatClient.prompt()
                .system("50자 이내로 답변해 주세요")
                .user(question)
                .call()
                .content();

        byte[] audio = tts(textAnswer);
        String base64Audio = Base64.getEncoder().encodeToString(audio);

        // 텍스트 답변과 음성 답변을 map에 저장
        Map<String, String> response = new HashMap<>();
        response.put("text", textAnswer);
        response.put("audio", base64Audio);

        return response;
    }

}
