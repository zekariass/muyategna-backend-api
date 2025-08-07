package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_lead.JobLeadDto;
import com.muyategna.backend.job_request.dto.job_lead.JobLeadListDto;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimListCustomerViewDto;
import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.messaging.notification.dto.queue.ServiceProviderRankingQueueMessage;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobLeadService {
    JobLead createJobLeadForNewUserJobRequest(JobRequest jobRequest);

    Page<JobLeadListDto> getJobLeadsClosestToTheServiceProvider(Long serviceProviderId, Pageable pageable);

    JobLeadDto getJobLeadById(Long jobLeadId);

    void matchLeadsToTopNServiceProvidersAndNotify(ServiceProviderRankingQueueMessage queueMessage, Message message, Channel channel);

    JobLead getJobLeadWithRelatedEntities(Long jobLeadId);

    List<JobLeadClaimListCustomerViewDto> getAllClaimsForJobLead(Long jobLeadId);

    JobLead save(JobLead jobLead);

    JobLead findLeadByRequestIdWithClaimsAndRelatedEntities(Long jobRequestId);

    void validateJobLeadStatusForClaiming(JobLead jobLead);

    void validateJobLeadClaimLimit(JobLead jobLead);

    void validateJobLead(JobLead jobLead);

    void saveAll(List<JobLead> jobLeads);
}
