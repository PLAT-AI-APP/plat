package com.plat.platai.ai.claude;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.anthropic.api.AnthropicApi.ChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ClaudeModelConfig {


    @Bean
    ChatHaikuClient chatHaikuClient(AnthropicChatModel chatModel) {
        return new ChatHaikuClient(haikuChatClient(chatModel));
    }
    @Bean
    ChatSonnetClient chatSonnetClient(AnthropicChatModel chatModel) {
        return new ChatSonnetClient(sonnetChatClient(chatModel));
    }

    @Bean("haikuChatClient")
    ChatClient haikuChatClient(AnthropicChatModel chatModel) {
        return ChatClient.builder(chatModel)
            .defaultOptions(defaultOptions(ChatModel.CLAUDE_HAIKU_4_5))
            .build();
    }

    @Bean("sonnetChatClient")
    ChatClient sonnetChatClient(AnthropicChatModel chatModel) {
        return ChatClient.builder(chatModel)
            .defaultOptions(defaultOptions(ChatModel.CLAUDE_SONNET_4_5))
            .build();
    }

    private AnthropicChatOptions defaultOptions(ChatModel chatModel) {
        return AnthropicChatOptions.builder()
            .model(chatModel)
            .temperature(0.7)
            .maxTokens(1024)
            .build();
    }

}
