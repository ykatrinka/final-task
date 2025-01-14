package ru.clevertec.exceptionstarter.exception;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationException extends RuntimeException {

    private AuthenticationException(String message) {
        super(message);
    }

    public static AuthenticationException getInstance(String message) {
        return new AuthenticationException(message);
    }

}
