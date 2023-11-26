package ru.kata.spring.boot_security.demo.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return  userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        User userByEmail = userRepository.findByEmail(email);
        if (userByEmail == null) {
            throw new UserNotFoundException(String.format("User with email=%s not found", email));
        }
        return userByEmail;
    }

    @Override
    public void addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(int id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(String.format("User with id=%s not found", id));
        }
        userRepository.deleteById(id);
    }

    @Override
    public void updateUserById(int id, User user) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(String.format("User with id=%s not found", id));
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
