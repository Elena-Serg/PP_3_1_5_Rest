package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;
import javax.persistence.FetchType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "firstname", nullable = false)
    @NotEmpty(message = "Firstname should not be empty")
    @Size(min = 2, max = 20, message = "Firstname should be between 2 and 20 characters")
    private String firstName;
    @Column(name = "lastname", nullable = false)
    @NotEmpty(message = "Lastname should not be empty")
    @Size(min = 2, max = 20, message = "Lastname should be between 2 and 20 characters")
    private String lastName;
    @Column(name = "age", nullable = false)
    @Min(value = 0, message = "Age should be equal or greater than 0")
    @Max(value = 100, message = "Age should be equal or less than 100")
    private byte age;
    @Column(name = "email", unique = true, nullable = false)
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email is not valid")
    private String email;
    @Column(name = "password", nullable = false)
    @NotEmpty(message = "Password should not be empty")
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @NotEmpty(message = "Roles should not be empty")
    private List<Role> roles;

    public User() {
    }

    public User(String firstName, String password, String lastName, byte age, String email, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getStringOfRoles() {
        return roles.stream().map(Role::toString).collect(Collectors.joining(" "));
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
