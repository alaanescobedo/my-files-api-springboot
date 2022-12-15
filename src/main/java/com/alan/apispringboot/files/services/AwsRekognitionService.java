package com.alan.apispringboot.files.services;

import com.alan.apispringboot.files.AWSConfig;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.ModerationLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

@Service
public class AwsRekognitionService {

    private static final Logger logger = LoggerFactory.getLogger(AwsRekognitionService.class);

    @Autowired
    private AWSConfig awsConfig;

    private DetectModerationLabelsResult detectModerationLabels(MultipartFile imageToCheck) throws IOException {
        try {
            logger.info("Detecting moderation labels for image: " + imageToCheck.getOriginalFilename());
            DetectModerationLabelsRequest request = new DetectModerationLabelsRequest()
                    .withImage(new Image().withBytes(ByteBuffer.wrap(imageToCheck.getBytes())))
                    .withMinConfidence(50F);
            return awsConfig.amazonRekognition().detectModerationLabels(request);
        } catch (Exception e) {
            logger.error("Error {} occurred while detecting moderation labels", e.getLocalizedMessage());
            throw e;
        }
    }

    public void detectImage(MultipartFile imageToCheck) throws IOException {
        try {
            DetectModerationLabelsResult result = detectModerationLabels(imageToCheck);
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