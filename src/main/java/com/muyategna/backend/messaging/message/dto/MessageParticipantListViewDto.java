package com.muyategna.backend.messaging.message.dto;

import com.muyategna.backend.messaging.message.enums.ParticipantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageParticipantListViewDto {
    private Long id;
    private Long threadId;
    private ParticipantType participantType;
    private Long participantId;
    private Long userProfileId;
    private Integer unreadCount;
}
