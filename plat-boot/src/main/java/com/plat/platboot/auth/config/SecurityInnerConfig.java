package com.plat.platboot.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
class SecurityInnerConfig {

    @Bean
    ObjectMapper mapper() {
        return new ObjectMapper();
    }

}
