package com.muyategna.backend.messaging.message.repository.projection;

import com.muyategna.backend.messaging.message.enums.MessageContextType;

import java.util.List;

public interface MessageThreadProjection {
    Long getId();

    String getSubject();

    MessageContextType getContextType();

    Long getContextId();

    List<MessageParticipantProjection> getParticipants();
}
