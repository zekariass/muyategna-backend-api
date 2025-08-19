package com.muyategna.backend.billing.gateways.service;

import com.muyategna.backend.billing.entity.BankTransferTransaction;
import com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction.BankTransferTransactionProofQueueMessage;
import com.muyategna.backend.billing.service.BankTransferTransactionService;
import com.muyategna.backend.common.Constants;
import com.muyategna.backend.storage.config.cloudflare.CloudflareR2Properties;
import com.muyategna.backend.storage.enums.StorageProvider;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentMediaServiceImpl implements PaymentMediaService {

    private final S3Client s3Client;
    private final CloudflareR2Properties r2Properties;
    private final BankTransferTransactionService bankTransferTransactionService;

    public PaymentMediaServiceImpl(S3Client s3Client, CloudflareR2Properties r2Properties, BankTransferTransactionService bankTransferTransactionService) {
        this.s3Client = s3Client;
        this.r2Properties = r2Properties;
        this.bankTransferTransactionService = bankTransferTransactionService;
    }

    @RabbitListener(queues = Constants.BANK_TRANSFER_PROOF_UPLOAD_QUEUE_BASE_NAME + ".queue")
    @Transactional
    @Override
    public void uploadBankTransferProof(BankTransferTransactionProofQueueMessage queueMessage, Message message, Channel channel) throws Exception {

        boolean messageAckedOrNacked = false;
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            String bucket = r2Properties.getBucketName();
            String basePath = "bank_transfer_proofs/" + queueMessage.getTransactionId() + "/";

            String originalFilename = queueMessage.getOriginalFilename();
            String newFilename = queueMessage.getNewFilename();
            String ext = queueMessage.getExtension();
            String contentType = queueMessage.getContentType();
            byte[] content = queueMessage.getFileContent();

            String folder = "";
            String normalizedExt = ext.toLowerCase();
            String mediaType = null;
            if (Constants.IMAGE_EXTENSIONS.contains(normalizedExt)) {
                folder = "images/";
                mediaType = "image";
            } else if (Constants.VIDEO_EXTENSIONS.contains(normalizedExt)) {
                folder = "videos/";
                mediaType = "video";
            } else if (Constants.DOCUMENT_EXTENSIONS.contains(normalizedExt)) {
                folder = "documents/";
                mediaType = "document";
            } else if (Constants.AUDIO_EXTENSIONS.contains(normalizedExt)) {
                folder = "audio/";
                mediaType = "audio";
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
            BankTransferTransaction bankTransferTransaction = bankTransferTransactionService
                    .getBankTransferTransactionByTxnRefWithTransaction(queueMessage.getTxnRef())
                    .orElseThrow(() -> new ResourceNotFoundException("Bank Transfer Transaction not found for transaction ref: " + queueMessage.getTxnRef() + ". File uploaded but url not saved to db."));

            // Construct ExtraData for the media
            Map<String, Object> extraData = new HashMap<>();
            extraData.put("original_filename", originalFilename);
            extraData.put("new_filename", newFilename);
            extraData.put("extension", ext);
            extraData.put("content_type", contentType);
            extraData.put("storage_provider", StorageProvider.CLOUDFLARE_R2);
            extraData.put("media_type", mediaType);

            bankTransferTransaction.setExtraData(extraData);
            bankTransferTransaction.setProofOfPaymentUrl(key);

            bankTransferTransactionService.saveBankTransferTransaction(bankTransferTransaction);

            // ACK if everything succeeds
            channel.basicAck(deliveryTag, false);
            messageAckedOrNacked = true;

        } catch (Exception e) {
            log.error("Error handling bank transfer file upload: {}", e.getMessage(), e);

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
