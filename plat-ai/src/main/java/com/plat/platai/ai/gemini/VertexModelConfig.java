package com.plat.platai.ai.gemini;//package ai.platchat.api.ai.gemini;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
//import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import static org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel.ChatModel.*;
//
//
//@Configuration
//public class VertexModelConfig {
//
//    @Bean
//    ChatGemini25FlashClient chatGemini25FlashClient(VertexAiGeminiChatModel chatModel) {
//        return new ChatGemini25FlashClient(gemini25FlashChatClient(chatModel));
//    }
//
//    @Bean
//    ChatGemini25ProClient chatGemini25ProClient(VertexAiGeminiChatModel chatModel) {
//        return new ChatGemini25ProClient(gemini25ProChatClient(chatModel));
//    }
//
//    @Bean
//    ChatGemini30FlashClient chatGemini30FlashClient(VertexAiGeminiChatModel chatModel) {
//        return new ChatGemini30FlashClient(gemini30FlashChatClient(chatModel));
//    }
//
//    @Bean
//    ChatGemini30ProClient chatGemini30ProClient(VertexAiGeminiChatModel chatModel) {
//        return new ChatGemini30ProClient(gemini30PRoChatClient(chatModel));
//    }
//
//    @Bean("gemini25FlashChatClient")
//    ChatClient gemini25FlashChatClient(VertexAiGeminiChatModel chatModel) {
//        return ChatClient.builder(chatModel)
//            .defaultOptions(defaultOptions(GEMINI_2_5_FLASH))
//            .build();
//    }
//
//    @Bean("gemini25ProChatClient")
//    ChatClient gemini25ProChatClient(VertexAiGeminiChatModel chatModel) {
//        return ChatClient.builder(chatModel)
//            .defaultOptions(defaultOptions(GEMINI_2_5_PRO))
//            .build();
//    }
//
//    @Bean("gemini30FlashChatClient")
//    ChatClient gemini30FlashChatClient(VertexAiGeminiChatModel chatModel) {
//        return ChatClient.builder(chatModel)
//            .defaultOptions(defaultOptions("gemini-3-flash-preview"))
//            .build();
//    }
//
//    @Bean("gemini30ProChatClient")
//    ChatClient gemini30PRoChatClient(VertexAiGeminiChatModel chatModel) {
//        return ChatClient.builder(chatModel)
//            .defaultOptions(defaultOptions("gemini-3.0-pro"))
//            .build();
//    }
//
//    private VertexAiGeminiChatOptions defaultOptions(VertexAiGeminiChatModel.ChatModel chatModel) {
//        return VertexAiGeminiChatOptions.builder()
//            .model(chatModel)
//            .temperature(0.7)
//            .maxOutputTokens(512)
//            .build();
//    }
//
//    private VertexAiGeminiChatOptions defaultOptions(String chatModel) {
//        return VertexAiGeminiChatOptions.builder()
//            .model(chatModel)
//            .temperature(0.7)
//            .maxOutputTokens(512)
//            .build();
//    }
//}
