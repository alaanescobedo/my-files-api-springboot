package com.alan.apispringboot.files.services;

import java.util.List;

import com.alan.apispringboot.exceptions.NotFoundException;
import com.alan.apispringboot.files.dtos.FilePublicDTO;
import com.alan.apispringboot.files.entities.FilePublic;
import com.alan.apispringboot.users.entities.User;

public interface IFilePublicService {

  public FilePublic getFilePublicById(Long id) throws NotFoundException;

  public List<FilePublicDTO> getFilesByOwner(User owner);

  public FilePublic saveFile(FilePublicDTO filePublicDTO);

  public void deleteFileById(Long id);

  public FilePublic mapFilePublicDTOToEntity(FilePublicDTO filePublicDTO);

  public FilePublicDTO mapFilePublicToDTO(FilePublic filePublic);

  public List<FilePublicDTO> mapListFilePublicToDTOS(List<FilePublic> files);

  public String generateKey(String fileName);
}
