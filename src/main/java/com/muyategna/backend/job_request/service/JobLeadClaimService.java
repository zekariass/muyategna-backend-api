package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_lead.LeadAssignmentMessage;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimDto;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimCreateDto;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.util.Set;

public interface JobLeadClaimService {
    void assignNewLeadToServiceProviders(LeadAssignmentMessage leadAssignmentMessage, Message message,
                                         Channel channel);
    Set<JobLeadClaim> getServiceProviderLeadsByLeadId(Long leadId);

    void claimJobLead(JobLeadClaimCreateDto jobLeadClaimCreateDto);

    JobLeadClaimDto getJobLeadClaimById(Long claimId);

    boolean existsNewJobLeadClaim(Long claimId, Long serviceProviderId);

    JobLeadClaim findByIdWithJobLeadAndJobRequest(Long leadClaimId);

    void saveJobLeadClaim(JobLeadClaim jobLeadClaim);
}

