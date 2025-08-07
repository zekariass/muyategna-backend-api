package com.muyategna.backend.messaging.notification.dto.queue;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewLeadNotificationQueueSPMessage {
    private List<Long> serviceProviderIds;
    private Long leadId;
    private Long jobRequestId;
    private Long countryId;
}
