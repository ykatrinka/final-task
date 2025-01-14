package ru.clevertec.authservice.adapter.output.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.clevertec.authservice.domain.User;
import ru.clevertec.authservice.port.output.AuthenticationPort;
import ru.clevertec.authservice.service.UserService;
import ru.clevertec.exceptionstarter.exception.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthenticationUserAdapter implements AuthenticationPort {

    public static final String USERNAME_OR_PASSWORD_IS_INCORRECT = "Username or password is incorrect";

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Override
    public User authenticate(User user) {
        User authUser = userService.loadUserByUsername(user.getUsername());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword()
            ));

        } catch (BadCredentialsException e) {
            throw AuthenticationException.getInstance(USERNAME_OR_PASSWORD_IS_INCORRECT);
        }
        return authUser;
    }
}
