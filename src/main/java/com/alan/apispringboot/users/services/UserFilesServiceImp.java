package com.alan.apispringboot.users.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alan.apispringboot.files.dtos.FilePublicDTO;
import com.alan.apispringboot.files.entities.FilePublic;
import com.alan.apispringboot.files.services.AwsS3Service;
import com.alan.apispringboot.files.services.CloudinaryService;
import com.alan.apispringboot.files.services.FilePublicService;
import com.alan.apispringboot.files.services.IFilePublicService;
import com.alan.apispringboot.users.entities.User;
import com.alan.apispringboot.users.entities.UserAvatar;
import com.alan.apispringboot.users.repositories.UserAvatarRepository;
import com.alan.apispringboot.users.repositories.UsersRepository;

@Service
public class UserFilesServiceImp implements UserFilesService {

    private static final Logger logger = LoggerFactory.getLogger(UserFilesServiceImp.class);

    @Autowired
    private UsersService usersService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserAvatarRepository userAvatarRepository;

    @Autowired
    private AwsS3Service cloudFilesService;
    @Autowired
    private CloudinaryService avatarFilesService;
    @Autowired
    private IFilePublicService filePublicService;
    @Autowired
    private UserFilesValidationService userFilesValidationService;

    @Override
    public FilePublicDTO uploadPublicFile(User user, MultipartFile file) throws Exception {
        logger.info("Uploading public file for user " + user.getUsername());
        try {
            FilePublicDTO filePublicDTO = cloudFilesService.uploadPublicFile(file);
            user.setCloudUploadsCount(user.getCloudUploadsCount() + 1);
            filePublicDTO.setOwner(user);
            FilePublic filePublic = filePublicService.saveFile(filePublicDTO);

            return filePublicService.mapFilePublicToDTO(filePublic);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading public file " + e.getMessage());
        }
    }

    @Override
    public void uploadAvatar(User user, MultipartFile avatarFile) throws Exception {
        logger.info("Uploading avatar for user " + user.getUsername());
        try {
            FilePublicDTO filePublicDTO = avatarFilesService.uploadAvatar(avatarFile);

            UserAvatar userAvatar = new UserAvatar();
            userAvatar.setKey(filePublicDTO.getKey());
            userAvatar.setUrl(filePublicDTO.getUrl());
            userAvatar.setPublicName(filePublicDTO.getPublicName());
            UserAvatar userAvatarCreated = userAvatarRepository.save(userAvatar);
            user.setAvatar(userAvatarCreated);
            usersRepository.save(user);

        } catch (Exception e) {
            throw new RuntimeException("Error setting avatar " + e.getMessage());
        }
    }

    @Override
    public List<FilePublicDTO> getAllPublicFilesByUsername(String username) {
        logger.info("Getting all public files by username {} ", username);
        try {
            User user = usersService.getUserByUsername(username);
            return filePublicService.getFilesByOwner(user);
        } catch (Exception e) {
            throw new RuntimeException("Error getting public files " + e.getMessage());
        }
    }

    @Override
    public Long deletePublicFile(User user, Long fileId) {
        logger.info("Deleting public file with id " + fileId);
        try {
            FilePublic filePublic = filePublicService.getFilePublicById(fileId);
            userFilesValidationService.validateOwner(user, filePublic.getOwner().getId());
            filePublicService.deleteFileById(filePublic.getId());
            return filePublic.getId();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting public file " + e.getMessage());
        }
    }

}
