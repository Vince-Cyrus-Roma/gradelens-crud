package com.gradelens2.gradelens_crud.controller;

import com.gradelens2.gradelens_crud.model.Role;
import com.gradelens2.gradelens_crud.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ROLE API
 *
 *  POST   /api/roles        create a role   body: { "roleName": "Teacher" }   -> 201
 *  GET    /api/roles        list all roles                                    -> 200
 *  GET    /api/roles/{id}   get one role                                      -> 200 / 404
 *  PUT    /api/roles/{id}   rename a role   body: { "roleName": "Student" }   -> 200 / 404 / 409
 *  DELETE /api/roles/{id}   delete a role                                     -> 204 / 404 / 409 (still used)
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Long id, @RequestBody Role role) {
        return roleService.updateRole(id, role);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }
}