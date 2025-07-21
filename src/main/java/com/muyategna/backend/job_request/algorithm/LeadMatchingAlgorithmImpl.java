package com.muyategna.backend.job_request.algorithm;

import com.muyategna.backend.common.Constants;
import com.muyategna.backend.job_request.dto.job_lead.LeadAssignmentMessage;
import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.enums.JobLeadStatus;
import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.job_request.repository.JobLeadRepository;
import com.muyategna.backend.job_request.service.JobLeadClaimService;
import com.muyategna.backend.job_request.service.producer.JobLeadClaimProducerService;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.professional_service.entity.Service;
import com.muyategna.backend.service_provider.entity.ServiceProviderStatistics;
import com.muyategna.backend.service_provider.repository.ServiceProviderStatisticsRepository;
import com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceProjection;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import com.muyategna.backend.service_provider.service.ServiceProviderStatisticsService;
import com.muyategna.backend.system.exception.ResourceNotFoundException;
import com.muyategna.backend.system.service.SystemConfigService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Slf4j
public class LeadMatchingAlgorithmImpl implements LeadMatchingAlgorithm {

    private final SystemConfigService systemConfigService;
    private final JobLeadRepository jobLeadRepository;
    private final ServiceProviderServiceInterface serviceProviderService;
    private final ServiceProviderStatisticsRepository providerStatisticsRepository;
    private final JobLeadClaimService jobLeadClaimService;
    private final ServiceProviderStatisticsService serviceProviderStatisticsService;
    private final JobLeadClaimProducerService leadProducerService;

    // Get the maximum number of leads that can be sent to service providers
    // For now, use a hardcoded value or fetch from system config

    private static final Integer MAX_LEAD_REASSIGNMENTS = 3;
    private static final Integer MAX_LEADS_SENT_TO_PROVIDERS = 3; // Maximum number of leads sent to providers
    private static final Double INITIAL_DISTANCE_IN_METERS = 5000.0; // Initial distance for ranking service providers
    private static final Double MAX_DISTANCE_IN_METERS = 100000.0; // Maximum distance for ranking service providers
    private static final Double DISTANCE_MULTIPLIER = 2.0; // Distance multiplier for ranking service providers


    @Autowired
    public LeadMatchingAlgorithmImpl(SystemConfigService systemConfigService, JobLeadRepository jobLeadRepository, ServiceProviderServiceInterface serviceProviderService, ServiceProviderStatisticsRepository providerStatisticsRepository, JobLeadClaimService jobLeadClaimService, ServiceProviderStatisticsService serviceProviderStatisticsService, JobLeadClaimProducerService leadProducerService) {
        this.systemConfigService = systemConfigService;
        this.jobLeadRepository = jobLeadRepository;
        this.serviceProviderService = serviceProviderService;
        this.providerStatisticsRepository = providerStatisticsRepository;
        this.jobLeadClaimService = jobLeadClaimService;
        this.serviceProviderStatisticsService = serviceProviderStatisticsService;
        this.leadProducerService = leadProducerService;
    }

