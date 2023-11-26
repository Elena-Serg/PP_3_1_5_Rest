package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void deleteUserById(int id);

    void updateUserById(int id, User user);

    void addUser(User user);

    User getUserByEmail(String email);

}
