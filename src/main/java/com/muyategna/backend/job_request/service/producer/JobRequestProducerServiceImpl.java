package com.muyategna.backend.job_request.service.producer;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.job_request.dto.request_media.JobRequestMediaCreateDto;
import com.muyategna.backend.rabbitmq.model.QueueSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
public class JobRequestProducerServiceImpl implements JobRequestProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public JobRequestProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void enqueueUploadJobRequestFiles(Long jobRequestId, List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {

            if (file == null || file.isEmpty()) {
                log.info("File is empty");
                continue;
            }

            String ext = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
            boolean extIsValid = Constants.IMAGE_EXTENSIONS.contains(ext) ||
                    Constants.VIDEO_EXTENSIONS.contains(ext) ||
                    Constants.DOCUMENT_EXTENSIONS.contains(ext);

            if (!extIsValid) {
                log.info("File extension is not valid for one of the file types uploaded");
                continue;
            }

            // Validate file size
            if (Constants.IMAGE_EXTENSIONS.contains(ext) && file.getSize() > Constants.IMAGE_MAX_SIZE) {
                continue;
            } else if (Constants.VIDEO_EXTENSIONS.contains(ext) && file.getSize() > Constants.VIDEO_MAX_SIZE) {
                continue;
            } else if (Constants.DOCUMENT_EXTENSIONS.contains(ext) && file.getSize() > Constants.DOCUMENT_MAX_SIZE) {
                continue;
            }

            JobRequestMediaCreateDto message = JobRequestMediaCreateDto.builder()
                    .newFilename(UUID.randomUUID().toString())
                    .originalFilename(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .jobRequestId(jobRequestId)
                    .content(file.getBytes())
                    .extension(ext)
                    .build();
            QueueSpec queueSpec = QueueSpec.withBaseName(Constants.JOB_REQUEST_FILE_UPLOAD_QUEUE_BASE_NAME);
            rabbitTemplate.convertAndSend(
                    queueSpec.exchange(),
                    queueSpec.routingKey(),
                    message);
        }
    }

//    @Override
//    public void enqueueGenerateJobLeadsForJobRequest(Long jobRequestId) {
//        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.JOB_LEAD_GENERATION_QUEUE_BASE_NAME);
//        rabbitTemplate.convertAndSend(
//                queueSpec.exchange(),
//                queueSpec.routingKey(),
//                jobRequestId);
//    }


    private String getFileExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        if (idx < 0 || idx == filename.length() - 1) {
            throw new IllegalArgumentException("Invalid file extension in filename: " + filename);
        }
        return filename.substring(idx + 1);
    }

}
