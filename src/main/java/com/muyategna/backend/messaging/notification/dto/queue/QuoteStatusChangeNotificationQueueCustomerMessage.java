package com.muyategna.backend.messaging.notification.dto.queue;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteStatusChangeNotificationQueueCustomerMessage {
    private Long quoteId;
    private Long customerId;
}
