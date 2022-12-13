package com.alan.apispringboot.files.services;

import com.alan.apispringboot.files.FilePublic;
import com.alan.apispringboot.files.FilePublicDTO;
import com.alan.apispringboot.files.FilePublicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilePublicService {

    @Autowired
    private FilePublicRepository filePublicRepository;

    public FilePublic saveFile(FilePublicDTO filePublicDTO) {
        FilePublic filePublic = new FilePublic();
        filePublic.setUrl(filePublicDTO.getUrl());
        filePublic.setKey(filePublicDTO.getKey());
        return filePublicRepository.save(filePublic);
    }

}
