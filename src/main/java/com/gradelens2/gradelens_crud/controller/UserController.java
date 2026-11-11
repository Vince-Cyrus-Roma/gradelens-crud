package com.gradelens2.gradelens_crud.controller;

import com.gradelens2.gradelens_crud.model.User;
import com.gradelens2.gradelens_crud.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * USER API
 *
 *  POST   /api/users        create a user                                -> 201 / 400 / 404 / 409
 *  GET    /api/users        list all users                               -> 200
 *  GET    /api/users/{id}   get one user                                 -> 200 / 404
 *  PUT    /api/users/{id}   update a user (also used to change the role) -> 200 / 400 / 404 / 409
 *  DELETE /api/users/{id}   disable the user (soft delete)               -> 204 / 404
 *
 * Create body:
 *  { "firstName": "Maria", "lastName": "Reyes", "email": "maria@cit.edu",
 *    "password": "secret123", "roleId": 2 }
 *
 * Update body ("password" and "accountStatus" are optional):
 *  { "firstName": "Maria", "lastName": "Reyes", "email": "maria@cit.edu",
 *    "roleId": 2, "accountStatus": "ACTIVE" }
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

        @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}