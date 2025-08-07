package com.muyategna.backend.job_request.algorithm;

import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.entity.JobRequest;
import com.muyategna.backend.job_request.enums.JobLeadStatus;
import com.muyategna.backend.job_request.enums.JobStatus;
import com.muyategna.backend.job_request.repository.JobRequestRepository;
import com.muyategna.backend.location.entity.Address;
import com.muyategna.backend.professional_service.entity.Service;
import com.muyategna.backend.service_provider.entity.ServiceProviderRankingWeight;
import com.muyategna.backend.service_provider.repository.projection.NearbyServiceProviderForAJobProjection;
import com.muyategna.backend.service_provider.repository.projection.ProviderServiceStatProjection;
import com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceAndStatisticsProjection;
import com.muyategna.backend.service_provider.service.ServiceProviderRankingWeightServiceImpl;
import com.muyategna.backend.service_provider.service.ServiceProviderServiceInterface;
import com.muyategna.backend.system.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@Slf4j
public class JobMatchingAlgorithmServiceImpl implements JobMatchingAlgorithmService {

    private final SystemConfigService systemConfigService;
    private final ServiceProviderServiceInterface serviceProviderService;
    private final ServiceProviderRankingWeightServiceImpl rankingWeightService;
    private final JobRequestRepository jobRequestRepository;
    private final ServiceProviderServiceInterface serviceProviderServiceInterface;

    @Autowired
    public JobMatchingAlgorithmServiceImpl(SystemConfigService systemConfigService,
                                           ServiceProviderServiceInterface serviceProviderService,
                                           ServiceProviderRankingWeightServiceImpl rankingWeightService,
                                           JobRequestRepository jobRequestRepository, ServiceProviderServiceInterface serviceProviderServiceInterface) {
        this.systemConfigService = systemConfigService;
        this.serviceProviderService = serviceProviderService;
        this.rankingWeightService = rankingWeightService;
        this.jobRequestRepository = jobRequestRepository;
        this.serviceProviderServiceInterface = serviceProviderServiceInterface;
    }


    @Override
    public List<Long> getTopNServiceProviders(Long jobRequestId, int topN, Long countryId, int queryLimit) {
        // Get the job request associated with the lead
        Optional<JobRequest> jobRequest = jobRequestRepository.findByIdWithRelatedEntities(jobRequestId);
        if (validateJobRequest(jobRequestId, jobRequest)) return null;

        // Get the service associated with the job request
        Service service = jobRequest.get().getService();
        if (validateService(service, jobRequestId)) return null;

        // Get the Job location associated with the job request
        Address jobLocation = jobRequest.get().getAddress();

        // Get the service providers as service provider service that match the job request, service and location
        double defaultRadius = Double.parseDouble(systemConfigService.getSystemConfigByName("DEFAULT_TRAVEL_DISTANCE_IN_KM").getValue());

        // Get the NEARBY_PROVIDERS_QUERY_LIMIT from the system config
        List<com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceAndStatisticsProjection> serviceProviderServicesWithDistance = serviceProviderService.getServiceProviderServicesByServiceAndLocation(
                service.getId(),
                jobRequest.get().getDeliveryMode().name(),
                new WKTWriter().write(jobLocation.getGeoPoint()),
                defaultRadius,
                countryId,
                queryLimit
        );

//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>: " + jobRequest.get().getDeliveryMode().name());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>: " + serviceProviderServicesWithDistance.size());


        // Rank the service providers based on their Statistics and distance from the job location
        List<Long> rankedServiceProvidersIds = rankServiceProvidersForNewJobNotification(serviceProviderServicesWithDistance);

//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>: " + rankedServiceProvidersIds);
        return rankedServiceProvidersIds.subList(0, Math.min(rankedServiceProvidersIds.size(), topN));
    }

    /**
     * Get ranked service providers to display for the customer that are close to the job request
     *
     * @param radiusMeters   radius in meters
     * @param queryLimit     number of service providers to return from db
     * @param numOfProviders number of service providers to show the customer
     * @param countryId
     * @return list of service providers
     */
    @Override
    public List<NearbyServiceProviderForAJobProjection> getNearbyServiceProviders(
            JobRequest jobRequest,
            double radiusMeters,
            int queryLimit,
            int numOfProviders,
            Long countryId) {
        log.info("Getting service providers for job request");

        Long serviceId = jobRequest.getService().getId();
        Point jobRequestGeoPoint = jobRequest.getAddress().getGeoPoint();
        String deliveryMode = jobRequest.getDeliveryMode().name();

        // Get the service providers as service provider service that match the job request, service and location
        List<ProviderServiceStatProjection> providerServiceStatProjections =
                serviceProviderService.getServiceProviderServicesByLocationAndService(
                        serviceId,
                        deliveryMode,
                        new WKTWriter().write(jobRequestGeoPoint),
                        radiusMeters,
                        countryId,
                        queryLimit);

        // Get the OPEN or PARTIALLY_OPEN lead to check if service providers have claims already
        // There is a constraint in the database to ensure that there is only one OPEN or PARTIALLY_OPEN lead
        JobLead jobLead = jobRequest.getJobLeads().stream()
                .filter(lead -> lead.getStatus().equals(JobLeadStatus.OPEN) || lead.getStatus().equals(JobLeadStatus.CLAIMED_PARTIALLY))
                .findFirst()
                .orElse(null);

        List<Long> serviceProviderListDtoList = rankServiceProvidersForCustomerView(providerServiceStatProjections);

//        System.out.println("==========================================================================>>>>: SIZE:" + serviceProviderListDtoList.size());

        List<Long> rankedServiceProviderIds = serviceProviderListDtoList.subList(0, Math.min(serviceProviderListDtoList.size(), numOfProviders));

        // Get the service providers.
        return serviceProviderServiceInterface.getNearbyServiceProvidersForCustomerView(
                rankedServiceProviderIds,
                jobLead,
                new WKTWriter().write(jobRequestGeoPoint)
        );
    }


