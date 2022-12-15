package com.alan.apispringboot.users.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alan.apispringboot.files.services.AwsRekognitionService;

@Service
public class UserFilesValidationServiceImp implements UserFilesValidationService {

  private static final int MAX_AVATAR_FILE_SIZE = 1_048_576; // 1MB = 1024 * 1024 bytes = 1_048_576 bytes
  private static final int MAX_PUBLIC_FILE_SIZE = 3_145_728; // 3MB = 3 * 1024 * 1024 bytes = 3_145_728 bytes
  private static final List<String> ALLOWED_AVATAR_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg");
  private static final List<String> ALLOWED_PUBLIC_FILE_TYPES = Arrays.asList("application/pdf");

  @Autowired
  private AwsRekognitionService moderationService;

  public void isAvatarFileValid(MultipartFile image) {
    try {
      moderationService.detectImage(image);
      if (image.getSize() > MAX_AVATAR_FILE_SIZE) {
        throw new RuntimeException("Avatar file size is too large");
      }
      if (!ALLOWED_AVATAR_FILE_TYPES.contains(image.getContentType())) {
        throw new RuntimeException("Avatar file type is not allowed");
      }
    } catch (Exception e) {
      throw new RuntimeException("Error validating avatar file " + e.getMessage());
    }
  }

  public void isPublicFileValid(MultipartFile image) {
    if (image.getSize() > MAX_PUBLIC_FILE_SIZE) {
      throw new RuntimeException("Public file size is too large");
    }
    if (!ALLOWED_PUBLIC_FILE_TYPES.contains(image.getContentType())) {
      throw new RuntimeException("Public file type is not allowed");
    }
  }

}
