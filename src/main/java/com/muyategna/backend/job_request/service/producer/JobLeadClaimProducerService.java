package com.muyategna.backend.job_request.service.producer;

import com.muyategna.backend.job_request.dto.job_lead.LeadAssignmentMessage;
import com.muyategna.backend.service_provider.entity.ServiceProvider;

import java.util.Set;

public interface JobLeadClaimProducerService {
    void enqueueServiceProviderRankingForNewLeadAssignment(Long jobLeadId);
    void enqueueServiceProviderRankingForLeadReassignment(Set<ServiceProvider> serviceProviders, Long jobLeadId);
    void enqueueServiceProviderNewLeadAssignment(LeadAssignmentMessage leadAssignmentMessage);
    void enqueueServiceProviderLeadReassignment(LeadAssignmentMessage leadAssignmentMessage);
}
