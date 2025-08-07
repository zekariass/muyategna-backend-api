package com.muyategna.backend.messaging.notification.dto.queue;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewJobRequestNotificationQueueCustomerMessage {
    private Long jobRequestId;
    private Long customerId;
}
