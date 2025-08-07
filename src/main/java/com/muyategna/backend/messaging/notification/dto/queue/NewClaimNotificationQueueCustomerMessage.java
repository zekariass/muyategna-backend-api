package com.muyategna.backend.messaging.notification.dto.queue;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewClaimNotificationQueueCustomerMessage {
    private Long claimId;
    private Long customerId;
}
