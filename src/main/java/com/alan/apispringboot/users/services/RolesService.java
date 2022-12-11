package com.alan.apispringboot.users.services;

import com.alan.apispringboot.users.entities.Role;
import com.alan.apispringboot.users.enums.RoleEnum;

import java.util.Optional;

public interface RolesService {
    public Role getRoleByName(RoleEnum roleName);
}
