package com.plat.platai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class PlatformRulesConfig {

    @Value("classpath:prompts/platform-rules.txt")
    private Resource platformRulesResource;

    @Bean
    public String platformRules() throws IOException {
        return platformRulesResource.getContentAsString(StandardCharsets.UTF_8);
    }
}
