package com.muyategna.backend.job_request.dto.job_quote;

import com.muyategna.backend.job_request.enums.JobQuoteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobQuoteDto {
    private Long id;
    private Long claimId;
    private BigDecimal proposedPrice;
    private LocalDateTime proposedStartDate;
    private String message;
    private JobQuoteStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
