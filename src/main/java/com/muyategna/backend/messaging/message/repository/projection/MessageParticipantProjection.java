package com.muyategna.backend.messaging.message.repository.projection;

import com.muyategna.backend.messaging.message.enums.ParticipantType;

public interface MessageParticipantProjection {
    Long getId();

    Long getThreadId();

    ParticipantType getParticipantType();

    Long getParticipantId();

    Long getUserProfileId();

    Integer getUnreadCount();
}
