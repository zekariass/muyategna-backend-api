package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceProvider;
import com.muyategna.backend.service_provider.repository.projection.NearbyServiceProviderForAJobProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    @Modifying
    @Query("UPDATE ServiceProvider s " +
            "SET s.isActive = true, " +
            "s.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE s.id = :serviceProviderId")
    Integer activateServiceProviderById(Long serviceProviderId);


    @Modifying
    @Query("UPDATE ServiceProvider s " +
            "SET s.isActive = false, " +
            "s.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE s.id = :serviceProviderId")
    Integer inActivateServiceProviderById(Long serviceProviderId);

//
//    @Query("""
//                SELECT DISTINCT s
//                FROM ServiceProvider s
//                LEFT JOIN FETCH s.notificationPreferences np
//                WHERE s.id IN :serviceProviderIds
//                  AND np.notificationCategory.recipientType = 'SERVICE_PROVIDER'
//                  AND np.notificationCategory.id = :notificationCategoryId
//            """)
//    List<ServiceProvider> findServiceProvidersByIdsWithNotificationPreferences(
//            @Param("serviceProviderIds") List<Long> serviceProviderIds,
//            @Param("notificationCategoryId") Long notificationCategoryId);

    @Query("""
                SELECT DISTINCT s
                FROM ServiceProvider s
                LEFT JOIN FETCH s.notificationPreferences np
                WHERE s.id IN :serviceProviderIds
            """)
    List<ServiceProvider> findServiceProvidersByIdsWithNotificationPreferences(
            @Param("serviceProviderIds") List<Long> serviceProviderIds);

    @Query("""
                SELECT DISTINCT s
                FROM ServiceProvider s
                LEFT JOIN FETCH s.businessAddress ba
                WHERE s.id = :serviceProviderId
            """)
    Optional<ServiceProvider> findByIdWithBusinessAddress(Long serviceProviderId);


    @Query("""
                SELECT DISTINCT s
                FROM ServiceProvider s
                LEFT JOIN FETCH s.notificationPreferences np
                WHERE s.id = :providerId
            """)
    Optional<ServiceProvider> getServiceProviderByIdWithNotificationPreference(Long providerId);


    @Query(value =
            """
                    SELECT 
                        sp.id AS serviceProviderId,
                        sp.business_name AS businessName,
                        sp.business_description AS businessDescription,
                        a.full_address AS fullAddress,
                        sp.max_travel_distance_in_km AS maxTravelDistanceInKM,
                        sp.business_logo_url AS businessLogoUrl,
                        sp.average_rating AS averageRating,
                        sp.number_of_reviews AS numberOfReviews,
                        sp.years_of_experience AS yearsOfExperience,
                        sp.business_email AS businessEmail,
                        sp.business_phone AS businessPhone,
                        inv.id AS inviteForQuoteId,
                        cl.id AS claimId,
                        ST_Distance(
                            a.geo_point::geography, 
                            ST_GeomFromText(:jobGeoPoint, 4326)::geography
                        ) AS distanceFromJobInMeters
                    FROM 
                        service_provider sp
                        JOIN address a ON sp.business_address_id = a.address_id
                        LEFT JOIN invite_for_quote inv ON sp.id = inv.provider_id
                        LEFT JOIN job_lead_claim cl 
                          ON sp.id = cl.provider_id AND cl.job_lead_id = :leadId
                    WHERE 
                        sp.id IN (:rankedServiceProviderIds)
                    ORDER BY 
                        distanceFromJobInMeters ASC
                    """,
            nativeQuery = true
    )
    List<NearbyServiceProviderForAJobProjection> getNearbyServiceProvidersForCustomerView(
            @Param("rankedServiceProviderIds") List<Long> rankedServiceProviderIds,
            @Param("leadId") Long leadId,
            @Param("jobGeoPoint") String jobGeoPoint);


    @Query("""
                SELECT DISTINCT s
                FROM ServiceProvider s
                LEFT JOIN FETCH s.businessAddress ba
                WHERE s.id = :providerId
            """)
    ServiceProvider getServiceProviderWithBusinessAddressById(@Param("providerId") Long providerId);
}
