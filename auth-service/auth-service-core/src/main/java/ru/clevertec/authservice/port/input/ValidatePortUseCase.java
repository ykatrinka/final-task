package ru.clevertec.authservice.port.input;


public interface ValidatePortUseCase {
    boolean validate(String authHeader);
}
