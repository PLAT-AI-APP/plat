package com.plat.platai.ai.claude;


import com.plat.platai.ai.AbstractChatModelClient;
import com.plat.platai.ai.ChatModelClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ChatSonnetClient extends AbstractChatModelClient implements ChatModelClient {

    public ChatSonnetClient(@Qualifier("sonnetChatClient") ChatClient chatClient) {
        super(chatClient);
    }

}
