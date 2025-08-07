package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_lead_claim.*;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.enums.JobLeadClaimStatus;
import com.muyategna.backend.messaging.notification.dto.queue.LeadAssignmentQueueMessage;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface JobLeadClaimService {
    void assignNewLeadToServiceProviders(LeadAssignmentQueueMessage leadAssignmentQueueMessage, Message message,
                                         Channel channel);

    Set<JobLeadClaim> getServiceProviderLeadsByLeadId(Long leadId);

    void claimJobLead(JobLeadClaimCreateDto jobLeadClaimCreateDto);

    JobLeadClaimDto getJobLeadClaimById(Long claimId);

    boolean existsNewJobLeadClaim(Long claimId, Long serviceProviderId);

    JobLeadClaim findByIdWithJobLeadAndJobRequest(Long leadClaimId);

    void saveJobLeadClaim(JobLeadClaim jobLeadClaim);

    Page<JobLeadClaimSPListViewDto> getClaimsByStatusForServiceProvider(Long serviceProviderId, JobLeadClaimStatus status, Pageable pageable);

    JobLeadClaimDetailSPViewDto getClaimByIdForServiceProvider(Long serviceProviderId, Long jobLeadClaimId);

    void withdrawJobLeadClaimByServiceProvider(Long serviceProviderId, Long jobLeadClaimId);

    JobLeadClaimDetailCustomerViewDto getClaimByIdForCustomer(Long jobLeadClaimId);

    void acceptJobLeadClaimByCustomer(Long jobLeadClaimId);

    void rejectJobLeadClaimByCustomer(Long jobLeadClaimId);

    void requestRequoteForJobLeadClaimByCustomer(Long jobLeadClaimId);

    List<JobLeadClaim> findAllByJobLeadIdIn(List<Long> jobLeadIds);
}

