package com.gradelens2.gradelens_crud.service;

import com.gradelens2.gradelens_crud.exceptions.DuplicateResourceException;
import com.gradelens2.gradelens_crud.exceptions.ResourceNotFoundException;
import com.gradelens2.gradelens_crud.model.User;
import com.gradelens2.gradelens_crud.repository.RoleRepository;
import com.gradelens2.gradelens_crud.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private static final String ACTIVE = "ACTIVE";
    private static final String DISABLED = "DISABLED";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // CREATE
    public User createUser(User user) {
        requireText(user.getFirstName(), "firstName");
        requireText(user.getLastName(), "lastName");
        requireText(user.getEmail(), "email");
        requireText(user.getPasswordHash(), "password");
        checkRoleExists(user.getRoleId());

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email already registered: " + user.getEmail());
        }

        user.setUserId(null); // the database generates the id
        user.setCreatedAt(LocalDateTime.now());
        user.setAccountStatus(ACTIVE);
        return userRepository.save(user);
    }

    // READ (all)
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by("userId"));
    }

    // READ (one)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    // UPDATE (send the full user; "password" and "accountStatus" are optional)
    public User updateUser(Long id, User newData) {
        User user = getUserById(id);

        requireText(newData.getFirstName(), "firstName");
        requireText(newData.getLastName(), "lastName");
        requireText(newData.getEmail(), "email");
        checkRoleExists(newData.getRoleId());

        if (userRepository.existsByEmailAndUserIdNot(newData.getEmail(), id)) {
            throw new DuplicateResourceException("Email already registered: " + newData.getEmail());
        }

        user.setFirstName(newData.getFirstName());
        user.setLastName(newData.getLastName());
        user.setEmail(newData.getEmail());
        user.setRoleId(newData.getRoleId());

        if (newData.getPasswordHash() != null && !newData.getPasswordHash().isBlank()) {
            user.setPasswordHash(newData.getPasswordHash());
        }
        if (newData.getAccountStatus() != null) {
            requireValidStatus(newData.getAccountStatus());
            user.setAccountStatus(newData.getAccountStatus());
        }

        return userRepository.save(user);
    }

    
    // DELETE = real delete: the row is removed from the database
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    // The role id must be given and must exist in the roles table
    private void checkRoleExists(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("roleId is required");
        }
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Role not found with id " + roleId);
        }
    }

    private void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

    private void requireValidStatus(String status) {
        if (!ACTIVE.equals(status) && !DISABLED.equals(status)) {
            throw new IllegalArgumentException("accountStatus must be ACTIVE or DISABLED");
        }
    }
}