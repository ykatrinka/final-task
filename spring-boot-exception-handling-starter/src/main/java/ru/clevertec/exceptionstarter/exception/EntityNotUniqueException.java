package ru.clevertec.exceptionstarter.exception;

import org.springframework.stereotype.Component;

@Component
public class EntityNotUniqueException extends RuntimeException {

    private EntityNotUniqueException(String message) {
        super(message);
    }

    public static EntityNotUniqueException getInstance(String message) {
        return new EntityNotUniqueException(message);
    }
}
