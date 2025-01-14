package ru.clevertec.authservice.port.output;


import ru.clevertec.authservice.domain.User;

public interface AuthenticationPort {
    User authenticate(User user);
}