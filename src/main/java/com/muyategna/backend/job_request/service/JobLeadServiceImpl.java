package com.muyategna.backend.job_request.service;

import com.muyategna.backend.job_request.dto.job_lead.JobLeadDto;
import com.muyategna.backend.job_request.dto.job_lead.JobLeadListDto;
import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.entity.JobStartTimeWindow;
import com.muyategna.backend.job_request.enums.JobLeadStatus;
import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.job_request.mapper.JobLeadMapper;
import com.muyategna.backend.job_request.mapper.JobRequestMapper;
import com.muyategna.backend.job_request.repository.JobLeadRepository;
import com.muyategna.backend.job_request.repository.JobRequestRepository;
import com.muyategna.backend.job_request.repository.projection.JobLeadProjection;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.professional_service.entity.ServiceTranslation;
import com.muyategna.backend.professional_service.service.ServiceTranslationService;
import com.muyategna.backend.redis.CacheConstants;
import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.entity.ServiceProviderService;
import com.muyategna.backend.service_provider.repository.ServiceProviderRepository;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import com.muyategna.backend.system.entity.SystemConfig;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.system.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JobLeadServiceImpl implements JobLeadService {

    private final JobLeadRepository jobLeadRepository;
    private final JobRequestRepository jobRequestRepository;
    private final SystemConfigService systemConfigService;
    private final ServiceProviderServiceInterface serviceProviderService;
    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceTranslationService serviceTranslationService;

    @Autowired
    public JobLeadServiceImpl(JobLeadRepository jobLeadRepository,
                              JobRequestRepository jobRequestRepository,
                              SystemConfigService systemConfigService,
                              ServiceProviderServiceInterface serviceProviderService,
                              ServiceProviderRepository serviceProviderRepository,
                              ServiceTranslationService serviceTranslationService) {
        this.jobLeadRepository = jobLeadRepository;
        this.jobRequestRepository = jobRequestRepository;
        this.systemConfigService = systemConfigService;
        this.serviceProviderService = serviceProviderService;
        this.serviceProviderRepository = serviceProviderRepository;
        this.serviceTranslationService = serviceTranslationService;
    }

//    @RabbitListener(queues = Constants.JOB_LEAD_GENERATION_QUEUE_BASE_NAME + ".queue")
    @Transactional
    @Override
    public void createJobLeadForNewUserJobRequest(JobRequest jobRequest) {

        try {

            if (jobRequest == null) {
                log.info("Job request is null, cannot generate job lead");
                return;
            }

            if (jobRequest.getStatus() != JobStatus.OPEN) {
                log.info("Job request not in OPEN status for ID: {}", jobRequest.getId());
                return;
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
            jobLeadRepository.save(jobLead);
            log.info("Job lead generated for jobRequestId={}", jobRequest.getId());
        }catch (Exception e) {
            log.error("Error generating job lead: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Page<JobLeadListDto> getAllJobLeadsClosestToTheServiceProvider(Long serviceProviderId, Pageable pageable) {
        // Get the Service Provider
        if (serviceProviderId == null) {
            throw new IllegalArgumentException("Service Provider ID cannot be null");
        }
        if (serviceProviderId <= 0) {
            throw new IllegalArgumentException("Service Provider ID must be a positive number");
        }

        // Check if the service provider exists
        ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId)
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

        // Get all job leads for the service provider's services that are OPEN
        List<Long> serviceIds = serviceProviderServices
                .stream()
                .map(sps -> sps.getService().getId())
                .toList();


        double defaultMaxTravelDistanceInKM = Double.parseDouble(systemConfigService.getSystemConfigByName("DEFAULT_MAX_TRAVEL_DISTANCE_IN_KM").getValue());

        double radiusMeters = serviceProvider.getMaxTravelDistanceInKM() != null
                ? serviceProvider.getMaxTravelDistanceInKM().doubleValue() * 1000
                : defaultMaxTravelDistanceInKM  * 1000;


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

        return jobLeads.map(jobLeadProjection -> {

            JobRequest jobRequest = jobRequestRepository.findById(jobLeadProjection.getJobRequestId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job Request not found with ID: " + jobLeadProjection.getJobRequestId()));

            com.muyategna.backend.professional_service.entity.Service service = jobRequest.getService();
            if (service == null) {
                throw new ResourceNotFoundException("Service not found for Job Request ID: " + jobLeadProjection.getJobRequestId());
            }

            ServiceTranslation serviceTranslation = serviceTranslationService.getTranslationByServiceId(service.getId());
            if (serviceTranslation == null) {
                throw new ResourceNotFoundException("Service Translation not found for Service ID: " + service.getId());
            }

            JobStartTimeWindow jobStartTimeWindow = jobRequest.getStartTimeWindow();

            JobLeadListDto dto = new JobLeadListDto();
            dto.setId(jobLeadProjection.getId());
            dto.setJobRequest(JobRequestMapper.toListDto(jobRequest, serviceTranslation, jobStartTimeWindow));
            dto.setStatus(jobLeadProjection.getStatus());
            dto.setMaxClaims(jobLeadProjection.getMaxClaims());
            dto.setClaimedCount(jobLeadProjection.getClaimedCount());
            dto.setCreatedAt(jobLeadProjection.getCreatedAt());
            dto.setDistanceMeters(jobLeadProjection.getDistanceMeters());
            return dto;
        });
    }

    @Cacheable(value = CacheConstants.CACHE_JOB_REQUEST,
            key = "T(com.muyategna.backend.redis.JobRequestCacheKeyUtil).getJobLeadByIdKey(#jobLeadId)",
            unless = "#result == null")
    @Override
    public JobLeadDto getJobLeadById(Long jobLeadId) {
        log.info("Fetching job lead with ID: {}", jobLeadId);
        return jobLeadRepository.findWithRequestAndService(jobLeadId)
                .map(JobLeadMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Job Lead not found with ID: " + jobLeadId));
    }
}
