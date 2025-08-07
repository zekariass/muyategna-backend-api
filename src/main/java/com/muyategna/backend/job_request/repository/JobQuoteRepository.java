package com.muyategna.backend.job_request.repository;

import com.muyategna.backend.job_request.entity.JobQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobQuoteRepository extends JpaRepository<JobQuote, Long> {

    @Query("""
            SELECT j FROM JobQuote j
            WHERE j.claim.id = :claimId
            AND j.claim.provider.id = :serviceProviderId
            """)
    List<JobQuote> findByClaimIdAndServiceProviderId(
            @Param("claimId") Long claimId,
            @Param("serviceProviderId") Long serviceProviderId);


    @Query("""
            SELECT j FROM JobQuote j
            WHERE j.claim.id = :leadClaimId
            AND j.claim.jobLead.jobRequest.customer.id = :customerId
            """)
    List<JobQuote> findByClaimIdAndCustomerId(
            @Param("leadClaimId") Long leadClaimId,
            @Param("customerId") Long customerId);


    @Query("""
            SELECT j FROM JobQuote j
            WHERE j.id = :quoteId
            AND j.claim.jobLead.jobRequest.customer.id = :customerId
            """)
    Optional<JobQuote> findByQuoteIdAndCustomerId(
            @Param("quoteId") Long quoteId,
            @Param("customerId") Long customerId);
}
