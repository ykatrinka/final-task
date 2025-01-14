package ru.clevertec.authservice.port.input;


import ru.clevertec.authservice.port.input.command.AuthorizationCommand;
import ru.clevertec.authservice.port.input.command.JwtUseCaseResult;

public interface AuthenticationUseCase {

    JwtUseCaseResult authenticate(AuthorizationCommand authorizationCommand);
}
