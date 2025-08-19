package com.muyategna.backend.billing.gateways.service.producer;

import com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction.BankTransferTransactionProofQueueMessage;
import com.muyategna.backend.common.Constants;
import com.muyategna.backend.rabbitmq.model.QueueSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class PaymentProducerServiceImpl implements PaymentProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public PaymentProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void enqueueBankTransferPaymentProofUpload(BankTransferTransactionProofQueueMessage queueMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.BANK_TRANSFER_PROOF_UPLOAD_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                queueMessage
        );
    }
}
