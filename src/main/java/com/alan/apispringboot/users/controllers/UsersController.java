package com.alan.apispringboot.users.controllers;

import com.alan.apispringboot.Message;
import com.alan.apispringboot.security.CurrentUser;
import com.alan.apispringboot.auth.dtos.UserDTO;
import com.alan.apispringboot.users.services.UsersService;
import com.amazonaws.AmazonServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    private static final int MAX_AVATAR_FILE_SIZE = 1048576; // 1MB = 1024 * 1024 bytes = 1_048_576 bytes
    private static final List<String> ALLOWED_AVATAR_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg");
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile image) throws Exception {
        logger.info("Upload avatar: " + image.getOriginalFilename() + "- size: " + image.getSize());
        try {

            // TODO: Move this validation to a service
            if (image.getSize() > MAX_AVATAR_FILE_SIZE) {
                double sizeInMb = MAX_AVATAR_FILE_SIZE / 1048576.0;
                double sizeInMbActual = image.getSize() / 1048576.0;
                logger.error("Avatar file size is too large: " + sizeInMb + "MB");
                return new ResponseEntity<>(
                        new Message("File size must be less than " + sizeInMb + " MB, " + sizeInMbActual + " MB given"),
                        HttpStatus.BAD_REQUEST);
            }
            if (!ALLOWED_AVATAR_FILE_TYPES.contains(image.getContentType())) {
                logger.error("Avatar file type is not allowed: " + image.getContentType());
                return new ResponseEntity<>(
                        new Message("File type must be " + ALLOWED_AVATAR_FILE_TYPES + ", " + image.getContentType() + " given"),
                        HttpStatus.BAD_REQUEST);
            }

            CurrentUser currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            logger.info("Current user: " + currentUser.getUsername());
            UserDTO userUpdated = usersService.uploadAvatar(currentUser.getId(), image);
            logger.info("Avatar uploaded, user: " + userUpdated.toString());
            return new ResponseEntity<>(new Message("Avatar uploaded"), HttpStatus.OK);
        }catch (AmazonServiceException e) {
            logger.error("Error {} occurred while uploading avatar", e.getLocalizedMessage());
            return new ResponseEntity<>(new Message("Error:" + e.getMessage()), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            return new ResponseEntity<>(new Message("Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/public-profile")
    public ResponseEntity<?> getAllUsersPublicData() {
        logger.info("Get all users public data");
        List<UserDTO> usersDTO = usersService.getAllUsersPublicData();
        logger.info("Success get all users public data");
        return new ResponseEntity<List<UserDTO>>(usersDTO, HttpStatus.OK);
    }
}
