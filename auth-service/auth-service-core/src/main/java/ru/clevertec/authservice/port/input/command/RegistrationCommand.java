package ru.clevertec.authservice.port.input.command;

import lombok.Builder;
import ru.clevertec.authservice.domain.Role;

@Builder
public record RegistrationCommand(
        String username,
        String email,
        String password,
        Role role
) {
}
