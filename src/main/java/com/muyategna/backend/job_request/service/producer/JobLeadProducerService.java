package com.muyategna.backend.job_request.service.producer;

import com.muyategna.backend.messaging.notification.dto.queue.ServiceProviderRankingQueueMessage;

public interface JobLeadProducerService {
    void enqueueJobLeadGenerationForNewJobRequest(Long jobRequestId);

    void enqueueServiceProvidersRankingForNewLeadNotification(ServiceProviderRankingQueueMessage serviceProviderRankingQueueMessage);
}
