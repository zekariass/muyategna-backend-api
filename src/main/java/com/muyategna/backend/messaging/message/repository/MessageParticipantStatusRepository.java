package com.muyategna.backend.messaging.message.repository;

import com.muyategna.backend.messaging.message.entity.MessageParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageParticipantStatusRepository extends JpaRepository<MessageParticipantStatus, Long> {
}
