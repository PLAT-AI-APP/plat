package com.plat.platboot.mail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MailPropertiesConfig {

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean mailSmtpAuth;
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int mailSmtpTimeout;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean mailSmtpStartTls;

    @Bean
    Properties mailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", mailSmtpAuth);
        properties.put("mail.smtp.timeout", mailSmtpTimeout);
        properties.put("mail.smtp.starttls.enable", mailSmtpStartTls);
        return properties;
    }
}
