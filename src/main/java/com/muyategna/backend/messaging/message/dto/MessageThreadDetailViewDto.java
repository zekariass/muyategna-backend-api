package com.muyategna.backend.messaging.message.dto;

import com.muyategna.backend.messaging.message.enums.MessageContextType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageThreadDetailViewDto {
    private Long id;
    private String subject;
    private MessageContextType contextType;
    private Long contextId;
    private Long customerId;
    private Long serviceProviderId;

    private List<MessageParticipantListViewDto> participants;
}
