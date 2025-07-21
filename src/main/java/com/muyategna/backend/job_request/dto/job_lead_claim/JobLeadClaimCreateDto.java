package com.muyategna.backend.job_request.dto.job_lead_claim;

import com.muyategna.backend.job_request.dto.job_quote.JobQuoteCreateDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobLeadClaimCreateDto {
    @NotNull(message = "Job Lead ID cannot be null")
    private Long jobLeadId;

    @NotNull(message = "Service Provider ID cannot be null")
    private Long serviceProviderId;
    private JobQuoteCreateDto jobQuote;
}
