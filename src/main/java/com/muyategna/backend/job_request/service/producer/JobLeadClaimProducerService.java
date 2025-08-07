package com.muyategna.backend.job_request.service.producer;

import com.muyategna.backend.messaging.notification.dto.queue.LeadAssignmentQueueMessage;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

import java.util.Set;

public interface JobLeadClaimProducerService {
    void enqueueServiceProviderRankingForNewLeadAssignment(Long jobLeadId);

    void enqueueServiceProviderRankingForLeadReassignment(Set<ServiceProvider> serviceProviders, Long jobLeadId);

    void enqueueServiceProviderNewLeadAssignment(LeadAssignmentQueueMessage leadAssignmentQueueMessage);

    void enqueueServiceProviderLeadReassignment(LeadAssignmentQueueMessage leadAssignmentQueueMessage);
}
