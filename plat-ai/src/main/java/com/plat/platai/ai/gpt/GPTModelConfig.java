package com.plat.platai.ai.gpt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GPTModelConfig {

    @Bean
    ChatGPT51Client chatGPT51Client(OpenAiChatModel chatModel) {
        return new ChatGPT51Client(gpt51Client(chatModel));
    }

    @Bean("gpt51Client")
    ChatClient gpt51Client(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
            .defaultOptions(defaultOptions("gpt-5.1"))
            .build();

    }

    private OpenAiChatOptions defaultOptions(String chatModel) {
        return OpenAiChatOptions.builder()
            .model(chatModel)
            .temperature(0.7)
            .maxTokens(512)
            .build();
    }
    private OpenAiChatOptions defaultOptions(OpenAiApi.ChatModel chatModel) {
        return defaultOptions(chatModel.value);
    }
}
