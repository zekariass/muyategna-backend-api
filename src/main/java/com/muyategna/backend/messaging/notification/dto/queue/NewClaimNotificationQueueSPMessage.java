package com.muyategna.backend.messaging.notification.dto.queue;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewClaimNotificationQueueSPMessage {
    Long claimId;
    Long serviceProviderId;
}
