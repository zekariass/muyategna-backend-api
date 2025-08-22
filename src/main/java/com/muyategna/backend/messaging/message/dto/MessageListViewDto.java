package com.muyategna.backend.messaging.message.dto;

import com.muyategna.backend.messaging.message.enums.ParticipantType;
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
public class MessageListViewDto {
    private Long id;
    private Long threadId;
    private Long senderId;
    private ParticipantType senderType;
    private String content;
    private Long replyToId;
    private List<String> attachments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
