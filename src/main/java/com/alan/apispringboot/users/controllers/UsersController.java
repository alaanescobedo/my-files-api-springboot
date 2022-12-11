package com.alan.apispringboot.users.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    //Add logger
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar() {
        logger.info("Upload avatar");
        return ResponseEntity.ok("Upload avatar");
    }
}
