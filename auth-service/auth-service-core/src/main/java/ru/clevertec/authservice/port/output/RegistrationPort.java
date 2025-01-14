package ru.clevertec.authservice.port.output;


import ru.clevertec.authservice.domain.User;

public interface RegistrationPort {
    User register(User user);
}