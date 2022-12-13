package com.alan.apispringboot.files.services;


import com.alan.apispringboot.files.AWSConfig;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;

@Service
public class AwsRekognitionService {

    private static final Logger logger = LoggerFactory.getLogger(AwsRekognitionService.class);

    @Autowired
    private AWSConfig awsConfig;

    public DetectModerationLabelsResult detectModerationLabels(MultipartFile imageToCheck) throws IOException {
        try {
            logger.info("Detecting moderation labels for image: " + imageToCheck.getOriginalFilename());
            DetectModerationLabelsRequest request = new DetectModerationLabelsRequest()
                    .withImage(new Image().withBytes(ByteBuffer.wrap(imageToCheck.getBytes())))
                    .withMinConfidence(50F);
            logger.info("Detection completed, request: " + request.toString());
            return awsConfig.amazonRekognition().detectModerationLabels(request);
        } catch (Exception e) {
            logger.error("Error {} occurred while detecting moderation labels", e.getLocalizedMessage());
            throw e;
        }
    }

}