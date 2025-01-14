package ru.clevertec.exceptionstarter.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.exceptionstarter.exception.AuthenticationException;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;
import ru.clevertec.exceptionstarter.exception.EntityNotUniqueException;
import ru.clevertec.exceptionstarter.exception.IndexException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Component
public class GlobalExceptionHandling {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleEntityNotFoundExceptions(EntityNotFoundException e) {
        return createErrorMessage(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = {EntityNotUniqueException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleEntityNotUniqueExceptions(EntityNotUniqueException e) {
        return createErrorMessage(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleAuthenticationExceptions(AuthenticationException e) {
        return createErrorMessage(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(value = {IndexException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleIndexExceptions(IndexException e) {
        return createErrorMessage(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidationException(
            MethodArgumentNotValidException e) {

        Map<String, String> mapErrors = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors()
                .forEach(error -> {
                    String field = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
                    String message = error.getDefaultMessage();
                    if (mapErrors.containsKey(field)) {
                        mapErrors.put(field, mapErrors.get(field) + " = " + message);
                    } else {
                        mapErrors.put(field, message);
                    }
                });

        return createErrorMessage(HttpStatus.BAD_REQUEST, mapErrors.toString());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleExceptions(Exception e) {
        return createErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }


    public ErrorMessage createErrorMessage(HttpStatus status, String message) {
        return new ErrorMessage(status.value(), status.name(), message);
    }

    public record ErrorMessage(
            int statusCode,
            String status,
            String message

    ) {
    }
}
