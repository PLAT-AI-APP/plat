package com.plat.platai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;


import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class LocaleTest {

    @Autowired
    private MessageSource ms;

    @Test
    void ko() {
        String message = ms.getMessage("timeout", null, Locale.KOREA);
        assertThat(message).isEqualTo("죄송합니다. 응답 시간이 초과되었습니다. 다시 시도해주세요.");
    }

    @Test
    void en() {
        String message = ms.getMessage("authentication", null, Locale.ENGLISH);
        assertThat(message).isEqualTo("Sorry, a server authentication error occurred. Please contact the administrator.");
    }

    @Test
    void jp() {
        String message = ms.getMessage("authentication", null, Locale.JAPAN);
        assertThat(message).isEqualTo("申し訳ございません。サーバー認証エラーが発生しました。管理者に問い合わせてください。");
    }
}