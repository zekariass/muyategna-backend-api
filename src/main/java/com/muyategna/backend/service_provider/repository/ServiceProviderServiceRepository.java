package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceProviderService;
import com.muyategna.backend.service_provider.repository.custom.ServiceProviderServiceRepositoryCustom;
import com.muyategna.backend.service_provider.repository.projection.ProviderServiceStatProjection;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ServiceProviderServiceRepository extends JpaRepository<ServiceProviderService, Long>, ServiceProviderServiceRepositoryCustom {
    List<ServiceProviderService> findByProviderId(Long serviceProviderId);

    Optional<ServiceProviderService> findByIdAndProviderId(Long serviceProviderServiceId, Long serviceProviderId);

    /**
     * Finds a list of service provider services based on the given service ID, geographical point, radius,
     * and country ID. The results are filtered by active status, verification status, and
     * travel distance constraints. The query joins several tables to gather service provider statistics
     * such as total claims, average claim time, average rating, and more. The results are ordered by
     * distance and limited to a specified number of entries.
     *
     * @param geoPointWkt      the geographical point (latitude and longitude) to measure distances from
     * @param serviceId        the ID of the service
     * @param deliveryMode
     * @param radiusMeters     the radius in meters within which to find services
     * @param currentCountryId the ID of the current country for filtering services
     * @param queryLimit       the maximum number of results to return
     * @return a list of {@link com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceAndStatisticsProjection} containing service provider
     * service details and statistics within the specified distance and matching the criteria
     * //
     */
//    @Query(value = """
//            SELECT
//                sps.id AS serviceProviderServiceId,
//                sps.service_id AS serviceId,
//                sps.provider_id AS providerId,
//                sps.is_active AS isActive,
//                ST_Distance(a.geo_point::geography, cast(:geoPoint AS geography)) AS distanceMeters,
//
//                stat.total_claims AS totalClaims,
//                stat.avg_claim_time_in_mins AS avgClaimTimeInMins,
//                stat.avg_rating AS avgRating,
//                stat.completed_leads AS completedLeads,
//                stat.total_quotes AS totalQuotes,
//                stat.accepted_quotes AS acceptedQuotes,
//                stat.daily_claim_count AS dailyClaimCount,
//                stat.weekly_claim_count AS weeklyClaimCount,
//                stat.two_weekly_claim_count AS twoWeeklyClaimCount,
//                stat.monthly_claim_count AS monthlyClaimCount
//
//            FROM service_provider_services sps
//            JOIN service_provider sp ON sps.provider_id = sp.id
//            JOIN address a ON sp.business_address_id = a.address_id
//            JOIN service srv ON sps.service_id = srv.id
//
//            LEFT JOIN service_provider_statistics stat ON stat.provider_id = sp.id
//
//            WHERE
//                srv.id = :serviceId
//                AND sps.is_active = TRUE
//                AND sp.is_active = TRUE
//                AND sp.is_verified = TRUE
//                AND sp.is_blocked = FALSE
//                AND a.country_id = :currentCountryId
//                AND (
//                    sp.max_travel_distance_in_km IS NULL
//                    OR ST_DWithin(
//                    a.geo_point::geography,
//                    cast(:geoPoint AS geography),
//                    sp.max_travel_distance_in_km * 1000
//                    )
//                )
//                AND ST_DWithin(
//                    a.geo_point::geography,
//                    cast(:geoPoint AS geography),
//                    :radiusMeters * 1000
//                )
//
//                ORDER BY distanceMeters
//                LIMIT :queryLimit
//            """, nativeQuery = true)
//    List<ProviderServiceStatProjection> findServiceProviderServicesByServiceAndLocation(
//            @Param("serviceId") Long serviceId,
//            @Param("geoPoint") Point geoPoint,
//            @Param("radiusMeters") Double radiusMeters,
//            @Param("currentCountryId") Long currentCountryId,
//            @Param("queryLimit") int queryLimit);
    @Query(value = """
                SELECT
                    sps.id AS serviceProviderServiceId,
                    sps.service_id AS serviceId,
                    sps.provider_id AS providerId,
                    sps.is_active AS isActive,
                    ST_Distance(a.geo_point::geography, ST_GeomFromText(:geoPoint, 4326)::geography) AS distanceMeters,
            
                    stat.total_claims AS totalClaims,
                    stat.avg_claim_time_in_mins AS avgClaimTimeInMins,
                    stat.avg_rating AS avgRating,
                    stat.completed_leads AS completedLeads,
                    stat.total_quotes AS totalQuotes,
                    stat.accepted_quotes AS acceptedQuotes,
                    stat.daily_claim_count AS dailyClaimCount,
                    stat.weekly_claim_count AS weeklyClaimCount,
                    stat.two_weekly_claim_count AS twoWeeklyClaimCount,
                    stat.monthly_claim_count AS monthlyClaimCount,
                    stat.total_new_job_notifs AS totalNewJobNotifs,
                    stat.daily_new_job_notifs AS dailyNewJobNotifs,
                    stat.weekly_new_job_notifs AS weeklyNewJobNotifs
            
                FROM service_provider_services sps
                JOIN service_provider sp ON sps.provider_id = sp.id
                JOIN address a ON sp.business_address_id = a.address_id
                JOIN service srv ON sps.service_id = srv.id
            
                LEFT JOIN service_provider_statistics stat ON stat.provider_id = sp.id
            
                WHERE
                    srv.id = :serviceId
                    AND sps.is_active = TRUE
                    AND sp.is_active = TRUE
                    AND sp.is_verified = TRUE
                    AND sp.is_blocked = FALSE
                    AND sp.is_deleted = FALSE
                    AND a.country_id = :currentCountryId
                    AND (
                        CAST(:deliveryMode AS service_delivery_mode_enum) = 'BOTH'
                        OR sps.delivery_mode = 'BOTH'
                        OR sps.delivery_mode = CAST(:deliveryMode AS service_delivery_mode_enum)
                    )
                    AND (
                         sps.delivery_mode != 'ONSITE'
                         OR (
                             (sp.max_travel_distance_in_km IS NULL OR ST_DWithin(
                                 a.geo_point::geography,
                                 ST_GeomFromText(:geoPoint, 4326)::geography,
                                 sp.max_travel_distance_in_km * 1000
                             ))
                             AND ST_DWithin(
                                 a.geo_point::geography,
                                 ST_GeomFromText(:geoPoint, 4326)::geography,
                                 :radiusMeters * 1000
                             )
                         )
                     )
            
                ORDER BY 
                  (
                      ( ST_Distance(
                           a.geo_point::geography,
                           ST_GeomFromText(:geoPoint, 4326)::geography
                       )
                   /1000)
                   + COALESCE(stat.daily_new_job_notifs, 0) * 30
                   + COALESCE(stat.weekly_new_job_notifs, 0) * 5
               ) ASC
                LIMIT :queryLimit
            """, nativeQuery = true)
    List<com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceAndStatisticsProjection> findServiceProviderServicesByServiceAndLocation(
            @Param("serviceId") Long serviceId,
            @Param("deliveryMode") String deliveryMode,
            @Param("geoPoint") String geoPointWkt,  // Now a WKT string like "POINT(80.123 13.456)"
            @Param("radiusMeters") Double radiusMeters,
            @Param("currentCountryId") Long currentCountryId,
            @Param("queryLimit") int queryLimit);


    @Query(value = """
                SELECT
                    sps.id AS serviceProviderServiceId,
                    sps.service_id AS serviceId,
                    sps.provider_id AS providerId,
                    sps.is_active AS isActive,
                    ST_Distance(a.geo_point::geography, ST_GeomFromText(:geoPoint, 4326)::geography) AS distanceMeters,
            
                    stat.total_claims AS totalClaims,
                    stat.avg_claim_time_in_mins AS avgClaimTimeInMins,
                    stat.avg_rating AS avgRating,
                    stat.completed_leads AS completedLeads,
                    stat.total_quotes AS totalQuotes,
                    stat.accepted_quotes AS acceptedQuotes,
                    stat.daily_claim_count AS dailyClaimCount,
                    stat.weekly_claim_count AS weeklyClaimCount,
                    stat.two_weekly_claim_count AS twoWeeklyClaimCount,
                    stat.monthly_claim_count AS monthlyClaimCount,
                    stat.total_new_job_notifs AS totalNewJobNotifs,
                    stat.daily_new_job_notifs AS dailyNewJobNotifs,
                    stat.weekly_new_job_notifs AS weeklyNewJobNotifs
            
                FROM service_provider_services sps
                JOIN service_provider sp ON sps.provider_id = sp.id
                JOIN address a ON sp.business_address_id = a.address_id
                JOIN service srv ON sps.service_id = srv.id
            
                LEFT JOIN service_provider_statistics stat ON stat.provider_id = sp.id
            
                WHERE
                    srv.id = :serviceId
                    AND sps.is_active = TRUE
                    AND sp.is_active = TRUE
                    AND sp.is_verified = TRUE
                    AND sp.is_blocked = FALSE
                    AND sp.is_deleted = FALSE
                    AND a.country_id = :currentCountryId
                    AND (
                        CAST(:deliveryMode AS service_delivery_mode_enum) = 'BOTH'
                        OR sps.delivery_mode = 'BOTH'
                        OR sps.delivery_mode = CAST(:deliveryMode AS service_delivery_mode_enum)
                    )
                    AND (
                         sps.delivery_mode != 'ONSITE'
                         OR (
                             (sp.max_travel_distance_in_km IS NULL OR ST_DWithin(
                                 a.geo_point::geography,
                                 ST_GeomFromText(:geoPoint, 4326)::geography,
                                 sp.max_travel_distance_in_km * 1000
                             ))
                             AND ST_DWithin(
                                 a.geo_point::geography,
                                 ST_GeomFromText(:geoPoint, 4326)::geography,
                                 :radiusMeters * 1000
                             )
                         )
                     )
                    AND sp.id NOT IN (:excludedServiceProviderIds)
            
                ORDER BY 
                  (
                      ( ST_Distance(
                           a.geo_point::geography,
                           ST_GeomFromText(:geoPoint, 4326)::geography
                       )
                   /1000)
                   + COALESCE(stat.daily_new_job_notifs, 0) * 30
                   + COALESCE(stat.weekly_new_job_notifs, 0) * 5
               ) ASC
                LIMIT :queryLimit
            """, nativeQuery = true)
    Set<com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceAndStatisticsProjection> findServiceProviderServicesByServiceAndLocation(
            @Param("serviceId") Long serviceId,
            @Param("deliveryMode") String deliveryMode,
            @Param("geoPoint") Point geoPoint,
            @Param("radiusMeters") Double radiusMeters,
            @Param("excludedServiceProviderIds") List<Long> excludedServiceProviderIds,
            @Param("currentCountryId") Long currentCountryId
    );


    // Mache Service Provider Services to a given Job Request

    @Query(value = """
                SELECT
                    sps.id AS serviceProviderServiceId,
                    sps.provider_id AS providerId,
                    ST_Distance(a.geo_point::geography, ST_GeomFromText(:jobRequestGeoPoint, 4326)::geography) AS distanceMeters,
            
                    stat.total_claims AS totalClaims,
                    stat.avg_claim_time_in_mins AS avgClaimTimeInMins,
                    stat.avg_rating AS avgRating,
                    stat.completed_leads AS completedLeads,
                    stat.total_quotes AS totalQuotes,
                    stat.accepted_quotes AS acceptedQuotes,
                    stat.daily_claim_count AS dailyClaimCount,
                    stat.weekly_claim_count AS weeklyClaimCount,
                    stat.two_weekly_claim_count AS twoWeeklyClaimCount,
                    stat.monthly_claim_count AS monthlyClaimCount,
                    stat.total_new_job_notifs AS totalNewJobNotifs,
                    stat.daily_new_job_notifs AS dailyNewJobNotifs,
                    stat.weekly_new_job_notifs AS weeklyNewJobNotifs
            
                FROM service_provider_services sps
                JOIN service_provider sp ON sps.provider_id = sp.id
                JOIN address a ON sp.business_address_id = a.address_id
                JOIN service srv ON sps.service_id = srv.id
            
                LEFT JOIN service_provider_statistics stat ON stat.provider_id = sp.id
            
                WHERE
                    srv.id = :serviceId
                    AND sps.is_active = TRUE
                    AND sp.is_active = TRUE
                    AND sp.is_verified = TRUE
                    AND sp.is_blocked = FALSE
                    AND sp.is_deleted = FALSE
                    AND a.country_id = :currentCountryId
                    AND (
                        CAST(:deliveryMode AS service_delivery_mode_enum) = 'BOTH'
                        OR sps.delivery_mode = 'BOTH'
                        OR sps.delivery_mode = CAST(:deliveryMode AS service_delivery_mode_enum)
                    )
                    AND (
                         sps.delivery_mode != 'ONSITE'
                         OR (
                             (sp.max_travel_distance_in_km IS NULL OR ST_DWithin(
                                 a.geo_point::geography,
                                 ST_GeomFromText(:jobRequestGeoPoint, 4326)::geography,
                                 sp.max_travel_distance_in_km * 1000
                             ))
                             AND ST_DWithin(
                                 a.geo_point::geography,
                                 ST_GeomFromText(:jobRequestGeoPoint, 4326)::geography,
                                 :radiusMeters * 1000
                             )
                         )
                     )            
                ORDER BY 
                  (
                      ( ST_Distance(
                           a.geo_point::geography,
                           ST_GeomFromText(:jobRequestGeoPoint, 4326)::geography
                       )
                   /1000)
                   + COALESCE(stat.daily_new_job_notifs, 0) * 30
                   + COALESCE(stat.weekly_new_job_notifs, 0) * 5
               ) ASC
                LIMIT :queryLimit
            """, nativeQuery = true)
    List<ProviderServiceStatProjection> getServiceProviderServicesByLocationAndService(
            @Param("serviceId") Long serviceId,
            @Param("deliveryMode") String deliveryMode,
            @Param("jobRequestGeoPoint") String jobRequestGeoPoint,
            @Param("radiusMeters") double radiusMeters,
            @Param("currentCountryId") Long countryId,
            @Param("queryLimit") int queryLimit);


    List<ServiceProviderService> findByProviderIdAndIsActive(Long serviceProviderId, boolean isActive);

    boolean existsByProviderIdAndServiceIdAndIsActiveTrue(Long serviceProviderId, Long serviceId);

    Optional<ServiceProviderService> getServiceProviderServiceByProviderIdAndServiceIdAndIsActiveTrue(Long serviceProviderId, Long serviceId);

}
