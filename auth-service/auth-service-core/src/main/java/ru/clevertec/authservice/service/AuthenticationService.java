package ru.clevertec.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.authservice.domain.User;
import ru.clevertec.authservice.port.input.AuthenticationUseCase;
import ru.clevertec.authservice.port.input.command.AuthorizationCommand;
import ru.clevertec.authservice.port.input.command.JwtUseCaseResult;
import ru.clevertec.authservice.port.output.AuthenticationPort;
import ru.clevertec.authservice.util.JwtService;
import ru.clevertec.authservice.util.UserMapper;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {

    private final AuthenticationPort userAdapter;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    @Override
    public JwtUseCaseResult authenticate(AuthorizationCommand authorizationCommand) {
        User user = userMapper.commandToDomain(authorizationCommand);
        User authUser = userAdapter.authenticate(user);

        String token = jwtService.generateToken(authUser);
        return new JwtUseCaseResult(token);
    }
}
