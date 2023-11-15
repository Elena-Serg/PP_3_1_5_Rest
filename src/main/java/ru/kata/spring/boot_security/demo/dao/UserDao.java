package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    void addUser(User user);

    void updateUserById(int id, User user);

    void deleteUserById(int id);

    User getUserById(int id);

    User getUserByUsername(String username);

}
