package com.muyategna.backend.messaging.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private Long id;
    private Long threadId;
    private Long senderId;
    private String content;
    private Long replyToId;
    private List<String> attachments;

//    List<String> attachments;
//    List<MessageParticipantStatusDto> participantStatuses;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
