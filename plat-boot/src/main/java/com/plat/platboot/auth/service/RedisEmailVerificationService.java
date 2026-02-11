package com.plat.platboot.auth.service;

import com.plat.platboot.mail.service.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class RedisEmailVerificationService implements EmailVerificationService {

    private static final String KEY_PREFIX = "email:verify:";
    private static final Duration CODE_TTL = Duration.ofSeconds(30);

    private final StringRedisTemplate redisTemplate;
    private final MailSender mailSender;

    @Override
    public void sendVerificationCode(String email) {
        String code = generateCode();
        redisTemplate.opsForValue().set(KEY_PREFIX + email, code, CODE_TTL);
        mailSender.sendAuth(email, code);
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String key = KEY_PREFIX + email;
        String storedCode = redisTemplate.opsForValue().get(key);
        if (code.equals(storedCode)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }

    private String generateCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }
}