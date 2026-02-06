package com.plat.platai.ai.gpt;

import com.plat.platai.ai.AbstractChatModelClient;
import com.plat.platai.ai.ChatModelClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;

public class ChatGPT51Client extends AbstractChatModelClient implements ChatModelClient {

    public ChatGPT51Client(@Qualifier("gpt51Client") ChatClient chatClient) {
        super(chatClient);
    }
}
