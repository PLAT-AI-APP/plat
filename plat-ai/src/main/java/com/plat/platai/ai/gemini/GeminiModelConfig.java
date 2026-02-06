package com.plat.platai.ai.gemini;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.ai.google.genai.GoogleGenAiChatModel.ChatModel.*;


@Configuration
public class GeminiModelConfig {

    @Bean
    ChatGemini25FlashClient chatGemini25FlashClient(GoogleGenAiChatModel chatModel) {
        return new ChatGemini25FlashClient(gemini25FlashChatClient(chatModel));
    }

    @Bean
    ChatGemini25ProClient chatGemini25ProClient(GoogleGenAiChatModel chatModel) {
        return new ChatGemini25ProClient(gemini25ProChatClient(chatModel));
    }

    @Bean
    ChatGemini30FlashClient chatGemini30FlashClient(GoogleGenAiChatModel chatModel) {
        return new ChatGemini30FlashClient(gemini30FlashChatClient(chatModel));
    }

    @Bean
    ChatGemini30ProClient chatGemini30ProClient(GoogleGenAiChatModel chatModel) {
        return new ChatGemini30ProClient(gemini30PRoChatClient(chatModel));
    }

    @Bean("gemini25FlashChatClient")
    ChatClient gemini25FlashChatClient(GoogleGenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
            .defaultOptions(defaultOptions(GEMINI_2_5_FLASH))
            .build();
    }

    @Bean("gemini25ProChatClient")
    ChatClient gemini25ProChatClient(GoogleGenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
            .defaultOptions(defaultOptions(GEMINI_2_5_PRO))
            .build();
    }

    @Bean("gemini30FlashChatClient")
    ChatClient gemini30FlashChatClient(GoogleGenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
            .defaultOptions(defaultOptions("gemini-3-flash-preview"))
            .build();
    }

    @Bean("gemini30ProChatClient")
    ChatClient gemini30PRoChatClient(GoogleGenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
            .defaultOptions(defaultOptions(GEMINI_3_PRO_PREVIEW))
            .build();
    }

    private GoogleGenAiChatOptions defaultOptions(String chatModel) {
        return GoogleGenAiChatOptions.builder()
            .model(chatModel)
            .temperature(0.7)
            .maxOutputTokens(512)
            .build();
    }
    private GoogleGenAiChatOptions defaultOptions(GoogleGenAiChatModel.ChatModel chatModel) {
        return defaultOptions(chatModel.value);
    }
}
