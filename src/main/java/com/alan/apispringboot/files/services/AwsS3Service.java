package com.alan.apispringboot.files.services;

import com.alan.apispringboot.files.AWSConfig;
import com.alan.apispringboot.files.dtos.FilePublicDTO;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class AwsS3Service {

    @Autowired
    private AWSConfig awsConfig;

    @Autowired
    private FilePublicService filePublicService;

    @Value("${aws.bucketName}")
    private String s3BucketName;

    @Value("${aws.bucketNamePrivate}")
    private String s3BucketNamePrivate;

    @Value("${aws.region}")
    private String s3Region;

    private static final Logger logger = LoggerFactory.getLogger(AwsS3Service.class);

    public FilePublicDTO uploadPublicFile(MultipartFile multipartFile) throws IOException {
        try {
            logger.info("Initiating upload to S3");

            File file = convertMultiPartToFile(multipartFile);
            String key = filePublicService.generateKey(file.getName());

            uploadToCloud(s3BucketName, key, file);
            String url = awsConfig.amazonS3().getUrl(s3BucketName, key).toString();

            return createFilePublicDTO(file, url, key);
        } catch (AmazonServiceException e) {
            logger.error("Error {} occurred while uploading file", e.getLocalizedMessage());
            throw e;
        } catch (IOException ex) {
            logger.error("Error {} occurred while deleting temporary file", ex.getLocalizedMessage());
            throw ex;
        } catch (Exception ex) {
            logger.error("Error {} occurred while uploading file", ex.getLocalizedMessage());
            throw ex;
        }
    }

    private File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
        try {
            logger.info("Converting multipart file to file");
            File file = new File(multipartFile.getOriginalFilename());
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(multipartFile.getBytes());
            fo.close();
            return file;
        } catch (IOException e) {
            logger.error("Error occurred while converting multipart file to file", e.getLocalizedMessage());
            throw e;
        }
    }

    private void uploadToCloud(String bucketName, String key, File file) throws IOException {
        logger.info("Uploading file to S3");
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, key, file);
            awsConfig.amazonS3().putObject(putObjectRequest);
            Files.delete(file.toPath()); // Remove the file locally created in the project
        } catch (IOException e) {
            logger.error("Error occurred while uploading file", e.getLocalizedMessage());
            throw e;
        }

    }

    private FilePublicDTO createFilePublicDTO(File file, String url, String key) {
        logger.info("Convert file to FilePublicDTO: " + file.getName());
        FilePublicDTO filePublicDTO = new FilePublicDTO();
        filePublicDTO.setUrl(url);
        filePublicDTO.setPublicName(file.getName());
        filePublicDTO.setKey(key);
        return filePublicDTO;
    }

}
