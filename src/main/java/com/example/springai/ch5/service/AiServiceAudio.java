package com.example.springai.ch5.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.*;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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

    public Flux<byte[]> ttsFlux(String text) {
        // 모델 옵션 설정
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .model("gpt-4o-mini-tts")
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY)
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0f)
                .build();

        // 프롬프트 생성
        SpeechPrompt prompt = new SpeechPrompt(text, options);

        // 모델 요청 응답 생성
        Flux<SpeechResponse> reponse = openAiAudioSpeechModel.stream(prompt);
        Flux<byte[]> flux = reponse.map(speechResponse -> speechResponse.getResult().getOutput());

        return flux;
    }

    // 음성으로 질문/대답 하기
    public Flux<byte[]> chatVoiceSttLlmTts(MultipartFile file) throws IOException {
        String textQuestion = stt(file);
        String textAnswer = chatClient.prompt()
                .system("50자 이내로 답변 해 주세요")
                .user(textQuestion)
                .options(ChatOptions.builder()
                        .maxTokens(100)
                        .build())
                .call()
                .content();

        // tts를 이용해서 비동기 음성 데이터 얻기
        Flux<byte[]> flux = ttsFlux(textAnswer);
        return flux;
    }

    public byte[] chatVoiceOneModel(MultipartFile file, String mimeType) throws IOException {
        // 음성 데이터를 resource로 생성
        Resource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        // 사용자 메시지 생성
        UserMessage userMessage = UserMessage.builder()
                .text("제공되는 음성에 맞는 자연스러운 대화로 이어주세요.")
                .media(new Media(MimeType.valueOf(mimeType), resource))
                .build();

        // 모델 옵션 설정
        ChatOptions chatOptions = OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_4_O_AUDIO_PREVIEW)
                .outputModalities(List.of("text", "audio"))
                .outputAudio(new OpenAiApi.ChatCompletionRequest.AudioParameters(
                        OpenAiApi.ChatCompletionRequest.AudioParameters.Voice.ALLOY,
                        OpenAiApi.ChatCompletionRequest.AudioParameters.AudioResponseFormat.MP3
                ))
                .build();

        ChatResponse response = chatClient.prompt()
                .system("50자 이내로 답변해 주세요")
                .messages(userMessage)
                .options(chatOptions)
                .call()
                .chatResponse();

        AssistantMessage assistantMessage = response.getResult().getOutput();

        String textAnswer = assistantMessage.getText();
        log.info("텍스트 응답 : {}", textAnswer);

        byte[] audioAnswer = assistantMessage.getMedia().get(0).getDataAsByteArray();
        return audioAnswer;

    }

}
