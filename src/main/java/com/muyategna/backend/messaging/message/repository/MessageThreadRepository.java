package com.muyategna.backend.messaging.message.repository;

import com.muyategna.backend.messaging.message.entity.MessageThread;
import com.muyategna.backend.messaging.message.enums.ParticipantType;
import com.muyategna.backend.messaging.message.repository.projection.MessageThreadProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MessageThreadRepository extends JpaRepository<MessageThread, Long> {

    @Query(
            """
                    SELECT mt FROM MessageThread mt
                    LEFT JOIN FETCH mt.participants p
                    WHERE mt.id = :messageThreadId
                    """
    )
    Optional<MessageThread> findByIdWithParticipants(Long messageThreadId);

    @Query("""
                SELECT DISTINCT t.id AS id,
                                t.subject AS subject,
                                t.contextType AS contextType,
                                t.contextId AS contextId
                FROM MessageParticipant mp
                JOIN mp.thread t
                WHERE mp.participantId = :participantId
                  AND mp.participantType = :participantType
            """)
    Page<MessageThreadProjection> findDistinctThreadsByParticipant(
            @Param("participantId") Long participantId,
            @Param("participantType") ParticipantType participantType,
            Pageable pageable
    );

    @Query(
            """
                    SELECT DISTINCT mt FROM MessageThread mt
                    LEFT JOIN FETCH mt.participants p
                    WHERE mt.serviceProvider.id = :serviceProviderId
                    """
    )
    Page<MessageThread> findAllByServiceProviderId(@Param("serviceProviderId") Long serviceProviderId, Pageable pageable);


    @Query(
            """
                    SELECT DISTINCT mt FROM MessageThread mt
                    LEFT JOIN FETCH mt.participants p
                    WHERE mt.customer.id = :customerId
                    """
    )
    Page<MessageThread> findAllByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
}
