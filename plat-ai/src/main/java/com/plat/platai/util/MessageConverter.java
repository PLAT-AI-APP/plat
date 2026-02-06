package com.plat.platai.util;

import com.plat.platai.dto.ChatMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.List;

public final class MessageConverter {

    private MessageConverter() {}

    public static Message toSpringAiMessage(ChatMessage chatMessage) {
        return switch (chatMessage.getRole()) {
            case USER -> new UserMessage(chatMessage.getContent());
            case ASSISTANT -> new AssistantMessage(chatMessage.getContent());
            case SYSTEM -> new UserMessage(chatMessage.getContent());
        };
    }

    public static List<Message> toSpringAiMessages(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
            .map(MessageConverter::toSpringAiMessage)
            .toList();
    }
}
