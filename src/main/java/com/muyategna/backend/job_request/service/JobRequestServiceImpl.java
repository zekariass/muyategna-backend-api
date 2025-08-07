package com.muyategna.backend.job_request.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyategna.backend.job_request.algorithm.JobMatchingAlgorithmService;
import com.muyategna.backend.job_request.dto.job_lead_claim.JobLeadClaimListCustomerViewDto;
import com.muyategna.backend.job_request.dto.job_request.*;
import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.entity.JobStartTimeWindow;
import com.muyategna.backend.job_request.enums.JobLeadStatus;
import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.job_request.exception.JobRequestStateInvalidException;
import com.muyategna.backend.job_request.mapper.JobLeadClaimMapper;
import com.muyategna.backend.job_request.mapper.JobRequestMapper;
import com.muyategna.backend.job_request.repository.JobRequestRepository;
import com.muyategna.backend.job_request.service.producer.JobLeadProducerService;
import com.muyategna.backend.job_request.service.producer.JobRequestProducerService;
import com.muyategna.backend.location.dto.country.CountryDto;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.location.service.AddressService;
import com.muyategna.backend.messaging.notification.dto.queue.JobStatusChangeNotificationQueueCustomerMessage;
import com.muyategna.backend.messaging.notification.dto.queue.JobStatusChangeNotificationQueueSPMessage;
import com.muyategna.backend.messaging.notification.dto.queue.ServiceProviderRankingQueueMessage;
import com.muyategna.backend.messaging.notification.producer.NotificationProducerService;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import com.muyategna.backend.professional_service.repository.ServiceRepository;
import com.muyategna.backend.professional_service.service.ServiceTranslationService;
import com.muyategna.backend.redis.CacheConstants;
import com.muyategna.backend.service_provider.dto.service_provider.NearbyServiceProviderListDto;
import com.muyategna.backend.service_provider.mapper.ServiceProviderMapper;
import com.muyategna.backend.service_provider.repository.projection.NearbyServiceProviderForAJobProjection;
import com.muyategna.backend.system.context.AuditorContextHolder;
import com.muyategna.backend.system.context.CountryContextHolder;
import com.muyategna.backend.system.entity.SystemConfig;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.system.service.SystemConfigService;
import com.muyategna.backend.user.dto.UserRegistrationDto;
import com.muyategna.backend.user.dto.UserRegistrationResultDto;
import com.muyategna.backend.user.entity.UserProfile;
import com.muyategna.backend.user.mapper.UserProfileMapper;
import com.muyategna.backend.user.repository.UserProfileRepository;
import com.muyategna.backend.user.service.UserService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class JobRequestServiceImpl implements JobRequestService {

    private final JobRequestRepository jobRequestRepository;
    private final ServiceRepository serviceRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final AddressService addressService;
    private final JobRequestProducerService jobRequestProducerService;
    private final ObjectMapper objectMapper;
    private final JobLeadProducerService jobLeadProducerService;
    private final JobLeadService jobLeadService;
    private final ServiceTranslationService serviceTranslationService;
    private final SystemConfigService systemConfigService;
    private final JobMatchingAlgorithmService jobMatchingAlgorithmService;
    private final NotificationProducerService notificationProducerService;

    @Autowired
    public JobRequestServiceImpl(JobRequestRepository jobRequestRepository,
                                 ServiceRepository serviceRepository,
                                 UserProfileRepository userProfileRepository, UserService userService,
                                 AddressService addressService,
                                 JobRequestProducerService jobRequestProducerService,
                                 ObjectMapper objectMapper,
                                 JobLeadProducerService jobLeadProducerService, JobLeadService jobLeadService, ServiceTranslationService serviceTranslationService, SystemConfigService systemConfigService, JobMatchingAlgorithmService jobMatchingAlgorithmService, NotificationProducerService notificationProducerService) {
        this.jobRequestRepository = jobRequestRepository;
        this.serviceRepository = serviceRepository;
        this.userProfileRepository = userProfileRepository;
        this.userService = userService;
        this.addressService = addressService;
        this.jobRequestProducerService = jobRequestProducerService;
        this.objectMapper = objectMapper;
        this.jobLeadProducerService = jobLeadProducerService;
        this.jobLeadService = jobLeadService;
        this.serviceTranslationService = serviceTranslationService;
        this.systemConfigService = systemConfigService;
        this.jobMatchingAlgorithmService = jobMatchingAlgorithmService;
        this.notificationProducerService = notificationProducerService;
    }


    /**
     * Creates a new job request based on the provided JobRequestByNewUserWithJsonStringCreateDto.
     * It also adds the files to the job-request file upload rabbitmq queue for processing.
     *
     * @param jsonStringDto the DTO containing the details of the job request to be created
     * @return the created JobRequestDto
     */
    @Override
    @Transactional
    public JobRequestDto createJobRequestForNewUser(JobRequestByNewUserWithJsonStringCreateDto jsonStringDto) {

        // Deserialize the JSON string to JobRequestCreateDto
        // The incoming DTO contains a JSON string for user registration data, questionAnswers and address data
        JobRequestCreateDto jobRequestCreateDto = JobRequestMapper.toCreateDto(jsonStringDto);

        UserRegistrationDto userRegistrationDto;
        try {
            userRegistrationDto = objectMapper.readValue(jsonStringDto.getUserRegistrationData(), UserRegistrationDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize UserRegistrationDto: ", e);
        }

        UserRegistrationResultDto userRegistrationResultDto = userService.createUser(userRegistrationDto);//.getUserProfile();

        UserProfile customer = UserProfileMapper.toEntity(userRegistrationResultDto.getUserProfile(), null);

        // Get files from job request object
        // Check if files are not null
        List<MultipartFile> files = null;
        if (jsonStringDto.getFiles() != null) {
            files = jsonStringDto.getFiles();
        }

        return saveJobRequestAndAddFilesToRabbitQueueForSave(jobRequestCreateDto, files, customer, userRegistrationResultDto.getKeycloakUserId());
    }


    /**
     * Creates a new job request for an existing user based on the provided JobRequestByExistingUserWithJsonStringCreateDto.
     *
     * @param jsonStringDto the DTO containing the details of the job request to be created
     * @return the created JobRequestDto
     * @throws ResourceNotFoundException if the customer with the given ID is not found
     */
//    @Override
//    public JobRequestDto createJobRequestForExistingUser(JobRequestByExistingUserWithJsonStringCreateDto jsonStringDto) {
//
//        UserProfile customer = AuditorContextHolder.getUserProfile();
//
//        // Check if the current user is an admin or the customer
//        // If the current user is not an admin, check if the customer is the same as the one in the incoming DTO
//        if (!userService.isCurrentUserAdmin()){
//            if (customer == null) {
//                throw new ResourceNotFoundException("Customer information not found");
//            }
//        }else {
//            Long customerId = jsonStringDto.getCustomerId();
//
//            customer = userProfileRepository
//                    .findById(customerId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
//        }
//
//        // Construct the JobRequestCreateDto from the incoming DTO
//        JobRequestCreateDto jobRequestCreateDto = JobRequestMapper.toCreateDto(jsonStringDto, customer);
//
//        // Get files from request object
//        List<MultipartFile> files = jsonStringDto.getFiles();
//
//
//        // Save the job request and add the files to the rabbitmq queue
//        return saveJobRequestAndAddFilesToRabbitQueueForSave(jobRequestCreateDto, files, customer, customer.getKeycloakUserId().toString());
//    }
    @Transactional
    @Override
    public JobRequestDto createJobRequestForExistingUser(JobRequestByExistingUserWithJsonStringCreateDto jsonStringDto) {

        // Get the customer profile who is creating the job
        UserProfile customer;

        // Check if the current user is an admin. If so, use the customer ID in the request
        // If the current user is not an admin, the customer is the current user
        if (userService.isCurrentUserAdmin()) {
            // Admins can specify a customerId in the request
            Long customerId = jsonStringDto.getCustomerId();
            customer = userProfileRepository
                    .findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
        } else {
            // Non-admins must be the customer
            customer = AuditorContextHolder.getUserProfile();
            if (customer == null) {
                throw new ResourceNotFoundException("Customer information not found");
            }

            // Validate that the ID in the DTO matches the current customer (if included)
            Long dtoCustomerId = jsonStringDto.getCustomerId();
            if (dtoCustomerId != null && !dtoCustomerId.equals(customer.getId())) {
                throw new AccessDeniedException("You are not authorized to create a job for another user");
            }
        }

        JobRequestCreateDto jobRequestCreateDto = JobRequestMapper.toCreateDto(jsonStringDto, customer);
        List<MultipartFile> files = jsonStringDto.getFiles();

        if (customer.getKeycloakUserId() == null) {
            throw new IllegalStateException("Customer Keycloak ID is missing");
        }

        return saveJobRequestAndAddFilesToRabbitQueueForSave(
                jobRequestCreateDto,
                files,
                customer,
                customer.getKeycloakUserId().toString()
        );
    }


    @Cacheable(value = CacheConstants.CACHE_JOB_REQUEST,
            key = "T(com.muyategna.backend.redis.JobRequestCacheKeyUtil).getJobRequestByIdKey(#jobRequestId)",
            unless = "#result == null")
    @Override
    public JobRequestDto getJobRequestById(Long jobRequestId) {
        log.info("Fetching job request with ID: {}", jobRequestId);
        return jobRequestRepository.findByIdWithService(jobRequestId)
                .map(JobRequestMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Job Request not found with ID: " + jobRequestId));
    }

    @Override
    public JobRequestDetailDto getJobRequestDetailById(Long jobRequestId) {
        log.info("Fetching job request with ID for detail: {}", jobRequestId);
        JobRequest jobRequest = jobRequestRepository.findJobRequestWithRelatedEntities(jobRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Request not found with ID: " + jobRequestId));

        com.muyategna.backend.professional_service.entity.Service service = jobRequest.getService();

        if (service == null) {
            throw new ResourceNotFoundException("Service not found for job request with ID: " + jobRequestId);
        }

        // Get the service translation for the current language
        ServiceTranslation serviceTranslation = serviceTranslationService
                .getTranslationByServiceId(service.getId());


        return JobRequestMapper.toDetailDto(jobRequest, serviceTranslation);
    }

    @CacheEvict(value = CacheConstants.CACHE_JOB_REQUEST,
            key = "T(com.muyategna.backend.redis.JobRequestCacheKeyUtil).getJobRequestByIdKey(#jobRequestId)")
    @Transactional
    @Override
    public JobRequestDto updateJobRequest(Long jobRequestId, JobRequestUpdateDto updateDto) {
        log.info("Updating job request with ID: {}", jobRequestId);
        // Get the existing job request
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Request not found with ID: " + jobRequestId));

        // Check if the current user is an admin or the customer of the job request
        if (!userService.isCurrentUserAdmin()
                && jobRequest.getCustomer() != null
                && !jobRequest.getCustomer().getId().equals(AuditorContextHolder.getUserProfile().getId())) {
            throw new AccessDeniedException("You are not authorized to update this job request");
        }

        // Check if the job request is in a status that allows updates
        if (jobRequest.getStatus() != JobStatus.OPEN && jobRequest.getStatus() != JobStatus.REOPEN) {
            throw new BadRequestException("Job request is not OPEN for updates");
        }

        // Set the updated data in the job request
        JobRequest jobRequestToUpdate = JobRequestMapper.toEntity(jobRequest, updateDto);

        // Update the job request with the new data
        JobRequest updatedJobRequest = jobRequestRepository.save(jobRequestToUpdate);

        log.info("Updated job request with ID: {}", jobRequestId);

        // Return the updated job request
        return JobRequestMapper.toDto(updatedJobRequest);
    }

    @CacheEvict(value = CacheConstants.CACHE_JOB_REQUEST,
            key = "T(com.muyategna.backend.redis.JobRequestCacheKeyUtil).getJobRequestByIdKey(#jobRequestId)")
    @Transactional
    @Override
    public void cancelJobRequest(Long jobRequestId) {
        // Get the existing job request
//        JobRequest jobRequest = jobRequestRepository.findById(jobRequestId)
//                .orElseThrow(() -> new ResourceNotFoundException("Job Request not found with ID: " + jobRequestId));

        JobRequest jobRequest = jobRequestRepository.findByIdWithLeads(jobRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Request not found with ID: " + jobRequestId));


        // Check if the current user is an admin or the customer of the job request
        if (jobRequest.getCustomer() != null
                && !jobRequest.getCustomer().getId().equals(AuditorContextHolder.getUserProfile().getId())) {
            throw new AccessDeniedException("You are not authorized to cancel this job request");
        }

        // Check if the job request is in a status that allows cancellation
        if (jobRequest.getStatus() != JobStatus.OPEN && jobRequest.getStatus() != JobStatus.REOPEN) {
            throw new BadRequestException("Job request is not OPEN for cancellation");
        }

        // Set the updated data in the job request
        jobRequest.setStatus(JobStatus.CANCELLED);
        JobRequest updatedJobRequest = jobRequestRepository.save(jobRequest);

        // Notify the customer and Service Providers that has claimed the job already that the job request has been cancelled
        JobStatusChangeNotificationQueueSPMessage jobRequestNotificationMessage = new JobStatusChangeNotificationQueueSPMessage();
        jobRequestNotificationMessage.setJobId(updatedJobRequest.getId());
        notificationProducerService.enqueueNotifyServiceProviderAboutJobStatusChange(jobRequestNotificationMessage);

        JobStatusChangeNotificationQueueCustomerMessage customerNotificationMessage = new JobStatusChangeNotificationQueueCustomerMessage();
        customerNotificationMessage.setJobId(updatedJobRequest.getId());
        notificationProducerService.enqueueNotifyCustomerAboutJobStatusChange(customerNotificationMessage);

        // If the status of JobRequest is CANCELLED, then set the status of all associatedJobLeads to CANCELLED
        List<JobLead> jobLeads = jobRequest.getJobLeads();
        for (JobLead lead : jobLeads) {
            lead.setStatus(JobLeadStatus.CANCELLED);
        }
        jobLeadService.saveAll(jobLeads);


    }

    @CacheEvict(value = CacheConstants.CACHE_JOB_REQUEST,
            key = "T(com.muyategna.backend.redis.JobRequestCacheKeyUtil).getJobRequestByIdKey(#jobRequestId)")
    @Transactional
    @Override
    public void reopenJobRequest(Long jobRequestId) {

        // Get the existing job request
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Request not found with ID: " + jobRequestId));

        // Check if the current user is an admin or the customer of the job request
        if (jobRequest.getCustomer() != null
                && !jobRequest.getCustomer().getId().equals(AuditorContextHolder.getUserProfile().getId())) {
            throw new AccessDeniedException("You are not authorized to reopen this job request");
        }

        // Check if the job request is in a status that allows reopening
        if (jobRequest.getStatus() != JobStatus.CANCELLED &&
                jobRequest.getStatus() != JobStatus.EXPIRED &&
                jobRequest.getStatus() != JobStatus.PAUSED) {
            throw new BadRequestException("Job request is not CANCELLED, EXPIRED or PAUSED for reopening");
        }

        // Set the updated data in the job request
        jobRequest.setStatus(JobStatus.REOPEN);
        jobRequestRepository.save(jobRequest);
    }

    /**
     * Creates a new job request based on the provided JobRequestCreateDto and saves it to the database.
     * It also adds the files to the job-request file upload rabbitmq queue for processing.
     *
     * @param jobRequestCreateDto the DTO containing the details of the job request to be created
     * @param customer            the customer who made the job request
     * @return the created JobRequestDto
     */
    private JobRequestDto saveJobRequestAndAddFilesToRabbitQueueForSave(JobRequestCreateDto jobRequestCreateDto, List<MultipartFile> files, UserProfile customer, String keycloakUserId) {
        // Get the service, if it doesn't exist, throw an exception
        com.muyategna.backend.professional_service.entity.Service service = serviceRepository
                .findById(jobRequestCreateDto.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with ID: " + jobRequestCreateDto.getServiceId()));

        // Create an address entity from the address create dto
        Address address = addressService.createAddress(jobRequestCreateDto.getAddressCreateDto());

        // Create a JobStartTimeWindow entity from the jobRequestCreateDto for setting the start time window of the job
        JobStartTimeWindow jobStartTimeWindow = new JobStartTimeWindow();
        jobStartTimeWindow.setId(jobRequestCreateDto.getStartTimeWindowId());

        // Map the job request create dto to a job request entity for saving
        JobRequest jobRequest = JobRequestMapper.toEntity(jobRequestCreateDto, service, customer, address, jobStartTimeWindow);

        // Save the job request to the database
        JobRequest savedJobRequest = jobRequestRepository.save(jobRequest);

        // Create and save the lead for the new job
        JobLead jobLead = jobLeadService.createJobLeadForNewUserJobRequest(savedJobRequest);

        // Add file uploads to job-request file upload rabbitmq queue
        // Call the RabbitMQProducer to send the files after JobRequest is saved and commit
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    if (files != null && !files.isEmpty()) {
                        jobRequestProducerService.enqueueUploadJobRequestFiles(savedJobRequest.getId(), files);
//                        jobLeadProducerService.enqueueJobLeadGenerationForNewJobRequest(savedJobRequest.getId());
                        if (jobLead != null) {
                            ServiceProviderRankingQueueMessage serviceProviderRankingMessage = new ServiceProviderRankingQueueMessage();
                            serviceProviderRankingMessage.setJobLeadId(jobLead.getId());
                            serviceProviderRankingMessage.setJobRequestId(savedJobRequest.getId());
                            serviceProviderRankingMessage.setCountryId(CountryContextHolder.getCountry().getId());
                            jobLeadProducerService.enqueueServiceProvidersRankingForNewLeadNotification(serviceProviderRankingMessage);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    Response response = userService.deleteUser(keycloakUserId);
                    response.close();
                }
            }
        });

        return JobRequestMapper.toDto(savedJobRequest);
    }


    /**
     * This method is used to get a list of nearby service providers for a given job request.
     * The service providers are retrieved based on the job request's address and the distance
     * between the job request's address and the service provider's address is calculated.
     * The service providers are sorted by their distance from the job request's address in ascending order.
     * The method takes a job request ID as input and returns a list of NearbyServiceProviderListDto.
     *
     * @param jobRequestId the ID of the job request
     * @return a list of NearbyServiceProviderListDto
     */
    @Override
    public List<NearbyServiceProviderListDto> getNearbyServiceProvidersForJobRequest(Long jobRequestId) {
        // Get the job request
        JobRequest jobRequest = jobRequestRepository.findByIdWithRelatedEntities(jobRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Job Request not found with ID: " + jobRequestId));

        // Check if the job request is in valid state
        if (jobRequest.isOpen() || jobRequest.isReopened()) {
            throw new JobRequestStateInvalidException("Job request is in: " + jobRequest.getStatus() + " state");
        }

        // Get the customer
        UserProfile customer = jobRequest.getCustomer();

        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found for job request with ID: " + jobRequestId);
        }

        if (!Objects.equals(customer.getId(), AuditorContextHolder.getUserProfile().getId())) {
            throw new AuthorizationDeniedException("You are not authorized to get nearby service providers for this job request with ID: " + jobRequestId);
        }

        List<String> configNames = new ArrayList<>();
        configNames.add("NEARBY_PROVIDERS_QUERY_LIMIT");
        configNames.add("DEFAULT_TRAVEL_DISTANCE_IN_KM");
        configNames.add("NEARBY_PROVIDER_DISPLAY_LIMIT");
        List<SystemConfig> systemConfigs = systemConfigService.getSystemConfigsByName(configNames);

        int queryLimit = 0;
        int displayLimit = 0;
        double radiusMeters = 0.0;

        for (SystemConfig systemConfig : systemConfigs) {
            if (systemConfig.getName().equals("NEARBY_PROVIDERS_QUERY_LIMIT")) {
                queryLimit = Integer.parseInt(systemConfig.getValue());
            } else if (systemConfig.getName().equals("NEARBY_PROVIDER_DISPLAY_LIMIT")) {
                displayLimit = Integer.parseInt(systemConfig.getValue());
            } else if (systemConfig.getName().equals("DEFAULT_TRAVEL_DISTANCE_IN_KM")) {
                radiusMeters = Double.parseDouble(systemConfig.getValue()) * 1000.0;
            }
        }

        if (queryLimit == 0) {
            log.warn(">>>>>>>>>>>> NEARBY_PROVIDERS_QUERY_LIMIT is not set, using default value of 10");
            queryLimit = 10;
        }

        if (displayLimit == 0) {
            log.warn(">>>>>>>>>>>> NEARBY_PROVIDER_DISPLAY_LIMIT is not set in system config, using default value of 10");
            displayLimit = 10;
        }

        if (radiusMeters == 0.0) {
            log.warn(">>>>>>>>>>>>  DEFAULT_TRAVEL_DISTANCE_IN_KM is not set in system config, using default value of 15km");
            radiusMeters = 15 * 1000.0;
        }

        CountryDto countryDto = CountryContextHolder.getCountry();

        List<NearbyServiceProviderForAJobProjection> nearbyServiceProviderListDtoListProjections = jobMatchingAlgorithmService.getNearbyServiceProviders(
                jobRequest,
                radiusMeters,
                queryLimit,
                displayLimit,
                countryDto.getId()
        );


        if (nearbyServiceProviderListDtoListProjections != null && !nearbyServiceProviderListDtoListProjections.isEmpty()) {
            return ServiceProviderMapper.toNearbyProviderListDto(nearbyServiceProviderListDtoListProjections);
        }

        return List.of();
    }

    @Override
    public List<JobLeadClaimListCustomerViewDto> getAllClaimsOfJobRequestForCustomer(Long jobRequestId) {
        log.info("Fetching all claims for job request with ID: {}", jobRequestId);
        JobLead jobLead = jobLeadService.findLeadByRequestIdWithClaimsAndRelatedEntities(jobRequestId);

        UserProfile currentLoggedInUser = AuditorContextHolder.getUserProfile();

        jobLeadService.validateJobLead(jobLead);

        validateJobRequest(jobLead.getJobRequest());

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

    private static void validateJobRequest(JobRequest jobRequest) {
        if (jobRequest.getStatus() != JobStatus.OPEN &&
                jobRequest.getStatus() != JobStatus.REOPEN &&
                jobRequest.getStatus() != JobStatus.COMPLETED) {
            throw new JobRequestStateInvalidException("Job request is in: " + jobRequest.getStatus() + " state");
        }
    }


}
