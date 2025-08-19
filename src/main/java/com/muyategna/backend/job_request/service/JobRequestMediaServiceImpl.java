package com.muyategna.backend.job_request.service;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.job_request.dto.request_media.JobRequestMediaCreateDto;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.entity.JobRequestMedia;
import com.muyategna.backend.job_request.enums.MediaType;
import com.muyategna.backend.job_request.repository.JobRequestMediaRepository;
import com.muyategna.backend.job_request.repository.JobRequestRepository;
import com.muyategna.backend.storage.config.cloudflare.CloudflareR2Properties;
import com.muyategna.backend.storage.enums.StorageProvider;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Service class responsible for uploading files to remote storage.
 * Currently Cloudflare R2 is supported.
 * It consumes messages from the RabbitMQ queue and uploads the files to R2
 */

@Service
@Slf4j
public class JobRequestMediaServiceImpl implements JobRequestMediaService {

    private final S3Client s3Client;
    private final CloudflareR2Properties r2Properties;
    private final JobRequestRepository jobRequestRepository;
    private final JobRequestMediaRepository jobRequestMediaRepository;

    @Autowired
    public JobRequestMediaServiceImpl(S3Client s3Client, CloudflareR2Properties r2Properties, JobRequestRepository jobRequestRepository, JobRequestMediaRepository jobRequestMediaRepository) {
        this.s3Client = s3Client;
        this.r2Properties = r2Properties;
        this.jobRequestRepository = jobRequestRepository;
        this.jobRequestMediaRepository = jobRequestMediaRepository;
    }

    @RabbitListener(queues = Constants.JOB_REQUEST_FILE_UPLOAD_QUEUE_BASE_NAME + ".queue")
    @Transactional
    @Override
    public void uploadJobRequestFile(JobRequestMediaCreateDto mediaCreateDto, Message message, Channel channel) throws Exception {

        boolean messageAckedOrNacked = false;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            String bucket = r2Properties.getBucketName();
            String basePath = "job_requests/" + mediaCreateDto.getJobRequestId() + "/";

            String originalFilename = mediaCreateDto.getOriginalFilename();
            String newFilename = mediaCreateDto.getNewFilename();
            String ext = mediaCreateDto.getExtension();
            String contentType = mediaCreateDto.getContentType();
            byte[] content = mediaCreateDto.getContent();

            String folder = "";
            String normalizedExt = ext.toLowerCase();
            MediaType mediaType;
            if (Constants.IMAGE_EXTENSIONS.contains(normalizedExt)) {
                folder = "images/";
                mediaType = MediaType.IMAGE;
            } else if (Constants.VIDEO_EXTENSIONS.contains(normalizedExt)) {
                folder = "videos/";
                mediaType = MediaType.VIDEO;
            } else if (Constants.DOCUMENT_EXTENSIONS.contains(normalizedExt)) {
                folder = "documents/";
                mediaType = MediaType.DOCUMENT;
            } else if (Constants.AUDIO_EXTENSIONS.contains(normalizedExt)) {
                folder = "audio/";
                mediaType = MediaType.AUDIO;
            } else {
                log.warn("Unsupported file extension: {}", ext);
                throw new IllegalArgumentException("Unsupported file extension: " + ext);
            }

            String key = basePath + folder + newFilename + "." + ext;

            // Upload to S3 (R2)
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(content));

            // Save to DB
            JobRequest jobRequest = jobRequestRepository
                    .findById(mediaCreateDto.getJobRequestId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job request not found"));

            // Construct ExtraData for the media
            Map<String, Object> extraData = new HashMap<>();
            extraData.put("original_filename", originalFilename);
            extraData.put("new_filename", newFilename);
            extraData.put("extension", ext);
            extraData.put("content_type", contentType);

            // Instantiate, set and save JobRequestMedia entity
            JobRequestMedia jobRequestMedia = new JobRequestMedia();
            jobRequestMedia.setRequest(jobRequest);
            jobRequestMedia.setMediaUrl(key);
            jobRequestMedia.setMediaType(mediaType);
            jobRequestMedia.setStorageProvider(StorageProvider.CLOUDFLARE_R2);
            jobRequestMedia.setExtraData(extraData);
            jobRequestMediaRepository.save(jobRequestMedia);

            // ACK if everything succeeds
            channel.basicAck(deliveryTag, false);
            messageAckedOrNacked = true;

        } catch (Exception e) {
            log.error("Error handling job request file upload: {}", e.getMessage(), e);

            // NACK only if retry interceptor is exhausted
            // Let retry interceptor do its work; we only nack if it's final failure
            channel.basicReject(deliveryTag, false); // false = don't requeue
            messageAckedOrNacked = true;
            throw e;
        } finally {
            // Safety net
            if (!messageAckedOrNacked) {
                try {
                    channel.basicReject(deliveryTag, false);
                } catch (IOException ioException) {
                    log.error("Failed to reject message in finally block", ioException);
                }
            }
        }

    }

}
