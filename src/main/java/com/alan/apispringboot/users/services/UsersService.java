package com.alan.apispringboot.users.services;

import com.alan.apispringboot.auth.dtos.RegisterDTO;
import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.files.FilePublic;
import com.alan.apispringboot.users.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public interface UsersService {
    public User getUserByUsername(String username);
    public User registerUser(RegisterDTO registerDto);
    public void saveRefreshToken(String refreshToken, String username);
    public UserDTO mapUserToUserDTO(User user);
    public UserDTO uploadAvatar(Long id, MultipartFile image) throws Exception;
    public List<UserDTO> getAllUsersPublicData();
}
