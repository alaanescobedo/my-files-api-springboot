package com.alan.apispringboot.users.controllers;

import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.users.services.UsersService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @GetMapping("/public-profiles")
    public ResponseEntity<?> getAllUsersPublicData() {
        logger.info("Get all users public data");
        List<UserDTO> usersDTO = usersService.getAllUsersPublicData();
        return new ResponseEntity<List<UserDTO>>(usersDTO, HttpStatus.OK);
    }
}
