package ru.clevertec.exceptionstarter.exception;

import org.springframework.stereotype.Component;

@Component
public class IndexException extends RuntimeException {

    private IndexException(String message) {
        super(message);
    }

    public static IndexException getInstance(String message) {
        return new IndexException(message);
    }

}
