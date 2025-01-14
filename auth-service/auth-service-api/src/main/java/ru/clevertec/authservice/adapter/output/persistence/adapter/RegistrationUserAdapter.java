package ru.clevertec.authservice.adapter.output.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.authservice.adapter.input.web.users.UserWebMapper;
import ru.clevertec.authservice.adapter.output.persistence.jpa.entity.UserEntity;
import ru.clevertec.authservice.adapter.output.persistence.jpa.repository.UserRepository;
import ru.clevertec.authservice.domain.User;
import ru.clevertec.authservice.port.output.RegistrationPort;
import ru.clevertec.exceptionstarter.exception.EntityNotUniqueException;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationUserAdapter implements RegistrationPort {

    public static final String USERNAME_IS_EXISTS = "Username %s is already in use";
    public static final String EMAIL_IS_EXISTS = "Email %s is already in use";

    private final UserRepository userRepository;
    private final UserWebMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw EntityNotUniqueException.getInstance(
                    String.format(USERNAME_IS_EXISTS, user.getUsername())
            );
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw EntityNotUniqueException.getInstance(
                    String.format(EMAIL_IS_EXISTS, user.getEmail())
            );
        }

        UserEntity userEntity = userMapper.domainToEntity(user);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity savedUser = userRepository.save(userEntity);

        return userMapper.entityToDomain(savedUser);
    }

}
