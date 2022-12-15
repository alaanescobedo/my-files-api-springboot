package com.alan.apispringboot.users.services;

import org.springframework.web.multipart.MultipartFile;

public interface UserFilesValidationService {

  void isAvatarFileValid(MultipartFile avatar);
  void isPublicFileValid(MultipartFile file);
  
}
