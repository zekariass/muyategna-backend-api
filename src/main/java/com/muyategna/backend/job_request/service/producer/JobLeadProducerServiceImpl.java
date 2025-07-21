package com.muyategna.backend.job_request.service.producer;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.rabbitmq.model.QueueSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobLeadProducerServiceImpl implements JobLeadProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public JobLeadProducerServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
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
}
