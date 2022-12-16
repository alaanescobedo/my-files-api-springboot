package com.alan.apispringboot.users.services;


import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.exceptions.NotFoundException;
import com.alan.apispringboot.users.entities.User;
import com.alan.apispringboot.users.repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsersServiceImp implements UsersService {

    private static final Logger logger = LoggerFactory.getLogger(UsersServiceImp.class);

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public User getUserByUsername(String username) {
        logger.info("Getting user by username: " + username);
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
        logger.info("Getting user by id: " + id);
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
    public List<UserDTO> getAllUsersPublicData() {
        logger.info("Getting all users public data");
        try {
            List<User> users = usersRepository.findAll();
            List<UserDTO> usersDTO = mapUsersToUsersDTO(users);
            return usersDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error getting all users public data " + e.getMessage());
        }
    }

    @Override
    public UserDTO mapUserToUserDTO(User user) {
        logger.info("Mapping user to userDTO {}", user.getUsername());
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .address(user.getAddress())
                .roles(user.getRoles())
                .avatar(user.getAvatar())
                .suscription(user.getSuscription())
                .build();
    }

    private List<UserDTO> mapUsersToUsersDTO(List<User> users) {
        logger.info("Mapping users to usersDTO");
        return users.stream().map(user -> mapUserToUserDTO(user)).collect(Collectors.toList());
    }
}