    private static boolean validateService(Service service, Long jobRequestId) {
        if (service == null) {
            log.error("Service not found for job request with ID: {}", jobRequestId);
            return true;
        }
        return false;
    }

    private static boolean validateJobRequest(Long jobRequestId, Optional<JobRequest> jobRequest) {
        if (jobRequest.isEmpty()) {
            log.error("Job request not found with ID: {}", jobRequestId);
            return true;
        }

        if (!jobRequest.get().getStatus().equals(JobStatus.OPEN) && !jobRequest.get().getStatus().equals(JobStatus.REOPEN)) {
            log.error("Job request is not in OPEN or REOPEN status for ID: {}", jobRequest.get().getId());
            return true;
        }

        if (jobRequest.get().getAddress() == null) {
            log.error("Business address not found for job request with ID: {}", jobRequest.get().getId());
            return true;
        }
        return false;
    }


    /**
     * Rank the service providers based on their Statistics and distance from the job location
     * The higher the score, the higher the priority of the service provider
     * Then top N service providers are returned and notification will be sent to them about the new job
     *
     * @param serviceProviderServicesWithDistances List of service providers with distance and statistics from the job request
     * @return List of service provider IDs in descending order of their score
     */
    private List<Long> rankServiceProvidersForNewJobNotification(
            List<ServiceProviderServiceWithDistanceAndStatisticsProjection> serviceProviderServicesWithDistances) {

        // Create a map of service provider ID to its score
        Map<Long, Double> providerScores = new HashMap<>();
        for (ServiceProviderServiceWithDistanceAndStatisticsProjection projection : serviceProviderServicesWithDistances) {
            Double score = getProviderScore(
                    Optional.ofNullable(projection.getDistanceMeters()).orElse(0.0),
                    Optional.ofNullable(projection.getDailyClaimCount()).orElse(0),
                    Optional.ofNullable(projection.getWeeklyClaimCount()).orElse(0),
                    Optional.ofNullable(projection.getTwoWeeklyClaimCount()).orElse(0),
                    Optional.ofNullable(projection.getMonthlyClaimCount()).orElse(0),
                    Optional.ofNullable(projection.getAvgRating()).orElse(BigDecimal.valueOf(0.0))
            );
            providerScores.put(projection.getProviderId(), score);
        }

        return providerScores.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    private List<Long> rankServiceProvidersForCustomerView(List<ProviderServiceStatProjection> providerServiceStatProjections) {

        // Create a map of service provider ID to its score
        Map<Long, Double> providerScores = new HashMap<>();
        for (ProviderServiceStatProjection projection : providerServiceStatProjections) {
            Double score = getProviderScore(
                    Optional.ofNullable(projection.getDistanceMeters()).orElse(0.0),
                    Optional.ofNullable(projection.getDailyClaimCount()).orElse(0),
                    Optional.ofNullable(projection.getWeeklyClaimCount()).orElse(0),
                    Optional.ofNullable(projection.getTwoWeeklyClaimCount()).orElse(0),
                    Optional.ofNullable(projection.getMonthlyClaimCount()).orElse(0),
                    Optional.ofNullable(projection.getAvgRating()).orElse(BigDecimal.valueOf(0.0))
            );
            providerScores.put(projection.getProviderId(), score);
        }

        return providerScores.entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    private double getProviderScore(Double distanceMeters,
                                    Integer dailyClaimCount,
                                    Integer weeklyClaimCount,
                                    Integer twoWeeklyClaimCount,
                                    Integer monthlyClaimCount,
                                    BigDecimal avgRating) {

        ServiceProviderRankingWeight rankingWeights = rankingWeightService.getRankingWeights().orElse(new ServiceProviderRankingWeight());

        // DISTANCE SCORE
        BigDecimal distanceWeight = Optional.ofNullable(rankingWeights.getDistanceWeight()).orElse(BigDecimal.valueOf(0.0));
        double distanceScore = distanceMeters * distanceWeight.doubleValue();

        // AVAILABILITY SCORE
        BigDecimal dailyWeight = Optional.ofNullable(rankingWeights.getDailyClaimCountWeight()).orElse(BigDecimal.valueOf(0.0));
        BigDecimal weeklyWeight = Optional.ofNullable(rankingWeights.getWeeklyClaimCountWeight()).orElse(BigDecimal.valueOf(0.0));
        BigDecimal twoWeeklyWeight = Optional.ofNullable(rankingWeights.getTwoWeeklyClaimCountWeight()).orElse(BigDecimal.valueOf(0.0));
        BigDecimal monthlyWeight = Optional.ofNullable(rankingWeights.getMonthlyClaimCountWeight()).orElse(BigDecimal.valueOf(0.0));

        double availabilityScore =
                dailyClaimCount * dailyWeight.doubleValue() +
                        weeklyClaimCount * weeklyWeight.doubleValue() +
                        twoWeeklyClaimCount * twoWeeklyWeight.doubleValue() +
                        monthlyClaimCount * monthlyWeight.doubleValue();


        // RATING SCORE
        BigDecimal avgRatingWeight = Optional.ofNullable(rankingWeights.getAvgRatingWeight()).orElse(BigDecimal.valueOf(0.0));
        double ratingScore = avgRating.multiply(avgRatingWeight).doubleValue();


        // I removed experience score because it favours providers with more experience. It excludes new service providers

        return distanceScore +
                availabilityScore +
                ratingScore;
    }
}
