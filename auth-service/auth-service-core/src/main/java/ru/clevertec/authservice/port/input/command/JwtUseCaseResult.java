package ru.clevertec.authservice.port.input.command;

import lombok.Builder;

@Builder
public record JwtUseCaseResult(
        String token
) {
}
