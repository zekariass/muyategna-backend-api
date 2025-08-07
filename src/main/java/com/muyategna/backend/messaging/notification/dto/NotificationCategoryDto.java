package com.muyategna.backend.messaging.notification.dto;

import com.muyategna.backend.messaging.notification.enums.NotificationRecipientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationCategoryDto {
    private Long id;
    private String name;
    private NotificationRecipientType recipientType;
    private Boolean userCanOptOut;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
