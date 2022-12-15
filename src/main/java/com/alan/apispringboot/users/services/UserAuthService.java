package com.alan.apispringboot.users.services;

import org.springframework.security.core.Authentication;

import com.alan.apispringboot.auth.dtos.AuthUserDTO;
import com.alan.apispringboot.auth.dtos.RegisterUserDTO;
import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.users.entities.User;

public interface UserAuthService {
  void registerUser(RegisterUserDTO registerDto);
  void saveRefreshToken(String refreshToken, String username);
  Authentication createAuthentication(AuthUserDTO authUserDTO);
  UserDTO getCurrentUserDTO();
  User getCurrentUser();
  Authentication getAuthentication();
}
