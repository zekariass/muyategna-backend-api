package com.muyategna.backend.messaging.notification.dto.queue;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimStatusChangeNotificationQueueSPMessage {
    private String claimId;
    private Long serviceProviderId;
}
