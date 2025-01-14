package ru.clevertec.authservice.adapter.input.web.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
@Schema(description = "Authorization query")
public record AuthUserDto(
        @NotNull
        @Length(min = 3, max = 50)
        @Schema(defaultValue = "Patrik", description = "Username")
        String username,

        @NotNull
        @Length(min = 3, max = 255)
        @Schema(defaultValue = "123", description = "Password")
        String password
) {
}
