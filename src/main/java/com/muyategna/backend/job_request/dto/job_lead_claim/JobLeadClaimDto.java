package com.muyategna.backend.job_request.dto.job_lead_claim;

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
public class JobLeadClaimDto {
    private Long id;
    private Long jobLeadId;
    private Long providerId;
    private LocalDateTime claimedAt;
    private JobLeadClaimStatus status;
    private Boolean requoteRequested;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
