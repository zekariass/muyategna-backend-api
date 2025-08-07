package com.muyategna.backend.messaging.notification.dto.queue;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequoteRequestNotificationQueueCustomerMessage {
    private String quoteId;
    private String customerId;
}
