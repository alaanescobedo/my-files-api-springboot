package com.alan.apispringboot.users.services;

import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.users.entities.User;

import java.util.List;

public interface UsersService {
    public User getUserByUsername(String username);
    public User getUserById(Long id);
    public UserDTO mapUserToUserDTO(User user);
    public List<UserDTO> getAllUsersPublicData();
}
