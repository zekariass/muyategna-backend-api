package com.muyategna.backend.job_request.repository;

import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.enums.JobLeadClaimStatus;
import com.muyategna.backend.job_request.repository.custom.JobLeadClaimRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JobLeadClaimRepository extends JpaRepository<JobLeadClaim, Long>, JobLeadClaimRepositoryCustom {

    boolean existsByJobLeadIdAndProviderId(Long jobLeadId, Long serviceProviderId);

    boolean existsByIdAndProviderIdAndStatus(Long claimId, Long serviceProviderId, JobLeadClaimStatus jobLeadClaimStatus);

    @Query("""
            SELECT j FROM JobLeadClaim j
            JOIN FETCH j.jobLead jl
            JOIN FETCH jl.jobRequest jr
            JOIN FETCH jr.address
            JOIN FETCH jr.service
            WHERE j.id = :leadClaimId
            """)
    Optional<JobLeadClaim> findByIdWithJobLeadAndJobRequest(Long leadClaimId);

    int countByJobLeadId(Long jobLeadId);

    //    @Query("""
//            SELECT j FROM JobLeadClaim j
//            JOIN FETCH j.jobLead jl
//            JOIN FETCH jl.jobRequest jr
//            JOIN FETCH jr.service
//            WHERE j.provider.id = :serviceProviderId AND j.status = :status
//            """)
    @EntityGraph(attributePaths = {"jobLead", "jobLead.jobRequest", "jobLead.jobRequest.service"})
    Page<JobLeadClaim> findByProviderIdAndStatus(Long serviceProviderId, JobLeadClaimStatus status, Pageable pageable);

    @Query("""
                SELECT DISTINCT j FROM JobLeadClaim j
                JOIN FETCH j.jobLead jl
                JOIN FETCH j.quotes
                JOIN FETCH jl.jobRequest jr
                JOIN FETCH jr.service
                WHERE j.id = :jobLeadClaimId AND j.provider.id = :serviceProviderId
            """)
    Optional<JobLeadClaim> findByIdWithRelatedEntitiesForServiceProvider(Long jobLeadClaimId, Long serviceProviderId);


    @Query("""
                SELECT DISTINCT j FROM JobLeadClaim j
                JOIN FETCH j.jobLead jl
                JOIN FETCH j.quotes
                JOIN FETCH jl.jobRequest jr
                JOIN FETCH jr.service
                WHERE j.id = :jobLeadClaimId AND jr.customer.id = :customerId
            """)
    Optional<JobLeadClaim> findByIdWithRelatedEntitiesForCustomer(Long jobLeadClaimId, Long customerId);


    @Query("""
                SELECT DISTINCT j FROM JobLeadClaim j
                JOIN FETCH j.jobLead jl
                JOIN FETCH j.quotes
                WHERE j.id = :jobLeadClaimId
            """)
    Optional<JobLeadClaim> findByIdWithQuotes(Long jobLeadClaimId);


    Optional<JobLeadClaim> findByIdAndProviderId(Long jobLeadClaimId, Long serviceProviderId);

    List<JobLeadClaim> findAllByJobLeadIdIn(List<Long> jobLeadIds);
}
