package ru.kata.spring.boot_security.demo.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserDao userDao, RoleService roleService, @Lazy PasswordEncoder encoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.getAllUsers().stream()
                .sorted(Comparator.comparingInt(User::getId))
                .toList();
    }

    @Override
    public User getUserByFirstName(String firstName) {
        return userDao.getUserByFirstName(firstName);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public void addUser(User user) {
        prepareToUserBeforeSave(user);
        userDao.addUser(user);
    }

    @Override
    public void deleteUserById(int id) {
        userDao.deleteUserById(id);
    }

    @Override
    public void updateUserById(int id, User user) {
        prepareToUserBeforeSave(user);
        userDao.updateUserById(id, user);
    }

    private void prepareToUserBeforeSave(User user) {
        user.setRoles(user.getRoles().stream().map(role -> roleService.getRoleByName(role.getName())).toList());
        user.setPassword(encoder.encode(user.getPassword()));
    }
}
