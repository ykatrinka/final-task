package ru.clevertec.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.authservice.adapter.input.web.users.UserWebMapper;
import ru.clevertec.authservice.adapter.output.persistence.jpa.repository.UserRepository;
import ru.clevertec.authservice.domain.User;
import ru.clevertec.exceptionstarter.exception.EntityNotFoundException;

@Component
@RequiredArgsConstructor
@Transactional
public class UserService {

    public static final String USERNAME_NOT_FOUND = "User by username %s not found";

    private final UserRepository userRepository;
    private final UserWebMapper userMapper;

    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::entityToDomain)
                .orElseThrow(() -> EntityNotFoundException.getInstance(
                        String.format(USERNAME_NOT_FOUND, username)
                ));

    }

    public UserDetailsService userDetailsService() {
        return this::loadUserByUsername;
    }

}
