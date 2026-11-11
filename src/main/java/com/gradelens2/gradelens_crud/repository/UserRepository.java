package com.gradelens2.gradelens_crud.repository;

import com.gradelens2.gradelens_crud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    // Used on update: does ANOTHER user already have this email?
    boolean existsByEmailAndUserIdNot(String email, Long userId);

    // Used before deleting a role: is any user still using it?
    boolean existsByRoleId(Long roleId);
}