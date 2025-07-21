package com.muyategna.backend.service_provider.repository;

import com.muyategna.backend.service_provider.entity.ServiceProviderService;
import com.muyategna.backend.service_provider.repository.custom.ServiceProviderServiceRepositoryCustom;
import com.muyategna.backend.service_provider.repository.projection.ServiceProviderServiceWithDistanceProjection;
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

    @Query(value = """
    SELECT 
        sps.id AS id,
        sps.service_id AS serviceId,
        sps.provider_id AS providerId,
        sps.is_active AS isActive,
        ST_Distance(a.geo_point::geography, cast(:geoPoint AS geography)) AS distanceMeters,

        spc.balance AS creditBalance,
        scp.credits_per_lead AS creditsPerLead,

        (spc.balance IS NOT NULL AND scp.credits_per_lead IS NOT NULL AND spc.balance >= scp.credits_per_lead) 
            AS hasEnoughCredits,

        (spsub.id IS NOT NULL 
            AND (spsub.expires_at IS NULL OR spsub.expires_at > NOW()) 
            AND (COALESCE(spsub.initial_leads, 0) - COALESCE(spsub.used_leads, 0)) > 0)
            AS hasActiveSubscription

    FROM service_provider_services sps
    JOIN service_provider sp ON sps.provider_id = sp.id
    JOIN address a ON sp.business_address_id = a.address_id
    JOIN service srv ON sps.service_id = srv.id

    LEFT JOIN service_provider_credit spc ON spc.provider_id = sp.id
    LEFT JOIN service_credit_plan scp ON scp.service_id = srv.id

    LEFT JOIN service_provider_subscription spsub ON spsub.provider_id = sp.id

    WHERE 
        srv.id = :serviceId
        AND sps.is_active = TRUE
        AND sp.is_active = TRUE
        AND sp.is_verified = TRUE
        AND sp.is_blocked = FALSE
        AND (
            sp.max_travel_distance_in_km IS NULL
            OR (sp.max_travel_distance_in_km * 1000) <= :radiusMeters
        )
        AND ST_DWithin(
            a.geo_point::geography,
            cast(:geoPoint AS geography),
            :radiusMeters
        )
        AND (
            (spc.balance IS NOT NULL AND scp.credits_per_lead IS NOT NULL AND spc.balance >= scp.credits_per_lead)
            OR 
            (spsub.id IS NOT NULL 
                AND (spsub.expires_at IS NULL OR spsub.expires_at > NOW()) 
                AND (COALESCE(spsub.initial_leads, 0) - COALESCE(spsub.used_leads, 0)) > 0)
        )
""", nativeQuery = true)
    Set<ServiceProviderServiceWithDistanceProjection> findServiceProviderServicesByServiceAndLocation(
            @Param("serviceId") Long serviceId,
            @Param("geoPoint") Point geoPoint,
            @Param("radiusMeters") Double radiusMeters
    );


    @Query(value = """
    SELECT 
        sps.id AS id,
        sps.service_id AS serviceId,
        sps.provider_id AS providerId,
        sps.is_active AS isActive,
        ST_Distance(a.geo_point::geography, cast(:geoPoint as geography)) AS distanceMeters,
        
        spc.balance AS creditBalance,
        scp.credits_per_lead AS creditsPerLead,
        (spc.balance IS NOT NULL AND scp.credits_per_lead IS NOT NULL AND spc.balance >= scp.credits_per_lead) AS hasEnoughCredits,
        
        (spsub.id IS NOT NULL 
            AND (spsub.expires_at IS NULL OR spsub.expires_at > NOW()) 
            AND (COALESCE(spsub.initial_leads, 0) - COALESCE(spsub.used_leads, 0)) > 0) AS hasActiveSubscription

    FROM service_provider_services sps
    JOIN service_provider sp ON sps.provider_id = sp.id
    JOIN address a ON sp.business_address_id = a.address_id
    JOIN service srv ON sps.service_id = srv.id

    LEFT JOIN service_provider_credit spc ON spc.provider_id = sp.id
    LEFT JOIN service_credit_plan scp ON scp.service_id = srv.id
    LEFT JOIN service_provider_subscription spsub ON spsub.provider_id = sp.id

    WHERE 
        srv.id = :serviceId
        AND sps.is_active = TRUE
        AND sp.is_active = TRUE
        AND sp.is_verified = TRUE
        AND sp.is_blocked = FALSE
        AND (
            sp.max_travel_distance_in_km IS NULL
            OR (sp.max_travel_distance_in_km * 1000) <= :radiusMeters
        )
        AND sp.id NOT IN (:excludedServiceProviderIds)
        AND ST_DWithin(
            a.geo_point::geography,
            cast(:geoPoint as geography),
            :radiusMeters
        )
        AND (
            (spc.balance IS NOT NULL AND scp.credits_per_lead IS NOT NULL AND spc.balance >= scp.credits_per_lead)
            OR 
            (spsub.id IS NOT NULL AND (COALESCE(spsub.initial_leads, 0) - COALESCE(spsub.used_leads, 0)) > 0)
        )
""", nativeQuery = true)
    Set<ServiceProviderServiceWithDistanceProjection> findServiceProviderServicesByServiceAndLocation(
            @Param("serviceId") Long serviceId,
            @Param("geoPoint") Point geoPoint,
            @Param("radiusMeters") Double radiusMeters,
            @Param("excludedServiceProviderIds") List<Long> excludedServiceProviderIds
    );


    List<ServiceProviderService> findByProviderIdAndIsActive(Long serviceProviderId, boolean isActive);

    boolean existsByProviderIdAndServiceIdAndIsActiveTrue(Long serviceProviderId, Long serviceId);

    Optional<ServiceProviderService> getServiceProviderServiceByProviderIdAndServiceIdAndIsActiveTrue(Long serviceProviderId, Long serviceId);
}
