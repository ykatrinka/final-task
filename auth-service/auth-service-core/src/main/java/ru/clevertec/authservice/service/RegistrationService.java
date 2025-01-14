package ru.clevertec.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.authservice.domain.User;
import ru.clevertec.authservice.port.input.RegistrationUseCase;
import ru.clevertec.authservice.port.input.command.JwtUseCaseResult;
import ru.clevertec.authservice.port.input.command.RegistrationCommand;
import ru.clevertec.authservice.port.output.RegistrationPort;
import ru.clevertec.authservice.util.JwtService;
import ru.clevertec.authservice.util.UserMapper;

@Service
@RequiredArgsConstructor
public class RegistrationService implements RegistrationUseCase {

    private final RegistrationPort userAdapter;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Override
    public JwtUseCaseResult register(RegistrationCommand registrationCommand) {
        User user = userMapper.commandToDomain(registrationCommand);
        User savedUser = userAdapter.register(user);

        String token = jwtService.generateToken(savedUser);
        return new JwtUseCaseResult(token);
    }

}
