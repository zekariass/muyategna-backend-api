package com.muyategna.backend.billing.gateways.service;

import com.muyategna.backend.billing.gateways.dto.bank_transfer_transaction.BankTransferTransactionProofQueueMessage;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public interface PaymentMediaService {

    void uploadBankTransferProof(BankTransferTransactionProofQueueMessage queueMessage, Message message, Channel channel) throws Exception;
}
