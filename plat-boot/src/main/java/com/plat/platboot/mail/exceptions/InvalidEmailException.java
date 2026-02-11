package com.plat.platboot.mail.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidEmailException extends EmailException {

    private final List<String> invalidAddresses;

    public InvalidEmailException(String errorEmail) {
        super("이메일 형식이 올바르지 않습니다. " + errorEmail);
        this.invalidAddresses = List.of(errorEmail);
    }

    public InvalidEmailException(List<String> errorEmails) {
        super("이메일 형식이 올바르지 않습니다. " + errorEmails);
        this.invalidAddresses = errorEmails;
    }
}
