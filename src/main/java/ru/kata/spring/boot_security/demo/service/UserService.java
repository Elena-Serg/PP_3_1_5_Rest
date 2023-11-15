package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    void deleteUserById(int id);

    void updateUserById(int id, User user);

    User getUserById(int id);

    void addUser(User user);

    User getUserByUsername(String username);

}
