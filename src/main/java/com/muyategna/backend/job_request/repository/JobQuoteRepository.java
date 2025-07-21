package com.muyategna.backend.job_request.repository;

import com.muyategna.backend.job_request.entity.JobQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobQuoteRepository extends JpaRepository<JobQuote, Long> {

    @Query("""
            SELECT j FROM JobQuote j
            WHERE j.claim.id = :claimId 
            AND j.claim.provider.id = :serviceProviderId
            """)
    List<JobQuote> findByClaimIdAndServiceProviderId(Long claimId, Long serviceProviderId);
}
