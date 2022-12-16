package com.alan.apispringboot.users.services;

import org.springframework.web.multipart.MultipartFile;

import com.alan.apispringboot.users.entities.User;

public interface UserFilesValidationService {

  void validateAvatarFile(MultipartFile avatar);
  void validatePublicFile(MultipartFile file);
  void validateOwner(User user, Long ownerId);
  
}
