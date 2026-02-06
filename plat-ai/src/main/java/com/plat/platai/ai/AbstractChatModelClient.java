package com.plat.platai.ai;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import reactor.core.publisher.Flux;

import java.util.List;

@AllArgsConstructor
public abstract class AbstractChatModelClient implements ChatModelClient {

    private final ChatClient chatClient;

    @Override
    public Flux<String> stream(String system, List<Message> messages) {
        return chatClient.prompt()
            .system(system)
            .messages(messages)
            .stream()
            .content();
    }

    @Override
    public String call(String system, String userMessage) {
        return chatClient.prompt()
            .system(system)
            .user(userMessage)
            .call()
            .content();
    }
}
