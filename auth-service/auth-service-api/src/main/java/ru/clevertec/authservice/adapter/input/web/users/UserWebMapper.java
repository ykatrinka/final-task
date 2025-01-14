package ru.clevertec.authservice.adapter.input.web.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.authservice.adapter.input.web.users.dto.AuthUserDto;
import ru.clevertec.authservice.adapter.input.web.users.dto.JwtResponse;
import ru.clevertec.authservice.adapter.input.web.users.dto.RegisterUserDto;
import ru.clevertec.authservice.adapter.output.persistence.jpa.entity.UserEntity;
import ru.clevertec.authservice.domain.User;
import ru.clevertec.authservice.port.input.command.AuthorizationCommand;
import ru.clevertec.authservice.port.input.command.JwtUseCaseResult;
import ru.clevertec.authservice.port.input.command.RegistrationCommand;

@Mapper(componentModel = "spring")
public interface UserWebMapper {

    RegistrationCommand dtoToCommand(RegisterUserDto userDto);

    AuthorizationCommand dtoToCommand(AuthUserDto userDto);

    JwtResponse useCaseToDto(JwtUseCaseResult useCaseResult);

    UserEntity domainToEntity(User user);

    @Mapping(target = "authorities", ignore = true)
    User entityToDomain(UserEntity userEntity);
}
