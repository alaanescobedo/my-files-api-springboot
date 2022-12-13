package com.alan.apispringboot.users.services;

import com.alan.apispringboot.auth.dtos.RegisterDTO;
import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.exceptions.NotFoundException;
import com.alan.apispringboot.files.services.AwsS3Service;
import com.alan.apispringboot.files.services.CloudinaryService;
import com.alan.apispringboot.files.FilePublic;
import com.alan.apispringboot.files.FilePublicDTO;
import com.alan.apispringboot.files.services.FilePublicService;
import com.alan.apispringboot.users.entities.Role;
import com.alan.apispringboot.users.entities.User;
import com.alan.apispringboot.users.enums.RoleEnum;
import com.alan.apispringboot.users.repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsersServiceImp implements UsersService {

    private static final Logger logger = LoggerFactory.getLogger(UsersServiceImp.class);

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private FilePublicService filePublicService;

    @Autowired
    private AwsS3Service imgCloudStoreService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        try {
            User user = usersRepository.findByUsername(username).orElse(null);
            if (user == null) {
                throw new NotFoundException("User with username: " + username);
            }
            return user;
        } catch (Exception e) {
            throw new NotFoundException(username);
        }
    }

    @Override
    public User registerUser(RegisterDTO registerDto) {
        try {
            String hashedPassword = passwordEncoder.encode(registerDto.getPassword());
            Boolean UserExists = usersRepository.existsByUsername(registerDto.getUsername());
            Boolean EmailExists = usersRepository.existsByEmail(registerDto.getEmail());
            if (UserExists && EmailExists) {
                throw new Exception("Username and email already exists");
            } else if (UserExists) {
                throw new Exception("Username already exists");
            } else if (EmailExists) {
                throw new Exception("Email already exists");
            }

            Role userRole = rolesService.getRoleByName(RoleEnum.ROLE_USER);
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);

            User userCreated = new User();
            userCreated.setUsername(registerDto.getUsername());
            userCreated.setEmail(registerDto.getEmail());
            userCreated.setPassword(hashedPassword);
            userCreated.setRoles(roles);
            userCreated.setAddress(registerDto.getAddress());
            userCreated.setAvatar(null);
            return usersRepository.save(userCreated);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user " + e.getMessage());
        }
    }

    @Override
    public void saveRefreshToken(String refreshToken, String username) {
        String hashedRefreshToken = passwordEncoder.encode(refreshToken);
        User user = getUserByUsername(username);
        user.setRefreshToken(hashedRefreshToken);
        usersRepository.save(user);
    }

    @Override
    public UserDTO mapUserToUserDTO(User user) {
        logger.info("Mapping user to userDTO");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        logger.info("User id: " + user.getId());
        userDTO.setUsername(user.getUsername());
        logger.info("User username: " + user.getUsername());
        userDTO.setEmail(user.getEmail());
        logger.info("User email: " + user.getEmail());
        userDTO.setAddress(user.getAddress());
        logger.info("User address: " + user.getAddress());
        userDTO.setRoles(user.getRoles());
        logger.info("User roles: " + user.getRoles());
        FilePublic avatar = user.getAvatar();
        logger.info("User avatar: " + user.getAvatar());
        userDTO.setAvatar(avatar);
        logger.info("User avatar: " + avatar);
        logger.info("UserDTO: >>>" + userDTO.toString());
        return userDTO;
    }

    @Override
    public UserDTO uploadAvatar(Long id, MultipartFile image) throws IOException {
        try {
            User user = usersRepository.findById(id).orElse(null);
            logger.info("User: " + user);
            if (user == null) {
                throw new NotFoundException("User with id: " + id);
            }
            FilePublicDTO filePublicDTO = imgCloudStoreService.upload(image);
            logger.info("Result: " + filePublicDTO.toString());

            FilePublic avatar = filePublicService.saveFile(filePublicDTO);
            logger.info("Avatar: " + avatar);
            user.setAvatar(avatar);
            User userUpdated = usersRepository.save(user);
            return mapUserToUserDTO(userUpdated);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading avatar " + e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllUsersPublicData() {
        List<User> users = usersRepository.findAll();
        logger.info("Users: " + users.toString());
        List<UserDTO> usersDTO = users.stream().map(user -> mapUserToUserDTO(user)).collect(Collectors.toList());
        logger.info("UsersDTO: " + usersDTO.toString());
        return usersDTO;
    }
}

