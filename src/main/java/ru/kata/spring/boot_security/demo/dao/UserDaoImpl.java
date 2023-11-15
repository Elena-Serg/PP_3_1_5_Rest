package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select user from User user", User.class).getResultList();
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void deleteUserById(int id) {
        entityManager.remove(getUserById(id));
    }

    @Override
    public void updateUserById(int id, User user) {
        User userById = getUserById(id);
        userById.setName(user.getName());
        userById.setLastName(user.getLastName());
        userById.setAge(user.getAge());
        userById.setEmail(user.getEmail());
        userById.setPassword(user.getPassword());
        userById.setRoles(user.getRoles());
        entityManager.merge(userById);
    }

    @Override
    public User getUserById(int id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new IllegalArgumentException(String.format("User with id=%s not found", id));
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = entityManager.createQuery("select u from User u where name = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user;
    }
}

