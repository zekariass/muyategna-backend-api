package com.muyategna.backend.job_request.algorithm;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public interface LeadMatchingAlgorithm {
    void matchNewLeadsToServiceProviders(Long JobLeadId, Message message, Channel channel);

    void matchLeadsToServiceProvidersForReassignment(Long JobLeadId, Message message, Channel channel);
}
