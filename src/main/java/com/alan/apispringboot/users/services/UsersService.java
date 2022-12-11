package com.alan.apispringboot.users.services;

import com.alan.apispringboot.auth.dtos.RegisterDTO;
import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.users.entities.User;

public interface UsersService {
    public User getUserByUsername(String username);
    public User registerUser(RegisterDTO registerDto);
    public void saveRefreshToken(String refreshToken, String username);
    public UserDTO mapUserToUserDTO(User user);
}
