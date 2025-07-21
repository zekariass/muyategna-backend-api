package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_quote.JobQuoteCreateDto;
import com.muyategna.backend.job_request.dto.job_quote.JobQuoteDto;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.entity.JobQuote;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.enums.JobLeadClaimStatus;
import com.muyategna.backend.job_request.enums.JobQuoteStatus;
import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.job_request.mapper.JobQuoteMapper;
import com.muyategna.backend.job_request.repository.JobLeadClaimRepository;
import com.muyategna.backend.job_request.repository.JobQuoteRepository;
import com.muyategna.backend.system.context.AuditorContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.user.entity.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class JobQuoteServiceImpl implements JobQuoteService {
    private final JobQuoteRepository jobQuoteRepository;
    private final JobLeadClaimRepository jobLeadClaimRepository;

//    private JobLeadClaimService jobLeadClaimService;


    @Autowired
    public JobQuoteServiceImpl(JobQuoteRepository jobQuoteRepository, JobLeadClaimRepository jobLeadClaimRepository) {
        this.jobQuoteRepository = jobQuoteRepository;
        this.jobLeadClaimRepository = jobLeadClaimRepository;
    }


    @Override
    public JobQuote createJobQuote(JobQuoteCreateDto jobQuoteCreateDto, JobLeadClaim jobLeadClaim) {
        log.info("Creating job quote...");
        JobQuote jobQuoteToCreate = JobQuoteMapper.toEntity(jobQuoteCreateDto, jobLeadClaim);
        jobQuoteToCreate.setStatus(JobQuoteStatus.PENDING);
        JobQuote saveJobQuote = jobQuoteRepository.save(jobQuoteToCreate);
        log.info("Job quote created...");
        return saveJobQuote;
    }

    @Transactional
    @Override
    public JobQuoteDto createNewJobQuoteAfterClaim(JobQuoteCreateDto jobQuoteCreateDto, Long serviceProviderId) {
        // Check if job lead claim exists
        JobLeadClaim jobLeadClaim = jobLeadClaimRepository.findById(jobQuoteCreateDto.getClaimId())
                .orElseThrow(() -> new IllegalArgumentException("Job lead claim not found"));

//        // Get quotes for job lead claim for the service provider
//        boolean claimExists = jobLeadClaimRepository.existsByJobLeadIdAndProviderId(jobLeadClaim.getId(), serviceProviderId);

        if (jobLeadClaim == null) {
            throw new IllegalArgumentException("Job lead claim does not exist");
        }

        if (jobLeadClaim.getStatus() != JobLeadClaimStatus.CLAIMED) {
            throw new IllegalArgumentException("Job lead claim is not valid for quotes");
        }

        // Create job quote
        JobQuote jobQuote = createJobQuote(jobQuoteCreateDto, jobLeadClaim);

        // Check if
        return JobQuoteMapper.toDto(jobQuote);
    }


    @Override
    public List<JobQuoteDto> getJobQuotesByClaimIdAndServiceProviderId(Long claimId, Long serviceProviderId) {
        log.info("Getting job quotes by claim ID: {} and service provider ID: {}", claimId, serviceProviderId);
        List<JobQuote> jobQuotes = jobQuoteRepository.findByClaimIdAndServiceProviderId(claimId, serviceProviderId);
        log.info("Job quotes found: {}", jobQuotes.size());
        return JobQuoteMapper.toDtoList(jobQuotes);
    }

    @Override
    public void requestRequote(Long leadClaimId) {
        // Get the customer for the job request from the current AuditorContext
        UserProfile customer = AuditorContextHolder.getUserProfile();

        // Get the job claim fetched with job request
        JobLeadClaim jobLeadClaim = jobLeadClaimRepository.findByIdWithJobLeadAndJobRequest(leadClaimId)
                .orElseThrow(() -> new ResourceNotFoundException("Job lead claim with ID: " + leadClaimId + " not found"));
        JobRequest jobRequest = jobLeadClaim.getJobLead().getJobRequest();

        // Check if the customer of the job request is the same as the user profile above
        if (!jobRequest.getCustomer().getId().equals(customer.getId())) {
            throw new IllegalArgumentException("You are not the owner of this job request");
        }

        // Check status of job claim
        if (jobLeadClaim.getStatus() == JobLeadClaimStatus.CLAIMED) {
            throw new IllegalArgumentException("Job lead claim is already open for quotes");
        }

        // Check if job lead claim is already accepted
        if (jobLeadClaim.getStatus() == JobLeadClaimStatus.ACCEPTED) {
            throw new IllegalArgumentException("Job lead claim is already accepted");
        }

        if (jobLeadClaim.getStatus() == JobLeadClaimStatus.WITHDRAWN) {
            throw new IllegalArgumentException("Job lead claim is already withdrawn by service provider");
        }

        // Check lead status
        if (!(jobRequest.getStatus() == JobStatus.OPEN || jobRequest.getStatus() == JobStatus.REOPEN)) {
            throw new IllegalArgumentException("Job request is not open for quotes");
        }

        // Ser request status to true
        jobLeadClaim.setRequoteRequested(true);
        jobLeadClaimRepository.save(jobLeadClaim);
    }
}
