package com.muyategna.backend.job_request.dto.job_lead_claim;

import com.muyategna.backend.job_request.dto.job_lead.JobLeadDetailDto;
import com.muyategna.backend.job_request.dto.job_quote.JobQuoteMinimalDto;
import com.muyategna.backend.job_request.enums.JobLeadClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobLeadClaimDetailSPViewDto {
    private Long id;
    private JobLeadDetailDto jobLead;
    private Long providerId;
    private LocalDateTime claimedAt;
    private JobLeadClaimStatus status;
    private Boolean requoteRequested;
    private List<JobQuoteMinimalDto> quotes;
}
