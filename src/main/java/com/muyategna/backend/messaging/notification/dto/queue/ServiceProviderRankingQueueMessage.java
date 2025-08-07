package com.muyategna.backend.messaging.notification.dto.queue;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProviderRankingQueueMessage {
    private Long jobLeadId;
    private Long jobRequestId;
    private Long countryId;
}
