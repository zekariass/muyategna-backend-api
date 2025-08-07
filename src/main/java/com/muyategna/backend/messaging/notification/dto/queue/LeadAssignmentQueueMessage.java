package com.muyategna.backend.messaging.notification.dto.queue;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadAssignmentQueueMessage {
    private Long jobLeadId;
    private List<Long> serviceProviderIds;
}
