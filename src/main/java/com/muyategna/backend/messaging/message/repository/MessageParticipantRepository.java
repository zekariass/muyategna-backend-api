package com.muyategna.backend.messaging.message.repository;

import com.muyategna.backend.messaging.message.entity.MessageParticipant;
import com.muyategna.backend.messaging.message.enums.ParticipantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageParticipantRepository extends JpaRepository<MessageParticipant, Long> {

//    @Query("""
//                SELECT DISTINCT mp
//                FROM MessageParticipant mp
//                JOIN FETCH mp.thread
//                WHERE mp.participantId = :participantId
//                  AND mp.participantType = :participantType
//            """)
//    Page<MessageParticipant> findAllByParticipantIdAndParticipantType(
//            @Param("participantId") Long participantId,
//            @Param("participantType") ParticipantType participantType,
//            Pageable pageable);

    @Query("""
                SELECT DISTINCT mp.thread
                FROM MessageParticipant mp
                WHERE mp.participantId = :participantId
                  AND mp.participantType = :participantType
            """)
    Page<MessageParticipant> findDistinctThreadsByParticipantIdAndType(
            @Param("participantId") Long participantId,
            @Param("participantType") ParticipantType participantType,
            Pageable pageable
    );

    List<MessageParticipant> findAllByThread_Id(Long threadId);
}
