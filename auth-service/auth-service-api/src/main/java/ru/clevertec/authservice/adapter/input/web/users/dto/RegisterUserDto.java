package ru.clevertec.authservice.adapter.input.web.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import ru.clevertec.authservice.domain.Role;

@Builder
@Schema(description = "Registration query")
public record RegisterUserDto(
        @NotNull
        @Length(min = 3, max = 50)
        @Schema(defaultValue = "Patrik", description = "Username")
        String username,

        @NotNull
        @Length(min = 3, max = 255)
        @Email
        @Schema(defaultValue = "patrik@gmail.com", description = "Email")
        String email,

        @NotNull
        @Length(min = 3, max = 255)
        @Schema(defaultValue = "123", description = "Password")
        String password,

        @NotNull
        @Schema(defaultValue = "SUBSCRIBER", description = "Role")
        Role role
) {
}
