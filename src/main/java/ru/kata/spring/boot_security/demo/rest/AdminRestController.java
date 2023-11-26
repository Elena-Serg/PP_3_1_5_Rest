package ru.kata.spring.boot_security.demo.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminRestController {
    private final UserService userService;

    public AdminRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/updateUser")
    public ResponseEntity<HttpStatus> updateUser(@Valid @RequestBody User user) {
        userService.updateUserById(user.getId(), user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam("id") int id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}