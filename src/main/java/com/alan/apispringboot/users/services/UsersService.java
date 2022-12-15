package com.alan.apispringboot.users.services;

import com.alan.apispringboot.auth.dtos.AuthUserDTO;
import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.files.FilePublic;
import com.alan.apispringboot.files.FilePublicDTO;
import com.alan.apispringboot.users.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UsersService {
    public User getUserByUsername(String username);
    public User getUserById(Long id);
    public User registerUser(AuthUserDTO registerDto);
    public void saveRefreshToken(String refreshToken, String username);
    public UserDTO mapUserToUserDTO(User user);
    public UserDTO uploadAvatar(Long id, MultipartFile file) throws Exception;
    public FilePublic uploadPublicFile(Long id, MultipartFile file) throws Exception;
    public List<UserDTO> getAllUsersPublicData();
    // public List<FilePublicDTO> getAllPublicFilesByUsername(String username);
}