    @RabbitListener(queues = Constants.SP_RANKING_FOR_NEW_LEAD_ASSIGNMENT_QUEUE_BASE_NAME + ".queue")
    @Transactional
    @Override
    public void matchNewLeadsToServiceProviders(Long JobLeadId, Message message, Channel channel) {

        // Acknowledge the message to RabbitMQ
        Long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {

            // Get the parent lead by ID
            Optional<JobLead> lead = jobLeadRepository.findById(JobLeadId);
            if (validateLead(JobLeadId, lead)) return;

            // Get the job request associated with the lead
            JobRequest jobRequest = lead.get().getJobRequest();
            if (validateJobRequest(JobLeadId, jobRequest)) return;

            // Get the service associated with the job request
            Service service = jobRequest.getService();
            if (validateService(service, jobRequest)) return;

            // Get the Job location associated with the job request
            Address jobLocation = jobRequest.getAddress();

            // Get the service providers Services that match the job request, service and location with distance from the job location
            Set<ServiceProviderServiceWithDistanceProjection> serviceProviderServicesWithDistance = new HashSet<>();

            double radius = INITIAL_DISTANCE_IN_METERS; // Start with the initial distance

            while (serviceProviderServicesWithDistance.size() < MAX_LEADS_SENT_TO_PROVIDERS && radius <= MAX_DISTANCE_IN_METERS) {
                serviceProviderServicesWithDistance = serviceProviderService.getServiceProviderServicesByServiceAndLocation(
                        service.getId(),
                        jobLocation.getGeoPoint(),
                        radius
                );

                radius *= DISTANCE_MULTIPLIER;
            }

            // Rank the service providers based on their Statistics and distance from the job location
            List<Long> rankedServiceProvidersIds = rankServiceProviders(serviceProviderServicesWithDistance);

            // Enqueue the lead generation to RabbitMQ
            LeadAssignmentMessage leadAssignmentMessage = LeadAssignmentMessage.builder()
                    .jobLeadId(JobLeadId)
                    .serviceProviderIds(rankedServiceProvidersIds)
                    .build();
            leadProducerService.enqueueServiceProviderNewLeadAssignment(leadAssignmentMessage);
            // Acknowledge the message to RabbitMQ
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Error while matching new leads to service providers: {}", e.getMessage(), e);
            try {
                channel.basicReject(deliveryTag, false);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException("Error while matching new leads to service providers", e);
        }
       }


    @Override
    @RabbitListener(queues = Constants.SP_RANKING_FOR_LEAD_REASSIGNMENT_QUEUE_BASE_NAME + ".queue")
    @Transactional
    public void matchLeadsToServiceProvidersForReassignment(Long JobLeadId, Message message, Channel channel) {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            // Get the lead by ID
            Optional<JobLead> lead = jobLeadRepository.findById(JobLeadId);
            if (validateLead(JobLeadId, lead)) return;

            // Get previously assigned service providers for the lead
            Set<JobLeadClaim> assignedServiceProviders = jobLeadClaimService.getServiceProviderLeadsByLeadId(JobLeadId);

            // Service providers are those who have been assigned the lead
            List<Long> excludedServiceProviderIds = assignedServiceProviders.stream().map(sp -> sp.getProvider().getId()).toList(); // Convert to List<Long>


            // Get the job request associated with the lead
            JobRequest jobRequest = lead.get().getJobRequest();
            if (validateJobRequest(JobLeadId, jobRequest)) return;

            // Get the service associated with the job request
            Service service = jobRequest.getService();
            if (validateService(service, jobRequest)) return;

            // Get the Job location associated with the job request
            Address jobLocation = jobRequest.getAddress();

            // Get the service providers Services that match the job request, service and location
            Set<ServiceProviderServiceWithDistanceProjection> serviceProviderServicesWithDistance = new HashSet<>();


            double radius = INITIAL_DISTANCE_IN_METERS; // Start with the initial distance

            while (serviceProviderServicesWithDistance.size() < MAX_LEADS_SENT_TO_PROVIDERS && radius <= MAX_DISTANCE_IN_METERS) {
                serviceProviderServicesWithDistance = serviceProviderService.getServiceProviderServicesByServiceAndLocation(
                        service.getId(),
                        jobLocation.getGeoPoint(),
                        radius,
                        excludedServiceProviderIds // Exclude previously assigned service providers
                );

                radius *= DISTANCE_MULTIPLIER;
            }

            // Rank the service providers based on their Statistics and distance from the job location
            List<Long> rankedServiceProvidersIds = rankServiceProviders(serviceProviderServicesWithDistance);

            // enqueue the lead generation to rabbitmq
            LeadAssignmentMessage leadAssignmentMessage = LeadAssignmentMessage.builder()
                    .jobLeadId(JobLeadId)
                    .serviceProviderIds(rankedServiceProvidersIds)
                    .build();
            leadProducerService.enqueueServiceProviderLeadReassignment(leadAssignmentMessage);
            // Acknowledge the message to RabbitMQ
            channel.basicAck(deliveryTag, false);
        }catch (Exception e) {
            log.error("Error while matching leads to service providers for reassignment: {}", e.getMessage(), e);
            try {
                channel.basicReject(deliveryTag, false);
            } catch (Exception ex) {
                throw new RuntimeException( ex);
            }
            throw new RuntimeException("Error while matching leads to service providers for reassignment", e);
        }
    }



