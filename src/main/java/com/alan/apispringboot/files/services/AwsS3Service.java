package com.alan.apispringboot.files.services;

import com.alan.apispringboot.files.AWSConfig;
import com.alan.apispringboot.files.FilePublicDTO;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsResult;
import com.amazonaws.services.rekognition.model.ModerationLabel;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
import java.util.List;

@Service
public class AwsS3Service {

    @Autowired
    private AWSConfig awsConfig;

    @Autowired
    private AwsRekognitionService awsRekognitionService;

    @Value("${aws.bucketName}")
    private String s3BucketName;

    @Value("${aws.region}")
    private String s3Region;

    private static final Logger logger = LoggerFactory.getLogger(AwsS3Service.class);

    public FilePublicDTO upload(MultipartFile multipartFile) throws IOException {
        try {
            detectModerationLabels(multipartFile);

            File file = convertMultiPartToFile(multipartFile);
            logger.info("Uploading file to s3: " + file.getName());
            String fileName = LocalDateTime.now() + "_" + file.getName();
            logger.info("Uploading file with name {}", fileName);

            PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fileName, file);
            awsConfig.amazonS3().putObject(putObjectRequest);

            Files.delete(file.toPath()); // Remove the file locally created in the project
            return convertPutObjectResultToFilePublicDTO(putObjectRequest, fileName);
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

    private FilePublicDTO convertPutObjectResultToFilePublicDTO(PutObjectRequest result, String fileName) {
        FilePublicDTO filePublicDTO = new FilePublicDTO();
        filePublicDTO.setUrl(awsConfig.amazonS3().getUrl(s3BucketName, fileName).toString());
        filePublicDTO.setKey(result.getKey());
        return filePublicDTO;
    }

    private void detectModerationLabels(MultipartFile imageToCheck) throws IOException {
        try {
            DetectModerationLabelsResult result = awsRekognitionService.detectModerationLabels(imageToCheck);
            List<ModerationLabel> moderationLabels = result.getModerationLabels();
            if (moderationLabels.size() > 0) {
                logger.info("Image contains moderation labels");
                for (ModerationLabel moderationLabel : moderationLabels) {
                    logger.info("Moderation label: " + moderationLabel.getName());
                    logger.info("Confidence: " + moderationLabel.getConfidence());
                }
                throw new IOException("Image contains moderation labels");
            }
        } catch (AmazonServiceException e) {
            logger.error("Error {} occurred while detecting moderation labels", e.getLocalizedMessage());
            throw e;
        }
    }
}
