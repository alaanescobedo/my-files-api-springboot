package com.alan.apispringboot.users.services;

import com.alan.apispringboot.auth.dtos.AuthUserDTO;
import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.exceptions.NotFoundException;
import com.alan.apispringboot.files.services.AwsS3Service;
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

import java.util.*;
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
    private AwsS3Service cloudFilesService;

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
    public User getUserById(Long id) {
        try {
            User user = usersRepository.findById(id).orElse(null);
            if (user == null) {
                throw new NotFoundException("User with id: " + id);
            }
            return user;
        } catch (Exception e) {
            throw new NotFoundException("User with id: " + id);
        }
    }

    @Override
    public User registerUser(AuthUserDTO registerDto) {
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
    public UserDTO uploadAvatar(Long id, MultipartFile avatar) throws Exception {
        try {
            logger.info("Uploading avatar");
            User user = getUserById(id);

            FilePublicDTO filePublicDTO = cloudFilesService.uploadAvatar(avatar);
            logger.info("Result: " + filePublicDTO.toString());

            FilePublic avatarFile = filePublicService.saveFile(filePublicDTO);

            logger.info("File Avatar: " + avatarFile.toString());
            user.setAvatar(avatarFile);
            usersRepository.save(user);
            return mapUserToUserDTO(user);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading avatar " + e.getMessage());
        }
    }


    @Override
    public FilePublic uploadPublicFile(Long id, MultipartFile file) throws Exception {
        try {
            logger.info("Uploading public file");
            User user = getUserById(id);

            FilePublicDTO filePublicDTO = cloudFilesService.uploadPublicFile(file);
            // filePublicDTO.setOwner(user);
            logger.info("Result: " + filePublicDTO.toString());

            FilePublic filePublic = filePublicService.saveFile(filePublicDTO);

            logger.info("File public: " + filePublic.toString());
            // user.getFilesPublic().add(filePublic);
            usersRepository.save(user);
            return filePublic;
        } catch (Exception e) {
            throw new RuntimeException("Error uploading public file " + e.getMessage());
        }
    }

    @Override
    public List<UserDTO> getAllUsersPublicData() {
        List<User> users = usersRepository.findAll();
        logger.info("Users: " + users.toString());
        List<UserDTO> usersDTO = mapUsersToUsersDTO(users);
        logger.info("UsersDTO: " + usersDTO.toString());
        return usersDTO;
    }

    // @Override
    // public List<FilePublicDTO> getAllPublicFilesByUsername(String username) {
    //     try {
    //         User user = getUserByUsername(username);
    //         Set<FilePublic> filesPublic = user.getFilesPublic();

    //         return filePublicService.mapListFilePublicToDTOS(new ArrayList<>(filesPublic));
    //     } catch (Exception e) {
    //         throw new RuntimeException("Error getting public files " + e.getMessage());
    //     }
    // }

    private List<UserDTO> mapUsersToUsersDTO(List<User> users) {
        return users.stream().map(user -> mapUserToUserDTO(user)).collect(Collectors.toList());
    }
}

