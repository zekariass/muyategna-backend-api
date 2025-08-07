package com.muyategna.backend.job_request.service.producer;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.messaging.notification.dto.queue.LeadAssignmentQueueMessage;
import com.muyategna.backend.rabbitmq.model.QueueSpec;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class JobLeadClaimProducerServiceImpl implements JobLeadClaimProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public JobLeadClaimProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void enqueueServiceProviderRankingForNewLeadAssignment(Long jobLeadId) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.SP_RANKING_FOR_NEW_LEAD_ASSIGNMENT_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                jobLeadId
        );
    }

    @Override
    public void enqueueServiceProviderRankingForLeadReassignment(Set<ServiceProvider> serviceProviders, Long jobLeadId) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.SP_RANKING_FOR_LEAD_REASSIGNMENT_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                jobLeadId
        );
    }

    @Override
    public void enqueueServiceProviderNewLeadAssignment(LeadAssignmentQueueMessage leadAssignmentQueueMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.JOB_LEAD_ASSIGNMENT_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                leadAssignmentQueueMessage
        );
    }

    @Override
    public void enqueueServiceProviderLeadReassignment(LeadAssignmentQueueMessage leadAssignmentQueueMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.JOB_LEAD_REASSIGNMENT_QUEUE_BASE_NAME);
        rabbitTemplate.convertAndSend(
                queueSpec.exchange(),
                queueSpec.routingKey(),
                leadAssignmentQueueMessage
        );

    }
}
