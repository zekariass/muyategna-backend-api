package com.muyategna.backend.job_request.service.producer;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.messaging.notification.dto.queue.ServiceProviderRankingQueueMessage;
import com.muyategna.backend.rabbitmq.model.QueueSpec;
import com.muyategna.backend.system.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobLeadProducerServiceImpl implements JobLeadProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final SystemConfigService systemConfigService;

    @Autowired
    public JobLeadProducerServiceImpl(RabbitTemplate rabbitTemplate, SystemConfigService systemConfigService) {
        this.rabbitTemplate = rabbitTemplate;
        this.systemConfigService = systemConfigService;
    }

    @Override
    public void enqueueJobLeadGenerationForNewJobRequest(Long jobRequestId) {
        if (jobRequestId != null) {

            QueueSpec queueSpec = QueueSpec.withBaseName(Constants.JOB_LEAD_GENERATION_QUEUE_BASE_NAME);
            rabbitTemplate.convertAndSend(
                    queueSpec.exchange(),
                    queueSpec.routingKey(),
                    jobRequestId
            );

        }
    }

    @Override
    public void enqueueServiceProvidersRankingForNewLeadNotification(ServiceProviderRankingQueueMessage serviceProviderRankingQueueMessage) {
        QueueSpec queueSpec = QueueSpec.withBaseName(Constants.SERVICE_PROVIDERS_RANKING_QUEUE_BASE_NAME);
        if (serviceProviderRankingQueueMessage.getJobRequestId() != null) {
            rabbitTemplate.convertAndSend(
                    queueSpec.exchange(),
                    queueSpec.routingKey(),
                    serviceProviderRankingQueueMessage
            );
        }
    }
}
