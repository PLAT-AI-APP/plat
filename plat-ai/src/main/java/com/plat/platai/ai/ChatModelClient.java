package com.plat.platai.ai;

import org.springframework.ai.chat.messages.Message;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatModelClient {

    Flux<String> stream(String system, List<Message> messages);

    String call(String system, String userMessage);
}
