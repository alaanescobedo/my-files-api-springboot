package com.alan.apispringboot.files.services;

import com.alan.apispringboot.exceptions.NotFoundException;
import com.alan.apispringboot.files.dtos.FilePublicDTO;
import com.alan.apispringboot.files.entities.FilePublic;
import com.alan.apispringboot.files.repositories.FilePublicRepository;
import com.alan.apispringboot.users.entities.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FilePublicService {

    private static final Logger logger = LoggerFactory.getLogger(FilePublicService.class);

    @Autowired
    private FilePublicRepository filePublicRepository;

    public FilePublicDTO getFilePublicById(Long id) throws NotFoundException {
        logger.info("Getting file by id: " + id);
        FilePublic filePublic = filePublicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("File not found"));
        return mapFilePublicToDTO(filePublic);
    }

    public List<FilePublicDTO> getFilesByOwner(User owner) {
        logger.info("Getting files by owner: " + owner);
        Set<FilePublic> files = filePublicRepository.findAllByOwner(owner);
//        logger.info("Files: " + files.());
        return mapListFilePublicToDTOS(files);
    }

    public FilePublic saveFile(FilePublicDTO filePublicDTO) {
        logger.info("Saving file: " + filePublicDTO.getPublicName());
        FilePublic filePublic = mapFilePublicDTOToEntity(filePublicDTO);
        return filePublicRepository.save(filePublic);
    }

    public void deleteFileById(Long id) {
        logger.info("Deleting file by id: " + id);
        try {
            filePublicRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting file by id: " + id);
            throw e;
        }
    }

    public FilePublic mapFilePublicDTOToEntity(FilePublicDTO filePublicDTO) {
        logger.info("Mapping file to entity: " + filePublicDTO.getPublicName());
        FilePublic filePublic = new FilePublic();
        filePublic.setUrl(filePublicDTO.getUrl());
        filePublic.setKey(filePublicDTO.getKey());
        filePublic.setPublicName(filePublicDTO.getPublicName());
        filePublic.setOwner(filePublicDTO.getOwner());
        return filePublic;
    }

    public FilePublicDTO mapFilePublicToDTO(FilePublic filePublic) {
        logger.info("Mapping file to DTO: " + filePublic.getPublicName());
        FilePublicDTO filePublicDTO = new FilePublicDTO();
        filePublicDTO.setUrl(filePublic.getUrl());
        filePublicDTO.setId(filePublic.getId());
        filePublicDTO.setPublicName(filePublic.getPublicName());
        filePublicDTO.setOwner(filePublic.getOwner());
        return filePublicDTO;
    }

    public List<FilePublicDTO> mapListFilePublicToDTOS(Set<FilePublic> files) {
        logger.info("Mapping list of files to DTOs, size: " + files.size());
        return files.stream().map(file -> mapFilePublicToDTO(file)).collect(Collectors.toList());
    }

    public String generateKey(String fileName) {
        logger.info("Generating key for file: " + fileName);
        return UUID.randomUUID().toString() + "_" + fileName;
    }
}
