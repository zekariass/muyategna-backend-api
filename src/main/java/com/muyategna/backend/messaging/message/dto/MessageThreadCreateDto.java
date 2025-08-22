package com.muyategna.backend.messaging.message.dto;

import com.muyategna.backend.messaging.message.enums.MessageContextType;
import com.muyategna.backend.messaging.message.enums.ParticipantType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageThreadCreateDto {

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotNull(message = "Context type is required")
    private MessageContextType contextType;

    @NotNull(message = "Context ID is required")
    private Long contextId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Service provider ID is required")
    private Long serviceProviderId;

    @NotNull(message = "Sender type is required")
    private ParticipantType participantType;
}
