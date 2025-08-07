package com.muyategna.backend.job_request.service;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.job_request.algorithm.JobMatchingAlgorithmServiceImpl;
import com.muyategna.backend.job_request.dto.job_lead.JobLeadDto;
import com.muyategna.backend.job_request.dto.job_lead.JobLeadListDto;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimListCustomerViewDto;
import com.muyategna.backend.job_request.dto.job_request.JobRequestListDto;
import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.entity.JobStartTimeWindow;
import com.muyategna.backend.job_request.enums.JobLeadStatus;
import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.job_request.exception.JobLeadClaimStateInvalidException;
import com.muyategna.backend.job_request.exception.JobRequestStateInvalidException;
import com.muyategna.backend.job_request.mapper.JobLeadClaimMapper;
import com.muyategna.backend.job_request.mapper.JobLeadMapper;
import com.muyategna.backend.job_request.mapper.JobRequestMapper;
import com.muyategna.backend.job_request.repository.JobLeadRepository;
import com.muyategna.backend.job_request.repository.JobRequestRepository;
import com.muyategna.backend.job_request.repository.projection.JobLeadProjection;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.messaging.notification.dto.queue.NewLeadNotificationQueueSPMessage;
import com.muyategna.backend.messaging.notification.dto.queue.ServiceProviderRankingQueueMessage;
import com.muyategna.backend.messaging.notification.producer.NotificationProducerService;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import com.muyategna.backend.professional_service.service.ServiceTranslationService;
import com.muyategna.backend.redis.CacheConstants;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderService;
import com.muyategna.backend.service_provider.repository.ServiceProviderRepository;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import com.muyategna.backend.system.context.AuditorContextHolder;
import com.muyategna.backend.system.entity.SystemConfig;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.system.service.SystemConfigService;
import com.muyategna.backend.user.entity.UserProfile;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JobLeadServiceImpl implements JobLeadService {

    private final JobLeadRepository jobLeadRepository;
    private final JobRequestRepository jobRequestRepository;
    private final SystemConfigService systemConfigService;
    private final ServiceProviderServiceInterface serviceProviderService;
    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceTranslationService serviceTranslationService;
    private final JobMatchingAlgorithmServiceImpl algorithmService;
    private final NotificationProducerService notificationProducerService;

    @Autowired
    public JobLeadServiceImpl(JobLeadRepository jobLeadRepository,
                              JobRequestRepository jobRequestRepository,
                              SystemConfigService systemConfigService,
                              ServiceProviderServiceInterface serviceProviderService,
                              ServiceProviderRepository serviceProviderRepository,
                              ServiceTranslationService serviceTranslationService,
                              JobMatchingAlgorithmServiceImpl algorithmService,
                              NotificationProducerService notificationProducerService) {
        this.jobLeadRepository = jobLeadRepository;
        this.jobRequestRepository = jobRequestRepository;
        this.systemConfigService = systemConfigService;
        this.serviceProviderService = serviceProviderService;
        this.serviceProviderRepository = serviceProviderRepository;
        this.serviceTranslationService = serviceTranslationService;
        this.algorithmService = algorithmService;
        this.notificationProducerService = notificationProducerService;
    }

    //    @RabbitListener(queues = Constants.JOB_LEAD_GENERATION_QUEUE_BASE_NAME + ".queue")
    @Transactional
    @Override
    public JobLead createJobLeadForNewUserJobRequest(JobRequest jobRequest) {

        try {

            if (jobRequest == null) {
                log.info("Job request is null, cannot generate job lead");
                return null;
            }

            if (jobRequest.getStatus() != JobStatus.OPEN) {
                log.info("Job request not in OPEN status for ID: {}", jobRequest.getId());
                return null;
            }

            Integer maxClaims = Optional.ofNullable(systemConfigService.getSystemConfigByName("JOB_CLAIM_LIMIT"))
                    .map(SystemConfig::getValue)
                    .map(Integer::parseInt)
                    .orElse(5);

            JobLead jobLead = new JobLead();
            jobLead.setJobRequest(jobRequest);
            jobLead.setStatus(JobLeadStatus.OPEN);
            jobLead.setMaxClaims(maxClaims); // Default to 5 if not set
            jobLead.setClaimedCount(0);
            JobLead savedJobLead = jobLeadRepository.save(jobLead);
            log.info("Job lead generated for jobRequestId={}", jobRequest.getId());

            return savedJobLead;
        } catch (Exception e) {
            log.error("Error generating job lead: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Page<JobLeadListDto> getJobLeadsClosestToTheServiceProvider(Long serviceProviderId, Pageable pageable) {
        // Get the Service Provider
        if (serviceProviderId == null) {
            throw new IllegalArgumentException("Service Provider ID cannot be null");
        }
        if (serviceProviderId <= 0) {
            throw new IllegalArgumentException("Service Provider ID must be a positive number");
        }

        // Check if the service provider exists
        ServiceProvider serviceProvider = serviceProviderRepository.findByIdWithBusinessAddress(serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Service Provider not found with ID: " + serviceProviderId));

        // Get address of the service provider
        Address serviceProviderAddress = serviceProvider.getBusinessAddress();

        if (serviceProviderAddress == null) {
            throw new ResourceNotFoundException("Service Provider does not have a business address set. Please set the business address before fetching job leads.");
        }

        // Get service the service provider is associated with
        List<ServiceProviderService> serviceProviderServices = serviceProviderService.getServiceProviderServicesByProviderIdAndIsActive(serviceProviderId, true);
        if (serviceProviderServices.isEmpty()) {
            return Page.empty();
        }

        // Get job leads for the service provider's services that are OPEN
        // Get the service ids
        List<Long> serviceIds = serviceProviderServices
                .stream()
                .map(sps -> sps.getService().getId())
                .toList();

        // Get the default max travel distance in KM
        BigDecimal maxTravelDistanceInKM = serviceProvider.getMaxTravelDistanceInKM();

        // If the service provider has not set a max travel distance, use the default
        if (maxTravelDistanceInKM == null || maxTravelDistanceInKM.compareTo(BigDecimal.ZERO) <= 0) {
            maxTravelDistanceInKM = BigDecimal.valueOf(Double.parseDouble(systemConfigService.getSystemConfigByName("DEFAULT_TRAVEL_DISTANCE_IN_KM").getValue()));
        }

        double radiusMeters = maxTravelDistanceInKM.doubleValue() * 1000;


        if (radiusMeters <= 0) {
            throw new IllegalArgumentException("Service Provider's max travel distance must be greater than 0");
        }

        Page<JobLeadProjection> jobLeads = jobLeadRepository.findAllOpenLeadsByServiceIdIn(
                serviceIds,
                serviceProviderAddress.getGeoPoint(),
                radiusMeters,
                serviceProviderId,
                pageable
        );


        if (jobLeads.isEmpty()) {
            return Page.empty();
        }

        // Get the job requests with the related services
        List<Long> jobRequestIds = jobLeads.getContent().stream().map(JobLeadProjection::getJobRequestId).toList();
        List<JobRequest> jobRequests = jobRequestRepository.findAllByIdWithService(jobRequestIds);
        Map<Long, JobRequest> jobRequestMap = jobRequests.stream().collect(Collectors.toMap(JobRequest::getId, Function.identity()));


        // Get service translations for the services
        List<Long> serviceIdsForTranslation = jobRequests.stream().map(jobRequest -> jobRequest.getService().getId()).toList();
        List<ServiceTranslation> serviceTranslations = serviceTranslationService.getTranslationByServiceIdList(serviceIdsForTranslation);
        Map<Long, ServiceTranslation> serviceTranslationMap = serviceTranslations.stream().collect(Collectors.toMap(tr -> tr.getService().getId(), Function.identity(), (tr1, tr2) -> tr1));


        return jobLeads.map(jobLeadProjection -> {

            JobRequest jobRequest = jobRequestMap.get(jobLeadProjection.getJobRequestId());
            if (jobRequest == null) {
                throw new ResourceNotFoundException("Job Request not found with ID: " + jobLeadProjection.getJobRequestId());
            }

            com.muyategna.backend.professional_service.entity.Service service = jobRequest.getService();
            if (service == null) {
                throw new ResourceNotFoundException("Service not found for Job Request ID: " + jobLeadProjection.getJobRequestId());
            }

            ServiceTranslation serviceTranslation = serviceTranslationMap.get(service.getId());
            if (serviceTranslation == null) {
                throw new ResourceNotFoundException("Service Translation not found for Service ID: " + service.getId());
            }

            JobStartTimeWindow jobStartTimeWindow = jobRequest.getStartTimeWindow();

            JobRequestListDto jobRequestListDto = JobRequestMapper.toListDto(jobRequest, serviceTranslation, jobStartTimeWindow);

            return JobLeadMapper.toListDto(jobLeadProjection, jobRequestListDto);
        });
    }

    @Cacheable(value = CacheConstants.CACHE_JOB_REQUEST,
            key = "T(com.muyategna.backend.redis.JobRequestCacheKeyUtil).getJobLeadByIdKey(#jobLeadId)",
            unless = "#result == null")
    @Override
    public JobLeadDto getJobLeadById(Long jobLeadId) {
        log.info("Fetching job lead with ID: {}", jobLeadId);
        return jobLeadRepository.findByIdWithRelatedEntities(jobLeadId)
                .map(JobLeadMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Job Lead not found with ID: " + jobLeadId));
    }


    /**
     * Matches job leads to the top N service providers based on a ranking algorithm
     * and sends notifications to the selected service providers. It consumes messages
     * from the SERVICE_PROVIDERS_RANKING_QUEUE, processes the job request ID, retrieves
     * the top N service providers, and enqueues a notification message for each provider.
     *
     * @param queueMessage the message containing the job request ID
     * @param message      the AMQP message containing the job request details
     * @param channel      the channel through which the message was received
     */
    @RabbitListener(queues = Constants.SERVICE_PROVIDERS_RANKING_QUEUE_BASE_NAME + ".queue")
    @Transactional
    @Override
    public void matchLeadsToTopNServiceProvidersAndNotify(ServiceProviderRankingQueueMessage queueMessage, Message message, Channel channel) {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        Long jobRequestId = queueMessage.getJobRequestId();
        Long jobLeadId = queueMessage.getJobLeadId();
        Long countryId = queueMessage.getCountryId();

        try {

            // Get the NEARBY_PROVIDERS_QUERY_LIMIT from the system config
            int queryLimit = Integer.parseInt(systemConfigService.getSystemConfigByName("NEARBY_PROVIDERS_QUERY_LIMIT").getValue());

            // Get the NUMBER_OF_PROVIDERS_TO_NOTIFY from the system config
            int numberOfProvidersToNotify = Integer.parseInt(systemConfigService.getSystemConfigByName("NUMBER_OF_PROVIDERS_TO_NOTIFY").getValue());

            List<Long> topNServiceProviders = algorithmService.getTopNServiceProviders(jobRequestId, numberOfProvidersToNotify, countryId, queryLimit);


            if (topNServiceProviders == null || topNServiceProviders.isEmpty()) {
                log.error(">>>>>>>>>>>>>>>>>>>> No service providers found for job request with ID: {}", jobRequestId);
                try {
                    channel.basicReject(deliveryTag, false);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                return;
            }

//            // Get the job lead by request id
//            Optional<JobLead> lead = jobLeadRepository.findByJobRequestId(jobRequestId);
//            if (validateLead(lead, jobRequestId)) return;
//
//            if (lead.isEmpty()) {
//                log.error("====================================================>>>>> Lead not found with ID: {}", jobRequestId);
//                try {
//                    channel.basicReject(deliveryTag, false);
//                } catch (IOException ex) {
//                    log.error("====================================================>>>>> Error rejecting lead with ID: {}", jobRequestId);
//
//                    throw new RuntimeException(ex);
//                }
//                return;
//            }

            // Construct the message
            NewLeadNotificationQueueSPMessage newLeadNotificationQueueSPMessage = new NewLeadNotificationQueueSPMessage();
            newLeadNotificationQueueSPMessage.setServiceProviderIds(topNServiceProviders);
            newLeadNotificationQueueSPMessage.setLeadId(jobLeadId);
            newLeadNotificationQueueSPMessage.setJobRequestId(jobRequestId);
            newLeadNotificationQueueSPMessage.setCountryId(countryId);

            // Call the notification queue producer
            notificationProducerService.enqueueNotifyNearByServiceProvidersAboutNewLead(newLeadNotificationQueueSPMessage);

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("=====================================================>>>>> Failed to match leads to top N service providers and notify for job request with ID: {}. Error: {}", jobRequestId, e.getMessage());
            try {
                channel.basicReject(deliveryTag, false);
            } catch (IOException ex) {
                log.error("=====================================================>>>>> Error rejecting lead with ID: {}", jobRequestId);
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public JobLead getJobLeadWithRelatedEntities(Long jobLeadId) {
        log.info("Fetching job lead with ID with related entities: {}", jobLeadId);
        return jobLeadRepository.findByIdWithRelatedEntities(jobLeadId).orElseThrow(
                () -> new ResourceNotFoundException("Job Lead not found with ID: " + jobLeadId)
        );
    }

    @Override
    public List<JobLeadClaimListCustomerViewDto> getAllClaimsForJobLead(Long jobLeadId) {
        log.info("Fetching all claims for job lead with ID: {}", jobLeadId);
        JobLead jobLead = jobLeadRepository.findByIdWithClaimsAndRelatedEntities(jobLeadId).orElseThrow(
                () -> new ResourceNotFoundException("Job Lead not found with ID: " + jobLeadId)
        );

        validateJobLead(jobLead);

        if (jobLead.getJobRequest().getStatus() != JobStatus.OPEN &&
                jobLead.getJobRequest().getStatus() != JobStatus.REOPEN &&
                jobLead.getJobRequest().getStatus() != JobStatus.COMPLETED) {
            throw new JobRequestStateInvalidException("Job request is in: " + jobLead.getJobRequest().getStatus() + " state");
        }

        UserProfile currentLoggedInUser = AuditorContextHolder.getUserProfile();

        if (currentLoggedInUser != null && !Objects.equals(currentLoggedInUser.getId(), jobLead.getJobRequest().getCustomer().getId())) {
            throw new AuthorizationDeniedException("You are not authorized to view claims for this job lead");
        }

        ServiceTranslation translation = serviceTranslationService.getTranslationByServiceId(jobLead.getJobRequest().getService().getId());

        return jobLead
                .getClaims()
                .stream()
                .map(claim -> JobLeadClaimMapper
                        .toCustomerClaimListDto(claim, translation))
                .toList();
    }

    @Override
    public JobLead save(JobLead jobLead) {
        log.info("Saving job lead with ID: {}", jobLead.getId());
        return jobLeadRepository.save(jobLead);
    }

    @Override
    public JobLead findLeadByRequestIdWithClaimsAndRelatedEntities(Long jobRequestId) {
        log.info("Fetching active lead for job request with ID: {}", jobRequestId);

        return jobLeadRepository.findLeadByJobRequestIdWithClaimsAndRelatedEntities(jobRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Lead not found for job request with ID: " + jobRequestId));
    }

    @Override
    public void validateJobLeadStatusForClaiming(JobLead jobLead) {

        if (jobLead.getStatus() != JobLeadStatus.OPEN && jobLead.getStatus() != JobLeadStatus.CLAIMED_PARTIALLY) {
            throw new JobLeadClaimStateInvalidException("Job lead is not in OPEN or CLAIMED_PARTIALLY state for job lead with ID: " + jobLead.getId());
        }

        if (jobLead.getJobRequest().getStatus() != JobStatus.OPEN && jobLead.getJobRequest().getStatus() != JobStatus.REOPEN) {
            throw new JobLeadClaimStateInvalidException("Job request is not in OPEN or REOPEN state for job lead with ID: " + jobLead.getId());
        }

    }


    @Override
    public void validateJobLeadClaimLimit(JobLead jobLead) {

        if (jobLead.getMaxClaims() == null) {
            return;
        }

        if (jobLead.getClaimedCount() >= jobLead.getMaxClaims()) {
            throw new JobLeadClaimStateInvalidException("Claim limit exceeded for job lead: " + jobLead.getId());
        }

    }

    @Override
    public void validateJobLead(JobLead jobLead) {
        if (jobLead.getStatus() == JobLeadStatus.CANCELLED ||
                jobLead.getStatus() == JobLeadStatus.ARCHIVED ||
                jobLead.getStatus() == JobLeadStatus.EXPIRED) {
            throw new JobLeadClaimStateInvalidException("Job lead is in: " + jobLead.getStatus() + " state");
        }
    }

    @Override
    public void saveAll(List<JobLead> jobLeads) {
        log.info("Saving {} job leads", jobLeads.size());
        jobLeadRepository.saveAll(jobLeads);
    }


    private static boolean validateLead(Optional<JobLead> lead, Long jobRequestId) {
        if (lead.isEmpty()) {
            log.error("Lead not found for job request with ID: {}", jobRequestId);
            return true;
        }

        if (lead.get().getStatus() != JobLeadStatus.OPEN) {
            log.error("Lead is not in OPEN status for lead with ID: {}", lead.get().getId());
            return true;
        }
        return false;
    }

}
