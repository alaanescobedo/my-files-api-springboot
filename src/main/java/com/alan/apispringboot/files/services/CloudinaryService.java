package com.alan.apispringboot.files.services;

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
            @Value("${cloudinary.apiSecret}") String apiSecret
    ) {
        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        cloudinary = new Cloudinary(config);

    }

    public Map upload(MultipartFile file) throws IOException {
        try {
            logger.info("Uploading file to cloudinary");
            logger.info("Cloudinary: " + cloudinary);
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "avatars",
                    "public_id", file.getOriginalFilename(),
                    "format", "webp",
                    "allowed_formats", "webp, png, jpg, jpeg",
                    "moderation", "manual"
            ));
            logger.info("Upload result: " + uploadResult);
            logger.info("Upload result url: " + uploadResult.get("url"));
            logger.info("File id: " + uploadResult.get("public_id"));
            return uploadResult;
        } catch (Exception e) {
            logger.error("Error uploading file to cloudinary: " + e.getMessage());
            throw new IOException(e);
        }
    }

    public Map delete(String id) throws IOException {
//        Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
//        return result;
        return null;
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
}