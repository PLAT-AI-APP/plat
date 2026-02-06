package com.plat.platai.ai;

import com.plat.platai.ai.claude.ChatHaikuClient;
import com.plat.platai.ai.claude.ChatSonnetClient;
import com.plat.platai.ai.gemini.ChatGemini25FlashClient;
import com.plat.platai.ai.gemini.ChatGemini25ProClient;
import com.plat.platai.ai.gemini.ChatGemini30FlashClient;
import com.plat.platai.ai.gemini.ChatGemini30ProClient;
import com.plat.platai.ai.gpt.ChatGPT51Client;
import com.plat.platai.dto.ChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatModelClientHandler {

    private final ChatHaikuClient haikuClient;
    private final ChatSonnetClient sonnetClient;

    private final ChatGemini25FlashClient gemini25FlashClient;
    private final ChatGemini25ProClient gemini25ProClient;
    private final ChatGemini30FlashClient gemini30FlashClient;
    private final ChatGemini30ProClient gemini30ProClient;

    private final ChatGPT51Client chatGPT51Client;

    public ChatModelClient get(ChatModel chatModel) {
        return switch (chatModel) {
            case CLAUDE_HAIKU_4_5 -> haikuClient;
            case CLAUDE_SONNET_4_5 -> sonnetClient;
            case GEMINI_2_5_FLASH ->  gemini25FlashClient;
            case GEMINI_2_5_PRO ->  gemini25ProClient;
            case GEMINI_3_0_FLASH -> gemini30FlashClient;
            case GEMINI_3_0_PRO ->  gemini30ProClient;
            case GPT_5_1 -> chatGPT51Client;
        };
    }

    public ChatModelClient getSummarizationClient() {
        return haikuClient;
    }
}
