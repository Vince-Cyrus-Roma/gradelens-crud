package com.gradelens2.gradelens_crud.repository;

import com.gradelens2.gradelens_crud.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByRoleName(String roleName);

    // Used on update: does ANOTHER role already have this name?
    boolean existsByRoleNameAndRoleIdNot(String roleName, Long roleId);
}