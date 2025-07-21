package com.muyategna.backend.job_request.service;

import com.muyategna.backend.billing.service.ServiceProviderCreditService;
import com.muyategna.backend.billing.service.ServiceProviderCreditUsageService;
import com.muyategna.backend.billing.service.ServiceProviderSubscriptionService;
import com.muyategna.backend.billing.service.ServiceProviderSubscriptionUsageService;
import com.muyategna.backend.common.Constants;
import com.muyategna.backend.job_request.dto.job_lead.JobLeadDto;
import com.muyategna.backend.job_request.dto.job_lead.LeadAssignmentMessage;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimCreateDto;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimDto;
import com.muyategna.backend.job_request.dto.job_quote.JobQuoteCreateDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestDto;
import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.entity.JobQuote;
import com.muyategna.backend.job_request.enums.JobLeadClaimStatus;
import com.muyategna.backend.job_request.exception.NoValidSubscriptionOrCreditFoundException;
import com.muyategna.backend.job_request.mapper.JobLeadClaimMapper;
import com.muyategna.backend.job_request.mapper.JobLeadMapper;
import com.muyategna.backend.job_request.repository.JobLeadClaimRepository;
import com.muyategna.backend.professional_service.service.ServiceService;
import com.muyategna.backend.service_provider.dto.service_provider_service.ServiceProviderServiceDto;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import com.muyategna.backend.service_provider.service.ServiceProviderStatisticsService;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class JobLeadClaimServiceImpl implements JobLeadClaimService {

    private final JobLeadService jobLeadService;
    private final ServiceProviderServiceInterface providerServices;
    private final ServiceProviderSubscriptionService subscriptionService;
    private final JobLeadClaimRepository jobLeadClaimRepository;
    private final ServiceProviderStatisticsService statisticsService;
    private final JobRequestService jobRequestService;
    private final JobQuoteService jobQuoteService;
    private final ServiceService serviceService;
    private final ServiceProviderCreditService serviceProviderCreditService;
    private final ServiceProviderSubscriptionUsageService serviceProviderSubscriptionUsageService;
    private final ServiceProviderCreditUsageService serviceProviderCreditUsageService;
    private static final int MAX_LEADS_REMINDER = 3;
//
//    @Autowired
//    private ProfilerService profilerService;

    @Autowired
    public JobLeadClaimServiceImpl(JobLeadService jobLeadService,
                                   ServiceProviderServiceInterface serviceProviderServices,
                                   ServiceProviderSubscriptionService subscriptionService,
                                   JobLeadClaimRepository jobLeadClaimRepository,
                                   ServiceProviderStatisticsService statisticsService, JobRequestService jobRequestService,
                                   JobQuoteService jobQuoteService, ServiceService serviceService,
                                   ServiceProviderCreditService serviceProviderCreditService,
                                   ServiceProviderSubscriptionUsageService serviceProviderSubscriptionUsageService,
                                   ServiceProviderCreditUsageService serviceProviderCreditUsageService) {
        this.jobLeadService = jobLeadService;
        this.providerServices = serviceProviderServices;
        this.subscriptionService = subscriptionService;
        this.jobLeadClaimRepository = jobLeadClaimRepository;
        this.statisticsService = statisticsService;
        this.jobRequestService = jobRequestService;
        this.jobQuoteService = jobQuoteService;
        this.serviceService = serviceService;
        this.serviceProviderCreditService = serviceProviderCreditService;
        this.serviceProviderSubscriptionUsageService = serviceProviderSubscriptionUsageService;
        this.serviceProviderCreditUsageService = serviceProviderCreditUsageService;
    }

    @RabbitListener(queues = Constants.JOB_LEAD_ASSIGNMENT_QUEUE_BASE_NAME + ".queue")
    @Override
    public void assignNewLeadToServiceProviders(LeadAssignmentMessage leadAssignmentMessage,
                                                Message message,
                                                Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            if (leadAssignmentMessage == null || leadAssignmentMessage.getJobLeadId() == null || leadAssignmentMessage.getServiceProviderIds() == null) {
                log.warn("Invalid lead assignment message: {}", leadAssignmentMessage);
                channel.basicReject(deliveryTag, false); // discard
                return;
            }

            Long jobLeadId = leadAssignmentMessage.getJobLeadId();
            List<Long> serviceProviderIds = leadAssignmentMessage.getServiceProviderIds();

            for (Long serviceProviderId : serviceProviderIds) {
                try {
                    if (jobLeadClaimRepository.existsByJobLeadIdAndProviderId(jobLeadId, serviceProviderId)) {
                        log.info("Service provider {} already has lead {} assigned. Skipping.", serviceProviderId, jobLeadId);
                        continue;
                    }

                    JobLead jobLead = new JobLead();
                    jobLead.setId(jobLeadId);

                    ServiceProvider serviceProvider = new ServiceProvider();
                    serviceProvider.setId(serviceProviderId);

                    JobLeadClaim lead = new JobLeadClaim();
                    lead.setJobLead(jobLead);
                    lead.setProvider(serviceProvider);
                    lead.setStatus(JobLeadClaimStatus.CLAIMED);

                    statisticsService.updateStatisticsForNewLeadAssignment(serviceProviderId);
                    jobLeadClaimRepository.save(lead);

                } catch (Exception providerEx) {
                    log.error("Failed to assign lead {} to service provider {}. Continuing with others.", jobLeadId, serviceProviderId, providerEx);
                    // You can optionally collect these failures and alert/report them
                }
            }

            // Message processed (best-effort)
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Unhandled error in lead assignment", e);
            try {
                channel.basicReject(deliveryTag, false);
            } catch (Exception nackEx) {
                log.error("Failed to reject message", nackEx);
            }
            // Optional: consider alerting or reporting unrecoverable failures
        }
    }

    @Transactional
    @Override
    public Set<JobLeadClaim> getServiceProviderLeadsByLeadId(Long jobLeadId) {
        if (jobLeadId == null) {
            return Set.of();
        }
        return jobLeadClaimRepository.findServiceProviderLeadsByLeadId(jobLeadId);
    }

    @Transactional
    @Override
    public void claimJobLead(JobLeadClaimCreateDto jobLeadClaimCreateDto) {

//        profilerService.logDbHits();

        // Extract parameters from DTO
        Long serviceProviderId = jobLeadClaimCreateDto.getServiceProviderId();
        Long jobLeadId = jobLeadClaimCreateDto.getJobLeadId();
        JobQuoteCreateDto jobQuote = jobLeadClaimCreateDto.getJobQuote();

        // Validate input parameters
        if (serviceProviderId == null || jobLeadId == null) {
            throw new IllegalArgumentException("Service Provider ID and Job Lead ID cannot be null");
        }

        if (serviceProviderId <= 0 || jobLeadId <= 0) {
            throw new IllegalArgumentException("Service Provider ID and Job Lead ID must be positive numbers");
        }

        // Get the Service from the job request that the lead is associated with
        JobLeadDto jobLead = jobLeadService.getJobLeadById(jobLeadId);

        JobRequestDto jobRequest = jobRequestService.getJobRequestById(jobLead.getJobRequestId());

        Optional<com.muyategna.backend.professional_service.entity.Service> optionalService = serviceService.getServiceById(jobRequest.getServiceId());

        if (optionalService.isEmpty()) {
            throw new ResourceNotFoundException("Service not found for Job Lead with ID: " + jobLeadId);
        }

        com.muyategna.backend.professional_service.entity.Service service = optionalService.get();


        // Check if Service Provider subscribes to the service of the job lead
        ServiceProviderServiceDto hasServiceProviderThisService = providerServices.getServiceProviderServiceByProviderIdAndServiceId(serviceProviderId, service.getId());

        // Check if the Service Provider has a valid subscription
        boolean hasValidSubscription = subscriptionService.hasServiceProviderValidSubscription(serviceProviderId, service.getId());


        //Service Provider Credit Check OR
        boolean hasServiceProviderCredit = false;
        if (!hasValidSubscription) {
            hasServiceProviderCredit = serviceProviderCreditService.hasServiceProviderEnoughCredit(serviceProviderId, service.getId());
        }

        // throw new IllegalStateException if neither subscription nor credit is valid
        if (!hasValidSubscription && !hasServiceProviderCredit) {
            throw new NoValidSubscriptionOrCreditFoundException("Service Provider does not have enough credit or subscription to claim the job lead");
        }

        // If the user could access this method then they are a service provider already through security check. So we can safely set the service provider
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setId(serviceProviderId);

        // Create a new JobLeadClaim
        JobLeadClaim jobLeadClaim = new JobLeadClaim();
        jobLeadClaim.setJobLead(JobLeadMapper.toEntity(jobLead));
        jobLeadClaim.setProvider(serviceProvider);
        jobLeadClaim.setStatus(JobLeadClaimStatus.CLAIMED);
        JobLeadClaim savedJobLeadClaim = jobLeadClaimRepository.save(jobLeadClaim);
        // Create a Job Quote if provided
        if (jobQuote != null) {
            JobQuote jobQ  = jobQuoteService.createJobQuote(jobQuote, savedJobLeadClaim);
        }

        // Service Provider Credit Usage OR Subscription Usage
        if (hasValidSubscription) {
            serviceProviderSubscriptionUsageService.createServiceProviderSubscriptionUsage(serviceProviderId, savedJobLeadClaim, service.getId());
        } else {
            serviceProviderCreditUsageService.createServiceProviderCreditUsage(serviceProviderId, savedJobLeadClaim, service.getId());
        }

//        profilerService.logDbHits();

    }


    @Override
    public JobLeadClaimDto getJobLeadClaimById(Long claimId) {
        log.info("Getting job lead claim by ID: {}", claimId);
        JobLeadClaim jobLeadClaim = jobLeadClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Job lead claim with ID: " + claimId + " not found"));
        log.info("Job lead claim found with ID: {}", jobLeadClaim.getId());
        return JobLeadClaimMapper.toDto(jobLeadClaim);
    }

    @Override
    public boolean existsNewJobLeadClaim(Long claimId, Long serviceProviderId) {
        return jobLeadClaimRepository.existsByIdAndProviderIdAndStatus(claimId, serviceProviderId, JobLeadClaimStatus.CLAIMED);
    }

    @Override
    public JobLeadClaim findByIdWithJobLeadAndJobRequest(Long leadClaimId) {
        log.info("Getting job lead claim by ID with job lead: {}", leadClaimId);
        JobLeadClaim jobLeadClaim = jobLeadClaimRepository.findByIdWithJobLeadAndJobRequest(leadClaimId)
                .orElseThrow(() -> new ResourceNotFoundException("Job lead claim with ID: " + leadClaimId + " not found"));
        log.info("Job lead claim found with ID : {}", jobLeadClaim.getId());
        return jobLeadClaim;
    }

    @Override
    public void saveJobLeadClaim(JobLeadClaim jobLeadClaim) {
        log.info("Saving job lead claim: {}", jobLeadClaim.getId());
        jobLeadClaimRepository.save(jobLeadClaim);
        log.info("Job lead claim saved with ID: {}", jobLeadClaim.getId());
    }
}
