package com.alan.apispringboot.files.services;

import com.alan.apispringboot.files.FilePublic;
import com.alan.apispringboot.files.FilePublicDTO;
import com.alan.apispringboot.files.FilePublicRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilePublicService {

    @Autowired
    private FilePublicRepository filePublicRepository;

    public FilePublic saveFile(FilePublicDTO filePublicDTO) {
        FilePublic filePublic = new FilePublic();
        filePublic.setUrl(filePublicDTO.getUrl());
        filePublic.setKey(filePublicDTO.getKey());
        // filePublic.setPublicName(filePublicDTO.getPublicName());
        return filePublicRepository.save(filePublic);
    }

    public List<FilePublicDTO> mapListFilePublicToDTOS(List<FilePublic> files) {
        return files.stream().map(file -> {
            FilePublicDTO filePublicDTO = new FilePublicDTO();
            filePublicDTO.setUrl(file.getUrl());
            filePublicDTO.setId(file.getId());
            // filePublicDTO.setPublicName(file.getPublicName());
            // filePublicDTO.setOwner(file.getOwner());
            return filePublicDTO;
        }).collect(Collectors.toList());
    }
}
