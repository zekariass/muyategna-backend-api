package com.muyategna.backend.job_request.repository;

import com.muyategna.backend.job_request.entity.JobLeadClaim;
import com.muyategna.backend.job_request.enums.JobLeadClaimStatus;
import com.muyategna.backend.job_request.repository.custom.JobLeadClaimRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JobLeadClaimRepository extends JpaRepository<JobLeadClaim, Long>, JobLeadClaimRepositoryCustom {

    boolean existsByJobLeadIdAndProviderId(Long jobLeadId, Long serviceProviderId);

    boolean existsByIdAndProviderIdAndStatus(Long claimId, Long serviceProviderId, JobLeadClaimStatus jobLeadClaimStatus);

    @Query("""
            SELECT j FROM JobLeadClaim j
            JOIN FETCH j.jobLead jl
            JOIN FETCH jl.jobRequest jr
            WHERE j.id = :leadClaimId
            """)
    Optional<JobLeadClaim> findByIdWithJobLeadAndJobRequest(Long leadClaimId);
}
