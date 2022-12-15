package com.alan.apispringboot.files.services;

import com.alan.apispringboot.exceptions.NotFoundException;
import com.alan.apispringboot.files.FilePublic;
import com.alan.apispringboot.files.FilePublicDTO;
import com.alan.apispringboot.files.FilePublicRepository;
import com.alan.apispringboot.users.entities.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilePublicService {

    private static final Logger logger = LoggerFactory.getLogger(FilePublicService.class);

    @Autowired
    private FilePublicRepository filePublicRepository;

    public List<FilePublicDTO> getFilesByOwner(User owner) {
        logger.info("Getting files by owner: " + owner);
        Set<FilePublic> files = filePublicRepository.findAllByOwner(owner);
        return mapListFilePublicToDTOS(files);
    }

    public FilePublic saveFile(FilePublicDTO filePublicDTO) {
        logger.info("Saving file: " + filePublicDTO.getPublicName());
        FilePublic filePublic = new FilePublic();
        filePublic.setUrl(filePublicDTO.getUrl());
        filePublic.setKey(filePublicDTO.getKey());
        filePublic.setPublicName(filePublicDTO.getPublicName());
        filePublic.setOwner(filePublicDTO.getOwner());
        return filePublicRepository.save(filePublic);
    }

    public List<FilePublicDTO> mapListFilePublicToDTOS(Set<FilePublic> files) {
        logger.info("Mapping list of files to DTOs, size: " + files.size());
        return files.stream().map(file -> {
            FilePublicDTO filePublicDTO = new FilePublicDTO();
            filePublicDTO.setUrl(file.getUrl());
            filePublicDTO.setId(file.getId());
            filePublicDTO.setPublicName(file.getPublicName());
            filePublicDTO.setOwner(file.getOwner());
            return filePublicDTO;
        }).collect(Collectors.toList());
    }
}
