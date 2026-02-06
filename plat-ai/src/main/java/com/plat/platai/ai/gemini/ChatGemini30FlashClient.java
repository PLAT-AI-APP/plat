package com.plat.platai.ai.gemini;

import com.plat.platai.ai.AbstractChatModelClient;
import com.plat.platai.ai.ChatModelClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;

public class ChatGemini30FlashClient extends AbstractChatModelClient implements ChatModelClient {
    public ChatGemini30FlashClient(@Qualifier("gemini30FlashChatClient") ChatClient chatClient) {
        super(chatClient);
    }
}
