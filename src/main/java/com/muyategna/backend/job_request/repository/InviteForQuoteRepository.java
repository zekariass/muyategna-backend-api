package com.muyategna.backend.job_request.repository;

import com.muyategna.backend.job_request.entity.InviteForQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InviteForQuoteRepository extends JpaRepository<InviteForQuote, Long> {

    Boolean existsByJobRequestIdAndProviderId(Long jobRequestId, Long serviceProviderId);

    List<InviteForQuote> findByJobRequestId(Long jobRequestId);

    @Query(
            """
                    SELECT i FROM InviteForQuote i
                    JOIN FETCH i.jobRequest
                    JOIN FETCH i.provider
                    WHERE i.id = :inviteForQuoteId AND i.provider.id = :serviceProviderId
                    """
    )
    Optional<InviteForQuote> findByIdWithRelatedEntities(Long inviteForQuoteId, Long serviceProviderId);
}
