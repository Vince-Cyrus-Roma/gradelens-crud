package com.gradelens2.gradelens_crud.service;

import com.gradelens2.gradelens_crud.exceptions.DuplicateResourceException;
import com.gradelens2.gradelens_crud.exceptions.ResourceNotFoundException;
import com.gradelens2.gradelens_crud.model.Role;
import com.gradelens2.gradelens_crud.repository.RoleRepository;
import com.gradelens2.gradelens_crud.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public Role createRole(Role role) {
        requireRoleName(role);

        if (roleRepository.existsByRoleName(role.getRoleName())) {
            throw new DuplicateResourceException("Role already exists: " + role.getRoleName());
        }

        role.setRoleId(null); // the database generates the id
        return roleRepository.save(role);
    }

    // READ (all)
    public List<Role> getAllRoles() {
        return roleRepository.findAll(Sort.by("roleId"));
    }

    // READ (one)
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + id));
    }

    // UPDATE (rename)
    public Role updateRole(Long id, Role newData) {
        Role role = getRoleById(id);
        requireRoleName(newData);

        if (roleRepository.existsByRoleNameAndRoleIdNot(newData.getRoleName(), id)) {
            throw new DuplicateResourceException("Role already exists: " + newData.getRoleName());
        }

        role.setRoleName(newData.getRoleName());
        return roleRepository.save(role);
    }

    // DELETE (a real delete, but blocked while users still have this role)
    public void deleteRole(Long id) {
        Role role = getRoleById(id);

        if (userRepository.existsByRoleId(id)) {
            throw new DuplicateResourceException("Cannot delete: this role is still assigned to users");
        }

        roleRepository.delete(role);
    }

    private void requireRoleName(Role role) {
        if (role.getRoleName() == null || role.getRoleName().isBlank()) {
            throw new IllegalArgumentException("roleName is required");
        }
    }
}