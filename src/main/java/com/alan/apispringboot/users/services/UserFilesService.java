package com.alan.apispringboot.users.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.alan.apispringboot.files.dtos.FilePublicDTO;
import com.alan.apispringboot.users.entities.User;

public interface UserFilesService {

  FilePublicDTO uploadPublicFile(User user, MultipartFile file) throws Exception;

  void uploadAvatar(User user, MultipartFile file) throws Exception;

  List<FilePublicDTO> getAllPublicFilesByUsername(String username);

  Long deletePublicFile(User user, Long fileId) throws Exception;
}
