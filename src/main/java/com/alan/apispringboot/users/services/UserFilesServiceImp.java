package com.alan.apispringboot.users.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alan.apispringboot.files.dtos.FilePublicDTO;
import com.alan.apispringboot.files.entities.FilePublic;
import com.alan.apispringboot.files.services.AwsS3Service;
import com.alan.apispringboot.files.services.FilePublicService;
import com.alan.apispringboot.users.entities.User;
import com.alan.apispringboot.users.repositories.UsersRepository;

@Service
public class UserFilesServiceImp implements UserFilesService {

  private static final Logger logger = LoggerFactory.getLogger(UserFilesServiceImp.class);

  @Autowired
  private UsersService usersService;
  @Autowired
  private UsersRepository usersRepository;

  @Autowired
  private AwsS3Service cloudFilesService;
  @Autowired
  private FilePublicService filePublicService;

  @Override
  public FilePublicDTO uploadPublicFile(User user, MultipartFile file) throws Exception {
    try {
      FilePublicDTO filePublicDTO = cloudFilesService.uploadPublicFile(file);
      filePublicDTO.setOwner(user);
      FilePublic filePublic= filePublicService.saveFile(filePublicDTO);

      return filePublicService.mapFilePublicToDTO(filePublic);
    } catch (Exception e) {
      throw new RuntimeException("Error uploading public file " + e.getMessage());
    }
  }

  @Override
  public FilePublicDTO uploadAvatar(User user, MultipartFile avatarFile) throws Exception {
    try {
      FilePublicDTO filePublicDTO = cloudFilesService.uploadPublicFile(avatarFile);
      filePublicDTO.setOwner(user);
      FilePublic avatar = filePublicService.saveFile(filePublicDTO);

      user.setAvatar(avatar);
      usersRepository.save(user);

      return filePublicService.mapFilePublicToDTO(avatar);
    } catch (Exception e) {
      throw new RuntimeException("Error setting avatar " + e.getMessage());
    }
  }

  @Override
  public List<FilePublicDTO> getAllPublicFilesByUsername(String username) {
    logger.info("Getting all public files by username");
    try {
      User user = usersService.getUserByUsername(username);
      return filePublicService.getFilesByOwner(user);
    } catch (Exception e) {
      throw new RuntimeException("Error getting public files " + e.getMessage());
    }
  }

}
