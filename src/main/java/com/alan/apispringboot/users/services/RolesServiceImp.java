package com.alan.apispringboot.users.services;

import com.alan.apispringboot.exceptions.NotFoundException;
import com.alan.apispringboot.users.entities.Role;
import com.alan.apispringboot.users.enums.RoleEnum;
import com.alan.apispringboot.users.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RolesServiceImp implements RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public Role getRoleByName(RoleEnum roleName) {
        try{
            return rolesRepository.findByRoleName(roleName).get();
        } catch (Exception e) {
            throw new NotFoundException("Role with name: " + roleName);
        }
    }
}
