package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role getRoleByName(String name) {
        Role role = entityManager.createQuery("select r from Role r where name = :name", Role.class)
                .setParameter("name", name)
                .getSingleResult();
        if (role == null) {
            throw new IllegalArgumentException(String.format("Role '%s' not found", name));
        }
        return role;
    }

    @Override
    public List<Role> getListOfRoles() {
        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
    }
}
