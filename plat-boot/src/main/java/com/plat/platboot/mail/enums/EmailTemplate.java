package com.plat.platboot.mail.enums;

import com.plat.platboot.mail.exceptions.MailTemplateNotLoadedException;
import lombok.Getter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public enum EmailTemplate {

    AUTH("templates/emailAuth.html"),
    ;

    private final String resourcePath;
    private final String cachedTemplate;  // 캐시된 템플릿

    EmailTemplate(String resourcePath) {
        this.resourcePath = resourcePath;
        this.cachedTemplate = loadTemplate(resourcePath);
    }

    private static String loadTemplate(String path) {
        try {
            var resource = new ClassPathResource(path);
            return StreamUtils.copyToString(
                resource.getInputStream(),
                StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new MailTemplateNotLoadedException(path);
        }
    }

    public String toHTMLString() {
        return cachedTemplate;
    }

    public String toHTMLString(Map<String, Object> variables) {
        return setVariable(cachedTemplate, variables);
    }

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(\\w+)\\}\\}");

    private String setVariable(String template, Map<String, Object> variables) {
        var sb = new StringBuilder();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);

        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = variables.get(key);

            String replacement = value != null ?
                Matcher.quoteReplacement(String.valueOf(value)) :
                matcher.group(0);  // 변수 없으면 원본 유지

            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
}