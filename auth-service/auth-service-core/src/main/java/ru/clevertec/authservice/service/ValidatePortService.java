package ru.clevertec.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.authservice.port.input.ValidatePortUseCase;
import ru.clevertec.authservice.util.JwtService;
import ru.clevertec.exceptionstarter.exception.AuthenticationException;

@Service
@RequiredArgsConstructor
public class ValidatePortService implements ValidatePortUseCase {

    public static final String INVALID_TOKEN = "Invalid token";
    public static final String BEARER = "Bearer ";

    private final JwtService jwtService;

    @Override
    public boolean validate(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            throw AuthenticationException.getInstance(INVALID_TOKEN);
        }
        String token = authHeader.substring(7);
        return jwtService.isTokenValid(token);
    }
}
