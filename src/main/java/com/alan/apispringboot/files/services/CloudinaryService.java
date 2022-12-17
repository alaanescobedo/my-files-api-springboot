package com.alan.apispringboot.files.services;

import com.alan.apispringboot.files.dtos.FilePublicDTO;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);
    private Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(
            @Value("${cloudinary.cloudName}") String cloudName,
            @Value("${cloudinary.apiKey}") String apiKey,
            @Value("${cloudinary.apiSecret}") String apiSecret) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        cloudinary = new Cloudinary(config);

    }

    public FilePublicDTO uploadAvatar(MultipartFile file) throws IOException {
        try {
            logger.info("Uploading file to cloudinary");
            // TODO: Find a way to get the file name without the extension, this works in
            // the most common cases
            String fileName = file.getOriginalFilename().split("\\.")[0];

            Map<String, String> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "avatars",
                    "public_id", fileName,
                    "format", "webp",
                    "allowed_formats", "webp, png, jpg, jpeg",
                    "transformation", "c_fill,h_200,w_200",
                    "overwrite", "true"));
            FilePublicDTO fileData = new FilePublicDTO();
            fileData.setUrl(uploadResult.get("url"));
            fileData.setKey(uploadResult.get("public_id"));
            fileData.setPublicName(uploadResult.get("public_id"));
            return fileData;
        } catch (Exception e) {
            logger.error("Error uploading file to cloudinary: " + e.getMessage());
            throw new IOException(e);
        }
    }

}