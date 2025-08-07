package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_request.JobRequestDto;
import com.muyategna.backend.job_request.entity.InviteForQuote;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.repository.InviteForQuoteRepository;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.system.context.AuditorContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.system.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class InviteForQuoteServiceImpl implements InviteForQuoteService {

    private final InviteForQuoteRepository inviteForQuoteRepository;
    private final JobRequestService jobRequestService;
    private final SystemConfigService systemConfigService;

    @Autowired
    public InviteForQuoteServiceImpl(InviteForQuoteRepository inviteForQuoteRepository, JobRequestService jobRequestService, SystemConfigService systemConfigService) {
        this.inviteForQuoteRepository = inviteForQuoteRepository;
        this.jobRequestService = jobRequestService;
        this.systemConfigService = systemConfigService;
    }

    @Transactional
    @Override
    public void inviteForQuote(Long jobRequestId, Long serviceProviderId) {
        JobRequestDto jobRequest = jobRequestService.getJobRequestById(jobRequestId);

        if (jobRequest == null) {
            throw new ResourceNotFoundException("Job Request not found with ID: " + jobRequestId);
        }

        if (!Objects.equals(jobRequest.getCustomerId(), AuditorContextHolder.getUserProfile().getId())) {
            throw new AuthorizationDeniedException("You are not authorized to invite for quote for this job request: " + jobRequestId);
        }

        Boolean isInvitedAlready = inviteForQuoteRepository.existsByJobRequestIdAndProviderId(jobRequestId, serviceProviderId);

        if (isInvitedAlready) {
            throw new AuthorizationDeniedException("You are already invited service provider with ID: {%d} for quote for job request with ID {%d}".formatted(serviceProviderId, jobRequestId));
        }

        List<InviteForQuote> inviteForQuoteList = inviteForQuoteRepository.findByJobRequestId(jobRequestId);
        int inviteForQuoteLimit = Integer.parseInt(systemConfigService.getSystemConfigByName("INVITE_FOR_QUOTE_LIMIT").getValue());

        if (inviteForQuoteList.size() >= inviteForQuoteLimit) {
            throw new AuthorizationDeniedException("You have reached the invite for quote limit for this job request: " + jobRequestId);
        }

        JobRequest jobRequest2 = new JobRequest();
        jobRequest2.setId(jobRequestId);

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setId(serviceProviderId);

        InviteForQuote inviteForQuoteToSave = new InviteForQuote();
        inviteForQuoteToSave.setJobRequest(jobRequest2);
        inviteForQuoteToSave.setProvider(serviceProvider);
        inviteForQuoteRepository.save(inviteForQuoteToSave);
    }


    @Override
    @Transactional
    public void acceptTheInviteForQuote(Long serviceProviderId, Long inviteForQuoteId) {
        log.info("Accepting invite for quote with ID: {} for service provider with ID: {}", inviteForQuoteId, serviceProviderId);
        InviteForQuote inviteForQuote = inviteForQuoteRepository
                .findByIdWithRelatedEntities(inviteForQuoteId, serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Invite For Quote not found with ID: " + inviteForQuoteId + " for service provider with ID: " + serviceProviderId));

        inviteForQuote.setAcceptedAt(LocalDateTime.now());
        if (!inviteForQuote.isViewed()) {
            inviteForQuote.setViewedAt(LocalDateTime.now());
            inviteForQuote.setViewed(true);
        }
        inviteForQuoteRepository.save(inviteForQuote);
        log.info("Invite for quote with ID: {} for service provider with ID: {} accepted successfully", inviteForQuoteId, serviceProviderId);
    }

    @Override
    @Transactional
    public void viewTheInviteForQuote(Long serviceProviderId, Long inviteForQuoteId) {
        InviteForQuote inviteForQuote = inviteForQuoteRepository
                .findByIdWithRelatedEntities(inviteForQuoteId, serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Invite For Quote not found with ID: " + inviteForQuoteId + " for service provider with ID: " + serviceProviderId));

        inviteForQuote.setViewedAt(LocalDateTime.now());
        inviteForQuote.setViewed(true);
        inviteForQuoteRepository.save(inviteForQuote);
        log.info("Invite for quote with ID: {} is viewed by service provider with ID: {}", inviteForQuoteId, serviceProviderId);
    }
}
