package com.plat.platai.ai.gemini;

import com.plat.platai.ai.AbstractChatModelClient;
import com.plat.platai.ai.ChatModelClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;

public class ChatGemini25ProClient extends AbstractChatModelClient implements ChatModelClient {

    public ChatGemini25ProClient(@Qualifier("gemini25ProChatClient") ChatClient chatClient) {
        super(chatClient);
    }
}
