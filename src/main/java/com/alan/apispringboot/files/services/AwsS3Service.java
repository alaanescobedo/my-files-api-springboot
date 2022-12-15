package com.alan.apispringboot.files.services;

import com.alan.apispringboot.files.AWSConfig;
import com.alan.apispringboot.files.FilePublicDTO;
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
import java.time.LocalDateTime;

@Service
public class AwsS3Service {

    @Autowired
    private AWSConfig awsConfig;

    @Autowired
    private AwsRekognitionService awsRekognitionService;

    @Value("${aws.bucketName}")
    private String s3BucketName;

    @Value("${aws.bucketNamePrivate}")
    private String s3BucketNamePrivate;

    @Value("${aws.region}")
    private String s3Region;

    private static final Logger logger = LoggerFactory.getLogger(AwsS3Service.class);

    public FilePublicDTO uploadAvatar(MultipartFile multipartFile) throws IOException {
        try {
            logger.info("Detecting moderation labels for image: " + multipartFile.getOriginalFilename());
            awsRekognitionService.detectImage(multipartFile);
            logger.info("Uploading avatar to S3");

            File file = convertMultiPartToFile(multipartFile);
            logger.info("Uploading file to s3: " + file.getName());
            String key = LocalDateTime.now() + "_" + file.getName();
            logger.info("Uploading file with name {}", key);

            PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, key, file);
            awsConfig.amazonS3().putObject(putObjectRequest);

            Files.delete(file.toPath()); // Remove the file locally created in the project

            String fileUrl = awsConfig.amazonS3().getUrl(s3BucketName, key).toString();

            return createFilePublicDTO(file, fileUrl, key);
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

    public FilePublicDTO uploadPublicFile(MultipartFile multipartFile) throws IOException {
        try {
            logger.info("Uploading file to S3");

            File file = convertMultiPartToFile(multipartFile);
            logger.info("Uploading file to s3: " + file.getName());
            String key = LocalDateTime.now() + "_" + file.getName();
            logger.info("Uploading file with name {}", key);

            PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, key, file);
            awsConfig.amazonS3().putObject(putObjectRequest);

            Files.delete(file.toPath()); // Remove the file locally created in the project
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
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
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
