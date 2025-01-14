package ru.clevertec.authservice.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.authservice.domain.User;
import ru.clevertec.authservice.port.input.command.AuthorizationCommand;
import ru.clevertec.authservice.port.input.command.RegistrationCommand;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User commandToDomain(RegistrationCommand registrationCommand);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User commandToDomain(AuthorizationCommand authorizationCommand);

}
