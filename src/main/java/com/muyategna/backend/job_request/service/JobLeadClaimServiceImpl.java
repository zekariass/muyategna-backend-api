package com.muyategna.backend.job_request.service;

import com.muyategna.backend.billing.dto.service_credit_cost.ServiceCreditCostDto;
import com.muyategna.backend.billing.entity.HoldCredit;
import com.muyategna.backend.billing.entity.ServiceProviderWallet;
import com.muyategna.backend.billing.enums.CreditSource;
import com.muyategna.backend.billing.service.CreditUsageService;
import com.muyategna.backend.billing.service.HoldCreditService;
import com.muyategna.backend.billing.service.ServiceCreditCostService;
import com.muyategna.backend.billing.service.ServiceProviderWalletService;
import com.muyategna.backend.billing.strategies.credit_usage_strategy.CreditContext;
import com.muyategna.backend.billing.strategies.credit_usage_strategy.CreditStrategyResolver;
import com.muyategna.backend.billing.strategies.credit_usage_strategy.CreditUsageStrategy;
import com.muyategna.backend.common.Constants;
import com.muyategna.backend.job_request.dto.job_lead_claim.*;
import com.muyategna.backend.job_request.dto.job_quote.JobQuoteCreateDto;
import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.entity.JobQuote;
import com.muyategna.backend.job_request.enums.JobLeadClaimStatus;
import com.muyategna.backend.job_request.enums.JobLeadStatus;
import com.muyategna.backend.job_request.exception.JobRequestStateInvalidException;
import com.muyategna.backend.job_request.mapper.JobLeadClaimMapper;
import com.muyategna.backend.job_request.repository.JobLeadClaimRepository;
import com.muyategna.backend.messaging.notification.dto.queue.LeadAssignmentQueueMessage;
import com.muyategna.backend.messaging.notification.dto.queue.NewClaimNotificationQueueCustomerMessage;
import com.muyategna.backend.messaging.notification.dto.queue.NewClaimNotificationQueueSPMessage;
import com.muyategna.backend.messaging.notification.producer.NotificationProducerService;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import com.muyategna.backend.professional_service.service.ServiceTranslationService;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderStatistics;
import com.muyategna.backend.service_provider.repository.ServiceProviderStatisticsRepository;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import com.muyategna.backend.service_provider.service.ServiceProviderStatisticsService;
import com.muyategna.backend.system.context.AuditorContextHolder;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.user.entity.UserProfile;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class JobLeadClaimServiceImpl implements JobLeadClaimService {

    private final JobLeadService jobLeadService;
    private final ServiceProviderServiceInterface providerServices;
    private final JobLeadClaimRepository jobLeadClaimRepository;
    private final ServiceProviderStatisticsService statisticsService;
    private final ServiceProviderStatisticsRepository statisticsRepository;
    private final JobQuoteService jobQuoteService;
    private final ServiceCreditCostService serviceCreditCostService;
    private final ServiceProviderWalletService serviceProviderWalletService;
    private final CreditUsageService creditUsageService;
    private final HoldCreditService holdCreditService;
    private final CreditStrategyResolver creditStrategyResolver;
    private final ServiceTranslationService serviceTranslationService;
    private final NotificationProducerService notificationProducerService;


    @Autowired
    public JobLeadClaimServiceImpl(JobLeadService jobLeadService,
                                   ServiceProviderServiceInterface serviceProviderServices,
                                   JobLeadClaimRepository jobLeadClaimRepository,
                                   ServiceProviderStatisticsService statisticsService, ServiceProviderStatisticsRepository statisticsRepository,
                                   JobQuoteService jobQuoteService,
                                   ServiceCreditCostService serviceCreditCostService,
                                   ServiceProviderWalletService serviceProviderWalletService,
                                   CreditUsageService creditUsageService,
                                   HoldCreditService holdCreditService,
                                   CreditStrategyResolver creditStrategyResolver,
                                   ServiceTranslationService serviceTranslationService, NotificationProducerService notificationProducerService) {
        this.jobLeadService = jobLeadService;
        this.providerServices = serviceProviderServices;
        this.jobLeadClaimRepository = jobLeadClaimRepository;
        this.statisticsService = statisticsService;
        this.statisticsRepository = statisticsRepository;
        this.jobQuoteService = jobQuoteService;
        this.serviceCreditCostService = serviceCreditCostService;
        this.serviceProviderWalletService = serviceProviderWalletService;
        this.creditUsageService = creditUsageService;
        this.holdCreditService = holdCreditService;
        this.creditStrategyResolver = creditStrategyResolver;
        this.serviceTranslationService = serviceTranslationService;
        this.notificationProducerService = notificationProducerService;
    }

    @RabbitListener(queues = Constants.JOB_LEAD_ASSIGNMENT_QUEUE_BASE_NAME + ".queue")
    @Override
    public void assignNewLeadToServiceProviders(LeadAssignmentQueueMessage leadAssignmentQueueMessage,
                                                Message message,
                                                Channel channel) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            if (leadAssignmentQueueMessage == null || leadAssignmentQueueMessage.getJobLeadId() == null || leadAssignmentQueueMessage.getServiceProviderIds() == null) {
                log.warn("Invalid lead assignment message: {}", leadAssignmentQueueMessage);
                channel.basicReject(deliveryTag, false); // discard
                return;
            }

            Long jobLeadId = leadAssignmentQueueMessage.getJobLeadId();
            List<Long> serviceProviderIds = leadAssignmentQueueMessage.getServiceProviderIds();

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

        // Extract parameters from DTO
        Long serviceProviderId = jobLeadClaimCreateDto.getServiceProviderId();
        Long jobLeadId = jobLeadClaimCreateDto.getJobLeadId();
        JobQuoteCreateDto jobQuote = jobLeadClaimCreateDto.getJobQuote();

        validateInputData(serviceProviderId, jobLeadId);

        // Get the job lead
        JobLead jobLead = jobLeadService.getJobLeadWithRelatedEntities(jobLeadId);

        // Check lead claim limit
        jobLeadService.validateJobLeadClaimLimit(jobLead);

        // Check lead status
        jobLeadService.validateJobLeadStatusForClaiming(jobLead);

        // Get the service
        com.muyategna.backend.professional_service.entity.Service service = jobLead.getJobRequest().getService();


        // Check if Service Provider subscribes to the service of the job lead
        boolean hasServiceProviderThisService = providerServices.existsActiveServiceProviderServiceByProviderIdAndServiceId(serviceProviderId, service.getId());

        if (!hasServiceProviderThisService) {
            throw new ResourceNotFoundException("Service Provider does not have this service");
        }

        // Get the number of credits needed for the service
        ServiceCreditCostDto serviceCreditCost = serviceCreditCostService.findByServiceId(service.getId());

        // Get the wallet of the service provider
        ServiceProviderWallet serviceProviderWallet = serviceProviderWalletService.findByServiceProviderIdWithRelatedEntities(serviceProviderId);

        if (serviceProviderWallet == null) {
            throw new ResourceNotFoundException("Service Provider Wallet not found for ID: " + serviceProviderId);
        }

        CreditContext creditContext = new CreditContext(
                serviceCreditCost,
                serviceProviderWallet,
                serviceProviderId,
                null,
                BigDecimal.ZERO
        );


        // If the user could access this method then they are a service provider already passed the security check.
        // So we can safely create and set the service provider id on the job lead claim
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setId(serviceProviderId);

        // Create a new JobLeadClaim
        JobLeadClaim savedJobLeadClaim = createJobLeadClaim(jobLead, serviceProvider);

        // Set the job lead claim on the hold credit and save it
        saveHoldCredit(creditContext, savedJobLeadClaim);

        // Create a Job Quote if provided
        createJobQuote(jobQuote, savedJobLeadClaim);

        // Create Service Provider Credit Usage OR Subscription Usage
        createServiceProviderCreditUsage(creditContext, serviceProvider, savedJobLeadClaim);

        log.info("Job lead claim created with ID: {}", savedJobLeadClaim.getId());

        // update job lead
        updateJobLead(jobLead);

        // update service provider statistics
        updateServiceProviderStatisticsForNewClaim(serviceProvider, jobLead);

        // Send notification
        sendNotification(savedJobLeadClaim, serviceProviderId, jobLead);
    }

    private void updateServiceProviderStatisticsForNewClaim(ServiceProvider serviceProvider, JobLead jobLead) {
        ServiceProviderStatistics statistics = statisticsRepository.findByProviderId(serviceProvider.getId()).orElse(null);
        if (statistics == null) {
            statistics = new ServiceProviderStatistics();
            statistics.setProvider(serviceProvider);
        }

        long timeInMins = ChronoUnit.MINUTES.between(jobLead.getCreatedAt(), LocalDateTime.now());

        statistics.setTotalClaims(statistics.getTotalClaims() + 1);
        statistics.setDailyClaimCount(statistics.getDailyClaimCount() + 1);
        statistics.setWeeklyClaimCount(statistics.getWeeklyClaimCount() + 1);
        statistics.setTwoWeeklyClaimCount(statistics.getTwoWeeklyClaimCount() + 1);
        statistics.setMonthlyClaimCount(statistics.getMonthlyClaimCount() + 1);
        statistics.setAvgClaimTimeInMins(BigDecimal.valueOf(timeInMins));

        statisticsRepository.save(statistics);


    }

    private void saveHoldCredit(CreditContext creditContext, JobLeadClaim savedJobLeadClaim) {
        CreditUsageStrategy strategy = creditStrategyResolver.resolve(creditContext);

        HoldCredit holdCredit = strategy.useCredit(creditContext);

        if (holdCredit != null) {
            holdCredit.setJobLeadClaim(savedJobLeadClaim);
            holdCreditService.save(holdCredit);
        }
    }

    private void createJobQuote(JobQuoteCreateDto jobQuote, JobLeadClaim savedJobLeadClaim) {
        if (jobQuote != null) {
            JobQuote jobQ = jobQuoteService.createJobQuote(jobQuote, savedJobLeadClaim);
        }
    }

    private void createServiceProviderCreditUsage(CreditContext creditContext, ServiceProvider serviceProvider, JobLeadClaim savedJobLeadClaim) {
        BigDecimal usedCreditAmount = creditContext.getUsedCreditAmount();
        if (creditContext.getCreditSource() == CreditSource.SUBSCRIPTION) {
            creditUsageService.createCreditUsage(serviceProvider, savedJobLeadClaim, usedCreditAmount, CreditSource.SUBSCRIPTION, false);
        } else if (creditContext.getCreditSource() == CreditSource.PAYGO) {
            creditUsageService.createCreditUsage(serviceProvider, savedJobLeadClaim, usedCreditAmount, CreditSource.PAYGO, false);
        } else {
            creditUsageService.createCreditUsage(serviceProvider, savedJobLeadClaim, usedCreditAmount, CreditSource.HYBRID, true);
        }
    }

    private void updateJobLead(JobLead jobLead) {
        jobLead.setClaimedCount(jobLead.getClaimedCount() + 1);

        if (jobLead.getClaimedCount() >= jobLead.getMaxClaims()) {
            jobLead.setStatus(JobLeadStatus.CLAIMED_FULL);
        } else {
            jobLead.setStatus(JobLeadStatus.CLAIMED_PARTIALLY);
        }

        JobLead savedJobLead = jobLeadService.save(jobLead);
    }

    private static void validateInputData(Long serviceProviderId, Long jobLeadId) {
        Objects.requireNonNull(serviceProviderId, "Service Provider ID cannot be null");
        Objects.requireNonNull(jobLeadId, "Job Lead ID cannot be null");

        if (serviceProviderId <= 0 || jobLeadId <= 0) {
            throw new IllegalArgumentException("Service Provider ID and Job Lead ID must be positive numbers");
        }
    }

    private void sendNotification(JobLeadClaim savedJobLeadClaim, Long serviceProviderId, JobLead jobLead) {
        NewClaimNotificationQueueSPMessage spNotificationMessage = new NewClaimNotificationQueueSPMessage();
        spNotificationMessage.setClaimId(savedJobLeadClaim.getId());
        spNotificationMessage.setServiceProviderId(serviceProviderId);
        notificationProducerService.enqueueNotifyServiceProviderAboutNewClaim(spNotificationMessage);

        NewClaimNotificationQueueCustomerMessage customerNotificationMessage = new NewClaimNotificationQueueCustomerMessage();
        customerNotificationMessage.setClaimId(savedJobLeadClaim.getId());
        customerNotificationMessage.setCustomerId(jobLead.getJobRequest().getCustomer().getId());
        notificationProducerService.enqueueNotifyCustomerAboutNewClaim(customerNotificationMessage);
    }

    private JobLeadClaim createJobLeadClaim(JobLead jobLead, ServiceProvider serviceProvider) {
        JobLeadClaim jobLeadClaim = new JobLeadClaim();
        jobLeadClaim.setJobLead(jobLead);
        jobLeadClaim.setProvider(serviceProvider);
        jobLeadClaim.setStatus(JobLeadClaimStatus.CLAIMED);
        jobLeadClaim.setRequoteRequested(false);
        return jobLeadClaimRepository.save(jobLeadClaim);
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

    @Override
    public Page<JobLeadClaimSPListViewDto> getClaimsByStatusForServiceProvider(Long serviceProviderId, JobLeadClaimStatus status, Pageable pageable) {
        log.info("Getting job lead claims for service provider: {}", serviceProviderId);
        Page<JobLeadClaim> jobLeadClaims = jobLeadClaimRepository.findByProviderIdAndStatus(serviceProviderId, status, pageable);
        log.info("Job lead claims found for service provider: {}", serviceProviderId);

        List<Long> serviceIds = jobLeadClaims.stream()
                .map(JobLeadClaim::getJobLead)
                .map(JobLead::getJobRequest)
                .map(jr -> jr.getService().getId())
                .toList();

        List<ServiceTranslation> translations = serviceTranslationService.getTranslationByServiceIdList(serviceIds);

        return jobLeadClaims.map(
                claim -> JobLeadClaimMapper.toSPClaimListDto(
                        claim,
                        translations.stream()
                                .filter(t -> t.getService().getId().equals(claim.getJobLead().getJobRequest().getService().getId()))
                                .findFirst()
                                .orElse(null)
                )
        );
    }

    @Override
    public JobLeadClaimDetailSPViewDto getClaimByIdForServiceProvider(Long serviceProviderId, Long jobLeadClaimId) {

        log.info("Getting job lead claim by ID for service provider: {}", jobLeadClaimId);
        JobLeadClaim claim = jobLeadClaimRepository.findByIdWithRelatedEntitiesForServiceProvider(jobLeadClaimId, serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Job lead claim with ID: " + jobLeadClaimId + " not found"));

        com.muyategna.backend.professional_service.entity.Service service = claim.getJobLead().getJobRequest().getService();

        ServiceTranslation translation = serviceTranslationService.getTranslationByServiceId(service.getId());

        if (translation == null) {
            throw new ResourceNotFoundException("Service translation for service ID: " + service.getId() + " not found");
        }

        JobLeadClaimDetailSPViewDto jobLeadClaimDetailSPViewDto = JobLeadClaimMapper.toSPClaimDetailDto(claim, translation);

        log.info(" Job lead claim found with ID: {}", jobLeadClaimId);
        return jobLeadClaimDetailSPViewDto;
    }

    @Override
    public void withdrawJobLeadClaimByServiceProvider(Long serviceProviderId, Long jobLeadClaimId) {
        log.info("Withdrawing job lead claim by ID for service provider: {}", jobLeadClaimId);

        // Get the job lead claim
        JobLeadClaim jobLeadClaim = jobLeadClaimRepository.findByIdAndProviderId(jobLeadClaimId, serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Job lead claim with ID: " + jobLeadClaimId + " not found"));

        if (jobLeadClaim.getStatus() != JobLeadClaimStatus.CLAIMED && jobLeadClaim.getStatus() != JobLeadClaimStatus.QUOTED) {
            throw new IllegalArgumentException("Job lead claim with ID: " + jobLeadClaimId + " is not in CLAIMED or QUOTED status");
        }

        jobLeadClaim.setStatus(JobLeadClaimStatus.WITHDRAWN);
        JobLeadClaim savedJobLeadClaim = jobLeadClaimRepository.save(jobLeadClaim);

        // Send notification
        if (savedJobLeadClaim.getStatus() == JobLeadClaimStatus.WITHDRAWN) {

        }
    }

    @Override
    public JobLeadClaimDetailCustomerViewDto getClaimByIdForCustomer(Long jobLeadClaimId) {

        log.info("Getting job lead claim by ID for customer: {}", jobLeadClaimId);
        UserProfile customer = AuditorContextHolder.getUserProfile();
        JobLeadClaim claim = jobLeadClaimRepository.findByIdWithRelatedEntitiesForCustomer(jobLeadClaimId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Job lead claim with ID: " + jobLeadClaimId + " not found for this customer"));


        if (claim.isWithdrawn() || claim.isExpired()) {
            throw new JobRequestStateInvalidException("Job claim is in invalid state");
        }

        jobLeadService.validateJobLead(claim.getJobLead());

        // Get the service translation
        ServiceTranslation translation = serviceTranslationService.getTranslationByServiceId(claim.getJobLead().getJobRequest().getService().getId());

        if (translation == null) {
            throw new ResourceNotFoundException("Service translation for service ID: " + claim.getJobLead().getJobRequest().getService().getId() + " not found");
        }
        return JobLeadClaimMapper.toCustomerClaimDetailDto(claim, translation);
    }

    @Override
    public void acceptJobLeadClaimByCustomer(Long jobLeadClaimId) {
        log.info("Accepting job lead claim by ID for customer: {}", jobLeadClaimId);

        UserProfile customer = AuditorContextHolder.getUserProfile();
        JobLeadClaim claim = jobLeadClaimRepository.findByIdWithRelatedEntitiesForCustomer(jobLeadClaimId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Job lead claim with ID: " + jobLeadClaimId + " not found for this customer"));

        if (!claim.isClaimed() && !claim.isQuoted()) {
            throw new JobRequestStateInvalidException("Job claim is in invalid state. Status: " + claim.getStatus() + " The claim must be in CLAIMED or QUOTED status to accept.");
        }

        claim.setStatus(JobLeadClaimStatus.ACCEPTED);
        jobLeadClaimRepository.save(claim);

        // TODO: send notification
    }

    @Override
    public void rejectJobLeadClaimByCustomer(Long jobLeadClaimId) {
        log.info("Rejecting job lead claim by ID for customer: {}", jobLeadClaimId);

        UserProfile customer = AuditorContextHolder.getUserProfile();
        JobLeadClaim claim = jobLeadClaimRepository.findByIdWithRelatedEntitiesForCustomer(jobLeadClaimId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Job lead claim with ID: " + jobLeadClaimId + " not found for this customer"));

        if (!claim.isClaimed() && !claim.isQuoted()) {
            throw new JobRequestStateInvalidException("Job claim is in invalid state. Status: " + claim.getStatus() + " The claim must be in CLAIMED or QUOTED status to reject.");
        }

        claim.setStatus(JobLeadClaimStatus.REJECTED);
        jobLeadClaimRepository.save(claim);

        // TODO: send notification

    }

    @Override
    public void requestRequoteForJobLeadClaimByCustomer(Long jobLeadClaimId) {
        log.info("Requesting requote for job lead claim by ID for customer: {}", jobLeadClaimId);

        UserProfile customer = AuditorContextHolder.getUserProfile();
        JobLeadClaim claim = jobLeadClaimRepository.findByIdWithRelatedEntitiesForCustomer(jobLeadClaimId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Job lead claim with ID: " + jobLeadClaimId + " not found for this customer"));

        if (!claim.isClaimed() && !claim.isQuoted() && !claim.isRejected()) {
            throw new JobRequestStateInvalidException("Job claim is in invalid state. Status: " + claim.getStatus() + " The claim must be in CLAIMED, QUOTED or REJECTED status to request requote.");
        }

        if (claim.getRequoteRequested()) {
            throw new JobRequestStateInvalidException("A requote has already been requested for this claim.");
        }

        claim.setRequoteRequested(true);
        jobLeadClaimRepository.save(claim);

        // TODO: send notification
    }

    @Override
    public List<JobLeadClaim> findAllByJobLeadIdIn(List<Long> jobLeadIds) {
        log.info("Finding job lead claims by job lead IDs: {}", jobLeadIds);
        return jobLeadClaimRepository.findAllByJobLeadIdIn(jobLeadIds);
    }
}
