package com.alan.apispringboot.users.controllers;

import com.alan.apispringboot.Message;
import com.alan.apispringboot.files.dtos.FilePublicDTO;
import com.alan.apispringboot.files.entities.FilePublic;
import com.alan.apispringboot.users.entities.User;
import com.alan.apispringboot.users.services.UserAuthService;
import com.alan.apispringboot.users.services.UserFilesService;
import com.alan.apispringboot.users.services.UserFilesValidationService;
import com.amazonaws.AmazonServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user-files")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserFilesController {

  private static final Logger logger = LoggerFactory.getLogger(UserFilesController.class);

  @Autowired
  private UserFilesValidationService userFilesValidation;
  @Autowired
  private UserFilesService userFilesService;
  @Autowired
  private UserAuthService userAuthService;

  @PostMapping("/avatar")
  public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile avatarFile) throws Exception {
    logger.info("Upload avatar: " + avatarFile.getOriginalFilename() + "- size: " + avatarFile.getSize());
    try {
      userFilesValidation.validateAvatarFile(avatarFile);

      User user = userAuthService.getCurrentUser();
      userFilesService.uploadAvatar(user, avatarFile);
      return new ResponseEntity<>(new Message("Avatar uploaded"), HttpStatus.OK);
    } catch (AmazonServiceException e) {
      logger.error("Error {} occurred while uploading avatar", e.getLocalizedMessage());
      return new ResponseEntity<>(new Message("Error:" + e.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      logger.error("Error: " + e.getMessage());
      return new ResponseEntity<>(new Message("Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/public-file")
  public ResponseEntity<?> uploadPublicFile(@RequestParam("file") MultipartFile file) {
    logger.info("Upload public file: " + file.getOriginalFilename() + "- size: " + file.getSize());
    try {
      userFilesValidation.validatePublicFile(file);
      User user = userAuthService.getCurrentUser();

      userFilesValidation.validateLimitOfFilesPerUser(user);
      FilePublicDTO filePublicCreated = userFilesService.uploadPublicFile(user, file);
      return new ResponseEntity<FilePublicDTO>(filePublicCreated, HttpStatus.CREATED);
    } catch (AmazonServiceException e) {
      logger.error("Error {} occurred while uploading public file", e.getLocalizedMessage());
      return new ResponseEntity<>(new Message("Error:" + e.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      logger.error("Error: " + e.getMessage());
      return new ResponseEntity<>(new Message("Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{username}/public-files")
  public ResponseEntity<?> getPublicFilesByUserId(@PathVariable String username) {
    logger.info("Get public files by username: " + username);
    List<FilePublicDTO> files = userFilesService.getAllPublicFilesByUsername(username);
    return new ResponseEntity<List<FilePublicDTO>>(files, HttpStatus.OK);
  }

  @DeleteMapping("/public-file/{fileId}")
  public ResponseEntity<?> deletePublicFile(@PathVariable Long fileId) {
    logger.info("Delete public file by id: " + fileId);
    try {
      User user = userAuthService.getCurrentUser();
      Long fileDeletedId = userFilesService.deletePublicFile(user, fileId);
      return new ResponseEntity<>(new Message("File deleted with id: " + fileDeletedId), HttpStatus.OK);
    } catch (Exception e) {
      logger.error("Error: " + e.getMessage());
      return new ResponseEntity<>(new Message("Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
