package ru.clevertec.exceptionstarter.exception;

import org.springframework.stereotype.Component;

@Component
public class EntityNotFoundException extends RuntimeException {

    private EntityNotFoundException(String message) {
        super(message);
    }

    public static EntityNotFoundException getInstance(String message) {
        return new EntityNotFoundException(message);
    }

}
