package ru.clevertec.authservice.adapter.input.web.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.authservice.adapter.input.web.users.dto.AuthUserDto;
import ru.clevertec.authservice.adapter.input.web.users.dto.JwtResponse;
import ru.clevertec.authservice.adapter.input.web.users.dto.RegisterUserDto;
import ru.clevertec.authservice.port.input.AuthenticationUseCase;
import ru.clevertec.authservice.port.input.RegistrationUseCase;
import ru.clevertec.authservice.port.input.ValidatePortUseCase;
import ru.clevertec.authservice.port.input.command.AuthorizationCommand;
import ru.clevertec.authservice.port.input.command.JwtUseCaseResult;
import ru.clevertec.authservice.port.input.command.RegistrationCommand;

@RestController
@RequestMapping(AuthController.URL_AUTH_ROOT)
@RequiredArgsConstructor
@Tag(name = "Registration and authentication")
public class AuthController {

    public static final String URL_REG = "/registration";
    public static final String URL_AUTH = "/authentication";
    public static final String URL_VALIDATE = "/validate";
    public static final String URL_AUTH_ROOT = "/auth";

    private final RegistrationUseCase registrationUseCase;
    private final AuthenticationUseCase authenticationUseCase;
    private final ValidatePortUseCase validatePortUseCase;

    private final UserWebMapper userMapper;

    @Operation(summary = "Registration user")
    @PostMapping(URL_REG)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<JwtResponse> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        RegistrationCommand command = userMapper.dtoToCommand(registerUserDto);
        JwtUseCaseResult useCaseResult = registrationUseCase.register(command);
        JwtResponse jwtResponse = userMapper.useCaseToDto(useCaseResult);

        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Authentication user")
    @PostMapping(URL_AUTH)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JwtResponse> authenticate(@RequestBody @Valid AuthUserDto authUserDto) {
        AuthorizationCommand command = userMapper.dtoToCommand(authUserDto);
        JwtUseCaseResult useCaseResult = authenticationUseCase.authenticate(command);
        JwtResponse jwtResponse = userMapper.useCaseToDto(useCaseResult);

        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @Operation(summary = "Validate token")
    @GetMapping(URL_VALIDATE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authHeader) {
        boolean useCaseResult = validatePortUseCase.validate(authHeader);
        return new ResponseEntity<>(useCaseResult, HttpStatus.OK);
    }
}
