package com.muyategna.backend.job_request.dto.job_lead_claim;

import com.muyategna.backend.job_request.dto.job_lead.JobLeadMinimalDto;
import com.muyategna.backend.job_request.enums.JobLeadClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobLeadClaimSPListViewDto {
    private Long id;
    private JobLeadMinimalDto jobLead;
    private Long providerId;
    private LocalDateTime claimedAt;
    private JobLeadClaimStatus status;
    private Boolean requoteRequested;
}