    private static boolean validateService(Service service, JobRequest jobRequest) {
        if (service == null) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>> Service not found for job request with ID: {}", jobRequest.getId());
            return true;
        }
        return false;
    }

    private static boolean validateJobRequest(Long JobLeadId, JobRequest jobRequest) {
        if (jobRequest == null) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>> Job request not found for lead with ID: {}", JobLeadId);
            return true;
        }

        if (!jobRequest.getStatus().equals(JobStatus.OPEN)) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>> Job request is not in PENDING status: {}", jobRequest.getStatus());
            return true;
        }

        if (jobRequest.getAddress() == null) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>> Job request address not found for lead with ID: {}", JobLeadId);
            return true;
        }
        return false;
    }

    private static boolean validateLead(Long JobLeadId, Optional<JobLead> lead) {
        if (lead.isEmpty()) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>> Lead not found with ID: " + JobLeadId);
            throw new ResourceNotFoundException("Lead not found with ID: " + JobLeadId);
        }

        if( lead.get().getStatus() != JobLeadStatus.OPEN) {
            log.error(">>>>>>>>>>>>>>>>>>>>>>>>> Lead is not in PENDING status: {}", lead.get().getStatus());
            return true;
        }

//        if (lead.get().getReassignmentCount() >= MAX_LEAD_REASSIGNMENTS) {
//            log.error(">>>>>>>>>>>>>>>>>>>>>>>>> Maximum number of leads reached for lead with ID: {}", JobLeadId);
//            return true;
//        }
        return false;
    }

    private List<Long> rankServiceProviders(
            Set<ServiceProviderServiceWithDistanceProjection> serviceProviderServicesWithDistance) {

        // Map service provider id to ServiceProviderServiceWithDistanceProjection object
        Map<Long, ServiceProviderServiceWithDistanceProjection> serviceProviderServiceWithDistanceFromJobMap = serviceProviderServicesWithDistance
                .stream()
                .collect(Collectors.toMap(
                        ServiceProviderServiceWithDistanceProjection::getProviderId,
                        Function.identity()
                ));

        // Get Service Providers from the service provider services
//        Set<ServiceProvider> serviceProviders = serviceProviderServicesWithDistance
//                .stream()
//                .map(spswd -> spswd.getServiceProviderService().getProvider())
//                .collect(Collectors.toSet());

        // Get statistics for each service providers
//        List<Long> serviceProviderIds = serviceProviderServicesWithDistance.stream()
//                .map(ServiceProviderServiceWithDistanceProjection::getProviderId)
//                .toList();

        // Create a map of service provider ID to service Provider ID itself for easy access
//        Map<Long, Long> serviceProviderMap = serviceProviderServicesWithDistance
//                .stream()
//                .collect(Collectors.toMap(ServiceProviderServiceWithDistanceProjection::getProviderId,
//                        ServiceProviderServiceWithDistanceProjection::getServiceId));

        Map<Long, Double> serviceProviderIdsWithDistanceMap = serviceProviderServicesWithDistance
                .stream()
                .collect(Collectors.toMap(
                        ServiceProviderServiceWithDistanceProjection::getProviderId,
                        ServiceProviderServiceWithDistanceProjection::getDistanceMeters)); //servicePro

        // Get the service provider IDs from the map
        List<Long> serviceProviderIds = serviceProviderIdsWithDistanceMap.keySet().stream().toList();

        // Fetch service provider statistics for the service provider IDs
        List<ServiceProviderStatistics> providerStatistics = providerStatisticsRepository.findByProviderIdIn(serviceProviderIds);

        // Check for null statistics and create default statistics if needed
        List<ServiceProviderStatistics> defaultProviderStatisticsList = checkForNullStatistics(serviceProviderIds, providerStatistics);

        // Add service provider statistics with defaults
        providerStatistics.addAll(defaultProviderStatisticsList);

        Map<Long, ServiceProviderStatistics> providerStatisticsMap = providerStatistics
                .stream()
                .collect(Collectors.toMap(stat -> stat.getProvider().getId(), Function.identity()));


        // Create a map of service provider ID to its score
        Map<Long, Double> providerScores = new HashMap<>();
        for (Long providerId : serviceProviderIdsWithDistanceMap.keySet()) {
            ServiceProviderStatistics serviceProviderStatistics = providerStatisticsMap.get(providerId);
            ServiceProviderServiceWithDistanceProjection
                    serviceProviderServiceWithDistanceFromJob = serviceProviderServiceWithDistanceFromJobMap.get(providerId);

            Double score = getProviderScore(serviceProviderStatistics, serviceProviderIdsWithDistanceMap.get(providerId));
            providerScores.put(providerId, score);
        }

        return providerScores.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(MAX_LEADS_SENT_TO_PROVIDERS)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Checks for null statistics for service providers and creates default statistics if needed.
     *
     * @param serviceProviderIds List of service provider IDs to check.
     * @param providerStatistics List of existing service provider statistics.
     * @return List of ServiceProviderStatistics with defaults for missing providers.
     */
    private List<ServiceProviderStatistics> checkForNullStatistics(List<Long> serviceProviderIds, List<ServiceProviderStatistics> providerStatistics) {
        Set<Long> existingStatIds = providerStatistics.stream()
                .map(stat -> stat.getProvider().getId())
                .collect(Collectors.toSet());

        List<Long> missingIds = serviceProviderIds.stream()
                .filter(id -> !existingStatIds.contains(id))
                .toList();

        List<ServiceProviderStatistics> defaultStatistics = new ArrayList<>();
        for (Long providerId : missingIds) {
            ServiceProviderStatistics defaultStat = serviceProviderStatisticsService.createDefaultStatistics(providerId);
            defaultStatistics.add(defaultStat);
        }

        return defaultStatistics;
    }


    private Double getProviderScore(ServiceProviderStatistics serviceProviderStatistics, Double distanceMeters) {
        return          (distanceMeters * -0.05)                               // Proximity: Closer is better.
//                        + (serviceProviderStatistics.getDayLeadLoad().doubleValue() * -0.2)   // Immediate Availability: Penalize high immediate load.
//                        + (serviceProviderStatistics.getWeekLeadLoad().doubleValue() * -0.1)  // Short-term Availability: Less penalty than day load.
//                        + (serviceProviderStatistics.getTwoWeekLeadLoad().doubleValue() * -0.05) // Medium-term Availability: Even less penalty.
//                        + (serviceProviderStatistics.getThreeWeekLeadLoad().doubleValue() * -0.02) // Longer-term Availability: Minimal penalty.
//                        + (serviceProviderStatistics.getMonthLeadLoad().doubleValue() * 0.01) // Long-term Demand: Reward consistent work.
                        + (serviceProviderStatistics.getAvgRating().doubleValue() * 0.5)     // Quality: High importance for customer satisfaction.
                        + (serviceProviderStatistics.getCompletedLeads() * 0.8)    ;         // Experience/Reliability: Reward completed jobs.
//                        + (serviceProviderStatistics.getRespondedLeads() * 0.3)             // Engagement: Reward active participation.
//                        + (serviceProviderStatistics.getRejectionRate().doubleValue() * -15.0) // Reliability: Heavily penalize high rejection rates.
//                        + (serviceProviderStatistics.getResponseRate().doubleValue() * 10.0) // Responsiveness: Reward high response rates.
//                        - (serviceProviderStatistics.getAvgResponseTime().doubleValue() * 0.5); // Speed: Penalize longer response times.


    }
}
