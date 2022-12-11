package com.alan.apispringboot.users.services;

import com.alan.apispringboot.auth.dtos.RegisterDTO;
import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.database.PostgreErrorCodes;
import com.alan.apispringboot.exceptions.NotFoundException;
import com.alan.apispringboot.users.entities.Address;
import com.alan.apispringboot.users.entities.Role;
import com.alan.apispringboot.users.entities.User;
import com.alan.apispringboot.users.enums.RoleEnum;
import com.alan.apispringboot.users.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UsersServiceImp implements UsersService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesService rolesService;

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
        String hashedPassword = passwordEncoder.encode(registerDto.getPassword());
        try {
            Role userRole = rolesService.getRoleByName(RoleEnum.ROLE_USER);
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);

            User userCreated = new User();
            userCreated.setUsername(registerDto.getUsername());
            userCreated.setEmail(registerDto.getEmail());
            userCreated.setPassword(hashedPassword);
            userCreated.setRoles(roles);
            userCreated.setAddress(registerDto.getAddress());
            return usersRepository.save(userCreated);
        } catch (Exception e) {
            if (e.hashCode() == PostgreErrorCodes.UNIQUE_VIOLATION.getCode()) {
                throw new RuntimeException("Username or email already exists");
            }
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
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }


}
