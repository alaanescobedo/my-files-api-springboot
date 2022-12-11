package com.alan.apispringboot.users.repositories;

import com.alan.apispringboot.users.entities.Role;
import com.alan.apispringboot.users.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleEnum roleName);
}
