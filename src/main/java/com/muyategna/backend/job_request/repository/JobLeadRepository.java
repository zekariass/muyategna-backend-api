package com.muyategna.backend.job_request.repository;

import com.muyategna.backend.job_request.entity.JobLead;
import com.muyategna.backend.job_request.repository.projection.JobLeadProjection;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobLeadRepository extends JpaRepository<JobLead, Long> {

//    @Query(value = """
//    SELECT
//        jl.id AS id,
//        jl.job_request_id AS jobRequestId,
//        jl.status AS status,
//        jl.max_claims AS maxClaims,
//        jl.claimed_count AS claimedCount,
//        jl.created_at AS createdAt,
//        ST_Distance(ad.geo_point::geography, cast(:geoPoint AS geography)) AS distanceMeters
//    FROM job_lead jl
//    JOIN job_request jr ON jl.job_request_id = jr.id
//    JOIN address ad ON jr.address_id = ad.address_id
//
//    JOIN job_lead_claim jlc ON jlc.job_lead_id = jl.id
//
//    WHERE
//        (jl.status = 'OPEN' OR jl.status = 'CLAIMED_PARTIALLY')
//        AND jl.max_claims > jl.claimed_count
//        AND jr.service_id IN :serviceIds
//        AND ST_DWithin(
//            ad.geo_point::geography,
//            cast(:geoPoint AS geography),
//            :maxTravelDistanceInMeters
//        )
//        AND (jr.status = 'OPEN' OR jr.status = 'REOPEN')
//
//        AND (jlc.job_lead_id != jl.id AND jlc.provider_id != :serviceProviderId)
//    """,
//            countQuery = """
//    SELECT COUNT(*) FROM job_lead jl
//    JOIN job_request jr ON jl.job_request_id = jr.id
//    JOIN address ad ON jr.address_id = ad.address_id
//    JOIN job_lead_claim jlc ON jlc.job_lead_id = jl.id
//    WHERE
//        (jl.status = 'OPEN' OR jl.status = 'CLAIMED_PARTIALLY')
//        AND jl.max_claims > jl.claimed_count
//        AND jr.service_id IN :serviceIds
//        AND ST_DWithin(
//            ad.geo_point::geography,
//            cast(:geoPoint AS geography),
//            :maxTravelDistanceInMeters
//        )
//        AND (jr.status = 'OPEN' OR jr.status = 'REOPEN')
//
//        AND (jlc.job_lead_id != jl.id AND jlc.provider_id != :serviceProviderId)
//    """,
//            nativeQuery = true
//    )
//    Page<JobLeadProjection> findAllOpenLeadsByServiceIdIn(
//            @Param("serviceIds") List<Long> serviceIds,
//            @Param("geoPoint") Point geoPoint,
//            @Param("maxTravelDistanceInMeters") Double maxTravelDistanceInMeters,
//            @Param("serviceProviderId") Long serviceProviderId,
//            Pageable pageable
//    );

    @Query(value = """
    SELECT 
        jl.id AS id,
        jl.job_request_id AS jobRequestId,
        jl.status AS status,
        jl.max_claims AS maxClaims,
        jl.claimed_count AS claimedCount,
        jl.created_at AS createdAt,
        ST_Distance(ad.geo_point::geography, cast(:geoPoint AS geography)) AS distanceMeters
    FROM job_lead jl
    JOIN job_request jr ON jl.job_request_id = jr.id
    JOIN address ad ON jr.address_id = ad.address_id
    LEFT JOIN job_lead_claim jlc 
        ON jlc.job_lead_id = jl.id AND jlc.provider_id = :serviceProviderId
    WHERE 
        (jl.status = 'OPEN' OR jl.status = 'CLAIMED_PARTIALLY')
        AND jl.max_claims > jl.claimed_count
        AND jr.service_id IN :serviceIds
        AND ST_DWithin(
            ad.geo_point::geography, 
            cast(:geoPoint AS geography), 
            :maxTravelDistanceInMeters
        )
        AND (jr.status = 'OPEN' OR jr.status = 'REOPEN')
        AND jlc.id IS NULL
    """,
            countQuery = """
    SELECT COUNT(*) FROM job_lead jl
    JOIN job_request jr ON jl.job_request_id = jr.id
    JOIN address ad ON jr.address_id = ad.address_id
    LEFT JOIN job_lead_claim jlc 
        ON jlc.job_lead_id = jl.id AND jlc.provider_id = :serviceProviderId
    WHERE 
        (jl.status = 'OPEN' OR jl.status = 'CLAIMED_PARTIALLY')
        AND jl.max_claims > jl.claimed_count
        AND jr.service_id IN :serviceIds
        AND ST_DWithin(
            ad.geo_point::geography, 
            cast(:geoPoint AS geography), 
            :maxTravelDistanceInMeters
        )
        AND (jr.status = 'OPEN' OR jr.status = 'REOPEN')
        AND jlc.id IS NULL
    """,
            nativeQuery = true
    )
    Page<JobLeadProjection> findAllOpenLeadsByServiceIdIn(
            @Param("serviceIds") List<Long> serviceIds,
            @Param("geoPoint") Point geoPoint,
            @Param("maxTravelDistanceInMeters") Double maxTravelDistanceInMeters,
            @Param("serviceProviderId") Long serviceProviderId,
            Pageable pageable
    );



    @Query("""
        SELECT jl FROM JobLead jl
        JOIN FETCH jl.jobRequest jr
        JOIN FETCH jr.service
        WHERE jl.id = :id
        """)
    Optional<JobLead> findWithRequestAndService(@Param("id") Long id);
}
