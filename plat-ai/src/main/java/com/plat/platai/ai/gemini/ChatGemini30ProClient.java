package com.plat.platai.ai.gemini;

import com.plat.platai.ai.AbstractChatModelClient;
import com.plat.platai.ai.ChatModelClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;

public class ChatGemini30ProClient extends AbstractChatModelClient implements ChatModelClient {

    public ChatGemini30ProClient(@Qualifier("gemini30ProChatClient") ChatClient chatClient) {
        super(chatClient);
    }
}
