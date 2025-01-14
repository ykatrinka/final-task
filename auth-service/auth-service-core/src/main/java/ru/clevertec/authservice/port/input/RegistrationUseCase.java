package ru.clevertec.authservice.port.input;


import ru.clevertec.authservice.port.input.command.JwtUseCaseResult;
import ru.clevertec.authservice.port.input.command.RegistrationCommand;

public interface RegistrationUseCase {

    JwtUseCaseResult register(RegistrationCommand registrationCommand);

}
