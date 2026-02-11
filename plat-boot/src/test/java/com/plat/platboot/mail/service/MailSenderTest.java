package com.plat.platboot.mail.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MailSenderTest {

    @Autowired
    private MailSender mailSender;

    @Test
    void sendAuth() {
        mailSender.sendAuth("tmd8635@gmail.com", "123456");
    }

}