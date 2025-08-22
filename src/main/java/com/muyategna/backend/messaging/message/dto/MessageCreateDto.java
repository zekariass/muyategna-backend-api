package com.muyategna.backend.messaging.message.dto;

import com.muyategna.backend.messaging.message.enums.ParticipantType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageCreateDto {
    @NotNull(message = "Thread ID is required")
    private Long threadId;

    @NotNull(message = "Sender ID is required")
    private Long senderId;

    @NotNull(message = "Sender type is required")
    private ParticipantType senderType;

    private String content;
    private List<MultipartFile> attachments = new ArrayList<>();

    private Long replyTo;
}
