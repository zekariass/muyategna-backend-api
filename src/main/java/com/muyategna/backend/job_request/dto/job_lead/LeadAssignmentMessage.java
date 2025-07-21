package com.muyategna.backend.job_request.dto.job_lead;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeadAssignmentMessage {
    private Long jobLeadId;
    private List<Long> serviceProviderIds;
}
