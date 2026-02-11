package com.plat.platboot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class I18nResponseAdvice implements ResponseBodyAdvice<Object> {

    private final MessageSource messageSource;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Map<?, ?> map) {
            Locale locale = LocaleContextHolder.getLocale();
            Map<String, Object> resolved = new LinkedHashMap<>();
            for (var entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof String code) {
                    try {
                        value = messageSource.getMessage(code, null, locale);
                    } catch (NoSuchMessageException ignored) {
                    }
                }
                resolved.put(String.valueOf(entry.getKey()), value);
            }
            return resolved;
        }
        return body;
    }
}